package com.wanted.domain.member.dao;

import com.wanted.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    /**
     * 계정 아이디로 회원 찾기
     *
     * @param account
     * @return Optional 회원
     */
    Optional<Member> findByAccount(String account);
}
