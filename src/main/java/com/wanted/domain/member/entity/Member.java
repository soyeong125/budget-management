package com.wanted.domain.member.entity;

import com.wanted.domain.budget.entity.Budget;
import com.wanted.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.wanted.domain.member.constants.MemberConstant.ACCOUNT_MAX_LENGTH;
import static com.wanted.domain.member.constants.MemberConstant.PASSWORD_MAX_LENGTH;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 회원 Entity
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id" , nullable = false)
    private Long id;

    @Column(name = "account", nullable = false, length = ACCOUNT_MAX_LENGTH)
    private String account;

    @Column(name = "password", nullable = false, length = PASSWORD_MAX_LENGTH)
    private String password;

    @Enumerated(STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Builder
    public Member(Long id, String account, String password, Role role,) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.role = role;
    }
}
