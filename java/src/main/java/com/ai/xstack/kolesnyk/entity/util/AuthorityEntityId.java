package com.ai.xstack.kolesnyk.entity.util;

import com.ai.xstack.kolesnyk.entity.enums.AuthorityRole;
import lombok.Data;

import java.io.Serializable;

@Data
public class AuthorityEntityId implements Serializable {
    private String username;

    private AuthorityRole authority;
}

