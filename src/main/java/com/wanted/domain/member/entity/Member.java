package com.wanted.domain.member.entity;

import com.wanted.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * 회원 Entity
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member")
public class Member extends BaseTimeEntity {

    private static final int ACCOUNT_MAX_LENGTH = 20;
    private static final int PASSWORD_MAX_LENGTH = 256;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id" , nullable = false)
    private Long id;

    @Column(name = "account", nullable = false, length = ACCOUNT_MAX_LENGTH)
    private String account;

    @Column(name = "password", nullable = false, length = PASSWORD_MAX_LENGTH)
    private String password;

    @Builder
    public Member(Long id, String account, String password) {
        this.id = id;
        this.account = account;
        this.password = password;
    }
}
