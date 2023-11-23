package com.wanted.domain.auth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.budgetManagement.config.restdocs.AbstractRestDocsTests;
import com.wanted.domain.auth.application.AuthService;
import com.wanted.domain.member.MemberTestHelper;
import com.wanted.domain.member.dto.request.MemberSignUpReqDto;
import com.wanted.domain.member.entity.Member;
import com.wanted.global.config.security.SecurityConfig;
import com.wanted.global.error.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static com.wanted.global.error.ErrorCode.MEMBER_ACCOUNT_DUPLICATE;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest extends AbstractRestDocsTests {

    private static final String AUTH_URL = "/api/v1/auth";

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private AuthService authService;

    private Member member;

    @BeforeEach
    void setUp(){
        member = MemberTestHelper.createMememberWithId();
    }

    @Nested
    @DisplayName("회원 가입 관련 컨트롤러 테스트")
    class Signup {

        @Test
        @DisplayName("회원가입이 정상적으로 성공한다.")
        void 회원가입이_정상적으로_성공한다() throws Exception{
            MemberSignUpReqDto reqDto =
                    MemberSignUpReqDto.builder()
                            .account(member.getAccount())
                            .password(member.getPassword())
                            .passwordConfirm(member.getPassword())
                            .build();

            given(authService.signUp(reqDto)).willReturn(1L);

            mockMvc.perform(post(AUTH_URL + "/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reqDto)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("아이디가 올바르지 않으면 회원가입에 실패한다.")
        void 아이디가_올바르지_않으면_회원가입에_실패한다() throws Exception{
            MemberSignUpReqDto reqDto =
                    MemberSignUpReqDto.builder()
                            .account("")
                            .password(member.getPassword())
                            .passwordConfirm(member.getPassword())
                            .build();

            given(authService.signUp(reqDto)).willReturn(1L);

            mockMvc.perform(post(AUTH_URL + "/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reqDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("패스워드가 올바르지 않으면 회원가입에 실패한다.")
        void 패스워드가_올바르지_않으면_회원가입에_실패한다() throws Exception{
            MemberSignUpReqDto reqDto =
                    MemberSignUpReqDto.builder()
                            .account("")
                            .password("1234")
                            .passwordConfirm("1234")
                            .build();

            given(authService.signUp(reqDto)).willReturn(1L);

            mockMvc.perform(post(AUTH_URL + "/signup")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reqDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(containsString("비밀번호는 특수문자를 포함한 8~16자리 수 여야만 합니다.")));
        }

        @Test
        @DisplayName("아이디 중복 확인에 성공한다.")
        void 아이디_중복_확인에_성공한다() throws Exception{
            String account = "testAccount";

            doNothing().when(authService).checkDuplicateAccount(account);

            mockMvc.perform(get("/api/v1/auth/signup/exists/account")
                            .param("account", account)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("아이디 중복 확인에 실패한다.")
        void 아이디_중복_확인에_실패한다() throws Exception{
            String account = "duplicateAccount";

            doThrow(new BusinessException(account, "account", MEMBER_ACCOUNT_DUPLICATE))
                    .when(authService)
                    .checkDuplicateAccount(account);

            mockMvc.perform(get("/api/v1/auth/signup/exists/account")
                            .param("account", account)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }


    }


}