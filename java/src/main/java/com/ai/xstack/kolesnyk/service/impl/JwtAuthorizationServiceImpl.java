package com.ai.xstack.kolesnyk.service.impl;

import com.ai.xstack.kolesnyk.entity.UserEntity;
import com.ai.xstack.kolesnyk.exception.NoJwtInHeadersException;
import com.ai.xstack.kolesnyk.service.JwtAuthorizationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.AuthenticationException;

@Component
@Log4j2
public class JwtAuthorizationServiceImpl implements JwtAuthorizationService {
    private static final long TOKEN_EXPIRATION_IN_MILLIS = 86_400_000L;
    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final String secretKey;
    private final JwtParser jwtParser;
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationServiceImpl(@Value("${security.jwt.token.secret}") String secretKey, UserDetailsService userDetailsService) {
        this.secretKey = secretKey;
        this.jwtParser = Jwts.parser().setSigningKey(secretKey);
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String createToken(UserEntity user) {
        String username = user.getUsername();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("roles", getRolesCommaSeparatedString(userDetails));
        Date now = new Date();
        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_EXPIRATION_IN_MILLIS))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        log.debug(String.format("created token for user %s", username));
        return jwtToken;
    }

    @Override
    public String getTokenFromUserRequest(HttpServletRequest request) {
        String authorizationToken = request.getHeader(TOKEN_HEADER);
        if (authorizationToken != null && authorizationToken.startsWith(TOKEN_PREFIX)) {
            return authorizationToken.substring(TOKEN_PREFIX.length());
        }
        throw new NoJwtInHeadersException();
    }

    @Override
    public Claims parseTokenToClaims(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        if (!isValidClaims(claims)) {
            throw new IllegalArgumentException("claims are invalid");
        }
        return claims;
    }

    private static String getRolesCommaSeparatedString(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private boolean isValidClaims(Claims claims) throws AuthenticationException {
        return claims != null
                && claims.getExpiration().after(new Date());
    }
}