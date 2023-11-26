package com.wanted.domain.auth.api;

import com.wanted.domain.auth.application.AuthService;
import com.wanted.domain.member.dto.request.MemberLoginReqDto;
import com.wanted.domain.member.dto.request.MemberSignUpReqDto;
import com.wanted.global.format.response.ResponseApi;
import com.wanted.global.security.data.TokenDto;
import com.wanted.global.security.data.TokenReqDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     *
     * @param reqDto 회원가입 입력 데이터
     * @return 201, 생성된 회원의 id
     */
    @PostMapping("/signup")
    public ResponseEntity<ResponseApi> signUp(
            @Valid @RequestBody MemberSignUpReqDto reqDto){

        reqDto.validPasswordConfirm(); // 비밀번호 확인 검증

        Long memberId = authService.signUp(reqDto);
        return ResponseEntity.status(CREATED)
                .body(ResponseApi.toSuccessForm(memberId));
    }

    /**
     * 아이디 중복 확인
     *
     * @param account 확인할 아이디
     * @return 204
     */
    @GetMapping("/signup/exists/account")
    public ResponseEntity<ResponseApi> checkDuplicateAccount(
            @RequestParam @NotNull String account) {
        authService.checkDuplicateAccount(account); // 아이디 중복 확인

        return ResponseEntity.status(NO_CONTENT)
                .body(ResponseApi.toSuccessForm(""));
    }

    /**
     * 로그인 및 토큰 발급
     *
     * @param reqDto 로그인 입력 데이터
     * @return 200, JWT 토큰
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseApi> login(
            @RequestBody MemberLoginReqDto reqDto
    ) {
        TokenDto jwtToken = authService.login(reqDto);

        return ResponseEntity.ok(ResponseApi.toSuccessForm(jwtToken));
    }

    /**
     * 토큰 재발급
     *
     * @param reqDto 토큰 정보 dto
     * @return 200, 재발급 된 JWT 토큰
     */
    @PostMapping("/reissue")
    public ResponseEntity<ResponseApi> reissue(
            @RequestBody TokenReqDto reqDto
    ) {
        TokenDto jwtToken = authService.reissue(reqDto);

        return ResponseEntity.ok(ResponseApi.toSuccessForm(jwtToken));
    }

    /**
     * 로그아웃 및 토큰 삭제
     *
     * @param reqDto 토큰 정보
     * @return 200, 토큰 삭제
     */
    @PostMapping("/logout")
    public ResponseEntity<ResponseApi> logout(
            @RequestBody TokenReqDto reqDto
    ) {
        TokenDto jwtToken = authService.reissue(reqDto);

        return ResponseEntity.ok(ResponseApi.toSuccessForm(null));
    }
}
