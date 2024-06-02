package com.ai.xstack.kolesnyk.entity;

import com.ai.xstack.kolesnyk.entity.util.AuthorityEntityId;
import com.ai.xstack.kolesnyk.entity.enums.AuthorityRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorities")
@IdClass(AuthorityEntityId.class)
public class AuthorityEntity {
    @Id
    private String username;

    @Id
    @Enumerated(EnumType.STRING)
    private AuthorityRole authority;
}
