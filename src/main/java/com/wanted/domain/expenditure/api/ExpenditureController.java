package com.wanted.domain.expenditure.api;

import com.wanted.domain.expenditure.application.ExpenditureService;
import com.wanted.domain.expenditure.dto.request.ExpenditureCreateReqDto;
import com.wanted.global.format.response.ResponseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/expenditure")
public class ExpenditureController {

    private final ExpenditureService expenditureService;

    /**
     * 지출 작성
     *
     * @param reqDto 지출 작성에 필요한 데이터 정보
     * @return 201, 작성된 지출의 id
     */
    @PostMapping
    private ResponseEntity<ResponseApi> createExpenditure(
            @Valid @RequestBody ExpenditureCreateReqDto reqDto
    ) {
        Long wroteExpenditureId = expenditureService.createExpenditure(reqDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseApi.toSuccessForm(wroteExpenditureId));
    }
}
