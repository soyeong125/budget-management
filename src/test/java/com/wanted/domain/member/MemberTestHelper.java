package com.wanted.domain.member;

import com.wanted.domain.member.entity.Member;
import com.wanted.domain.member.entity.Role;

/**
 * 회원에 관한 테스트 헬퍼
 */
public class MemberTestHelper {

    public static Member createMememberWithId(){
        return Member.builder()
                .id(1L)
                .account("test1234")
                .password("test1234*")
                .role(Role.USER)
                .build();
    }
}
