package com.wanted.domain.expenditure.api;

import com.wanted.domain.expenditure.application.ExpenditureService;
import com.wanted.domain.expenditure.dto.request.ExpenditureCreateReqDto;
import com.wanted.domain.expenditure.dto.request.ExpenditureUpdateReqDto;
import com.wanted.global.format.response.ResponseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 지출 수정
     *
     * @param expenditureId 수정할 지출의 ID
     * @param reqDto 지출 수정 요청 파라미터
     * @return 수정된 지출의 ID
     */
    @PutMapping("/{expenditureId}")
    public ResponseEntity<ResponseApi> updateExpenditure(
            @PathVariable Long expenditureId,
            @RequestBody @Validated ExpenditureUpdateReqDto reqDto
            ) {
        return ResponseEntity.ok(ResponseApi.toSuccessForm(expenditureService.updateExpenditure(expenditureId, reqDto)));
    }
}
