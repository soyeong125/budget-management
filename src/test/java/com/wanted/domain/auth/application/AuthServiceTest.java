package com.wanted.domain.auth.application;

import com.wanted.domain.member.MemberTestHelper;
import com.wanted.domain.member.dao.MemberRepository;
import com.wanted.domain.member.dto.request.MemberSignUpReqDto;
import com.wanted.domain.member.entity.Member;
import com.wanted.global.config.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("회원 가입 관련 서비스 테스트")
    class SignUp{

        Member member = MemberTestHelper.createMememberWithId();

        @Test
        @DisplayName("회원가입이 성공하여 ID를 반환한다.")
        void 회원_가입이_성공하여_ID_를_반환한다(){
            MemberSignUpReqDto memberSignUpReqDto =
                    MemberSignUpReqDto.builder()
                            .account(member.getAccount())
                            .password(member.getPassword())
                            .passwordConfirm(member.getPassword())
                            .build();

            given(memberRepository.save(any())).willReturn(member);
            given(passwordEncoder.encode(any())).willReturn("test1234*");

            Long createdMemberId = authService.signUp(memberSignUpReqDto);

            assertThat(1L).isEqualTo(createdMemberId);

        }

        @Test
        @DisplayName("아이디가 중복일 경우, 예외가 발생한다.")
        void 아이디가_중복일_경우_예외가_발생한다(){
            MemberSignUpReqDto memberSignUpReqDto =
                    MemberSignUpReqDto.builder()
                            .account(member.getAccount())
                            .password(member.getPassword())
                            .passwordConfirm(member.getPassword())
                            .build();

            given(memberRepository.findByAccount("test")).willReturn(Optional.of(member)); // 중복된 아이디

            assertThatThrownBy(
                    () -> authService.checkDuplicateAccount(memberSignUpReqDto.getAccount())
            );
        }
    }
}