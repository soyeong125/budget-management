package com.wanted.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 오류 메시지와 상태를 쉽게 추가하기 위한 Enum
 */
@Getter
public enum ErrorCode {
    //Member
    MEMBER_WRONG_PASSWORD_CONFIRM("비밀번호가 서로 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_ACCOUNT_DUPLICATE("중복된 아이디 입니다.", HttpStatus.BAD_REQUEST),
    MEMBER_ACCOUNT_NOT_FOUND("아이디를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MEMBER_PASSWORD_BAD_REQUEST("비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_FOUND("존재하지 않은 회원입니다", HttpStatus.NOT_FOUND),

    // Security
    ACCESS_DENIED_EXCEPTION("필요한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ACCESS_AUTH_ENTRY_EXCEPTION("유요한 자격이 없습니다.", HttpStatus.UNAUTHORIZED),
    SPRING_CONTEXT_NOT_FOUND("Spring Context에 해당 정보가 없습니다.", HttpStatus.BAD_REQUEST),

    // JWT
    MEMBER_LOGOUT("로그아웃 된 사용자입니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_BAD_REQUEST("Refresh Token 이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_MISMATCH("Refresh Token 이 알맞지 않습니다.", HttpStatus.BAD_REQUEST),
    ACCESS_TOKEN_BAD_REQUEST("Access Token 이 알맞지 않습니다.", HttpStatus.BAD_REQUEST),

    // 예산 카테고리
    BUDGET_CATEGORY_NOT_FOUND("존재하지 않는 예산 카테고리입니다.", HttpStatus.BAD_REQUEST),
    BUDGET_NOT_FOUND("존재하지 않는 예산입니다.", HttpStatus.BAD_REQUEST),

    // 지출
    EXPENDITURE_NOT_FOUND("존재하지 않는 지출입니다.", HttpStatus.BAD_REQUEST),
    ;

    //오류 메시지
    private final String message;
    //오류 상태코드
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
