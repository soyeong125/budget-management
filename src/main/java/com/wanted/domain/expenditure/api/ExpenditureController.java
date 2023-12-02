package com.wanted.domain.expenditure.api;

import com.wanted.domain.expenditure.application.ExpenditureService;
import com.wanted.domain.expenditure.dto.request.ExpenditureCreateReqDto;
import com.wanted.domain.expenditure.dto.request.ExpenditureUpdateReqDto;
import com.wanted.domain.expenditure.dto.response.ExpenditureRecommendResDto;
import com.wanted.domain.expenditure.dto.response.ExpenditureTodayResDto;
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
     * 지출을 수정한다.
     *
     * @param expenditureId 수정할 지출의 ID
     * @param reqDto        지출 수정 요청 파라미터
     * @return 수정된 지출의 ID
     */
    @PutMapping("/{expenditureId}")
    public ResponseEntity<ResponseApi> updateExpenditure(
            @PathVariable Long expenditureId,
            @RequestBody @Validated ExpenditureUpdateReqDto reqDto
    ) {
        return ResponseEntity.ok(ResponseApi.toSuccessForm(expenditureService.updateExpenditure(expenditureId, reqDto)));
    }

    /**
     * 지출의 상세 내역을 조회한다.
     *
     * @param expenditureId 조회할 지출의 ID
     * @return 지출 상세 정보
     */
    @GetMapping("/{expenditureId}")
    public ResponseEntity<ResponseApi> getExpenditure(
            @PathVariable Long expenditureId
    ) {
        return ResponseEntity.ok(ResponseApi.toSuccessForm(expenditureService.getExpenditure(expenditureId)));
    }

    /**
     * 지출을 삭제한다.
     *
     * @param expenditureId 삭제할 지출의 ID
     * @return 삭제된 지출의 ID
     */
    @DeleteMapping("/{expenditureId}")
    public ResponseEntity<ResponseApi> deleteExpenditure(
            @PathVariable Long expenditureId
    ) {
        return ResponseEntity.ok(ResponseApi.toSuccessForm(expenditureService.deleteExpenditure(expenditureId)));
    }

    /**
     * 오늘 사용 가능한 지출 추천 금액을 반환한다.
     *
     * @param account 사용자 계정
     * @return 추천 지출 금액
     */
    @GetMapping("/recommendation")
    public ResponseEntity<ResponseApi> createExpenditureRecommendation(
            @RequestHeader String account
    ) {
        // 지출 추천 정보를 생성한다.
        ExpenditureRecommendResDto expenditureRecommendation = expenditureService.createExpenditureRecommendation(
                account);

        return ResponseEntity.ok(ResponseApi.toSuccessForm(expenditureRecommendation));
    }

    /**
     * 오늘 지출 금액 안내 정보를 생성한다.
     *
     * @param account 사용자 계정
     * @return 오늘 지출 정보
     */
    @GetMapping("/today")
    public ResponseEntity<ResponseApi> createExpenditureToday(
            @RequestHeader String account
    ) {

        ExpenditureTodayResDto expenditureToday = expenditureService.createExpenditureToday(
                account);

        return ResponseEntity.ok(ResponseApi.toSuccessForm(expenditureToday));
    }
}
