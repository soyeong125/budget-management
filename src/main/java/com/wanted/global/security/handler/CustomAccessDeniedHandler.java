package com.wanted.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.global.error.ErrorCode;
import com.wanted.global.format.response.ResponseApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인가 괴정에서 발생하는 예외 처리
 * 인증 후, 권한 없이 접근 시 발생하는 예외 핸들러
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    /**
     * ApiResponse 양식에 맞게 Response 커스텀
     *
     * @param request               request
     * @param response              response
     * @param accessDeniedException 권한 없을 때 예외
     * @throws IOException 입출력 예외
     */
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json; charset=UTF-8");

        ResponseApi body = ResponseApi.toErrorForm(ErrorCode.ACCESS_DENIED_EXCEPTION.getMessage());

        response.getWriter().write(mapper.writeValueAsString(body));
    }
}