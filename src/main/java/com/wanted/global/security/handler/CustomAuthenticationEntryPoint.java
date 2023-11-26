package com.wanted.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.global.error.ErrorCode;
import com.wanted.global.format.response.ResponseApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증 과정에서 발생하는 예외 처리
 * 유효한 자격증명을 제공하지 않고 접근하려 할때 발생하는 예외 핸들러
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    /**
     * ApiResponse 양식에 맞게 Response 커스텀
     *
     * @param request       request
     * @param response      response
     * @param authException 유효하지 않은 자격 증명 예외
     * @throws IOException 입출력 예외
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");

        ResponseApi body = ResponseApi.toErrorForm(ErrorCode.ACCESS_AUTH_ENTRY_EXCEPTION.getMessage());

        response.getWriter().write(mapper.writeValueAsString(body));
    }
}
