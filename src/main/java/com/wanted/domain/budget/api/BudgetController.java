package com.wanted.domain.budget.api;

import com.wanted.domain.budget.application.BudgetService;
import com.wanted.domain.budget.dto.request.BudgetCreateReqDto;
import com.wanted.domain.budget.dto.request.BudgetRecommendReqDto;
import com.wanted.domain.budget.dto.response.BudgetRecommendResDto;
import com.wanted.global.format.response.ResponseApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    /**
     * 예산 설정
     *
     * @param reqDto 예산 생성 요청 dto (유저 Id, 카테고리명, 금액)
     * @return 생성된 예산 Id
     */
    @PostMapping
    public ResponseEntity<ResponseApi> createBudget(
            @RequestBody @Valid BudgetCreateReqDto reqDto) {
        Long budgetId = budgetService.createBudget(reqDto);
        return ResponseEntity.ok(ResponseApi.toSuccessForm(budgetId));
    }

    /**
     * 예산 설계 추천
     * 예산 생성 기준은 기존 이용중인 사용자들이 설정한 평균 값.
     *
     * @param reqDto 예산 추천 요청 dto
     * @return 예산 설계 추천 결과
     */
    @GetMapping("/recommend")
    public ResponseEntity<ResponseApi> recommendDeposit(
            @Valid @RequestBody BudgetRecommendReqDto reqDto
    ) {
        BudgetRecommendResDto resDto = budgetService.createBudgetRecommendation(reqDto);

        return ResponseEntity.ok(ResponseApi.toSuccessForm(resDto));
    }


    /**
     * 예산 수정
     *
     * @param depositId 수정할 예산 아이디
     * @param reqDto    수정 데이터 정보
     * @return 201, 수정된 예산 아이디
     */
    @PutMapping("/{depositId}")
    public ResponseEntity<ResponseApi> updateDeposit(
            @PathVariable(name = "depositId") Long depositId,
            @Valid @RequestBody BudgetCreateReqDto reqDto
    ) {
        Long updatedDeposit = budgetService.updateDeposit(depositId, reqDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseApi.toSuccessForm(updatedDeposit));
    }

}
