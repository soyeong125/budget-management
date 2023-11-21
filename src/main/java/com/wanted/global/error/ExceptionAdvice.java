package com.wanted.global.error;

import com.wanted.global.format.response.ResponseApi;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 전역 예외 처리
 */
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * BindException 오류가 발생할 때, Response 처리.
     *
     * @param e BindException
     * @return Response 내용
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseApi> bindException(BindException e) {
        String errorMessage = getErrorMessage(e);

        return ResponseEntity.badRequest()
                .body(ResponseApi.toErrorForm(errorMessage));
    }

    /**
     * BusinessException 오류가 발생할 때, Response 처리.
     *
     * @param e BusinessException
     * @return Response 내용
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseApi> businessException(BusinessException e) {
        String errorMessage = getErrorMessage(e.getInvalidValue(), e.getFieldName(), e.getMessage());

        return ResponseEntity.status(e.getHttpStatus())
                .body(ResponseApi.toErrorForm(errorMessage));
    }

    /**
     * BindingException의 bindingResult 분석 후, 오류 메시지 생성
     *
     * @param e BindException
     * @return 포멧팅된 오류 메시지
     */
    private static String getErrorMessage(BindException e) {
        BindingResult bindingResult = e.getBindingResult();

        return bindingResult.getFieldErrors().stream()
                .map(fieldError ->
                        getErrorMessage(
                                (String) fieldError.getRejectedValue(),
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                )
                .collect(Collectors.joining(", "));
    }

    /**
     * 메시지 포멧팅
     */
    private static String getErrorMessage(String invalidValue, String errorField,
                                          String errorMessage) {
        return String.format("[%s] %s: %s", invalidValue, errorField, errorMessage);
    }
}
