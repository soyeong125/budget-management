package com.wanted.domain.budget.api;

import com.wanted.domain.budget.application.BudgetService;
import com.wanted.domain.budget.dto.request.BudgetCreateReqDto;
import com.wanted.global.format.response.ResponseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ResponseApi> createBudget(@RequestBody BudgetCreateReqDto reqDto) {
        Long budgetId = budgetService.createBudget(reqDto);
        return ResponseEntity.ok(ResponseApi.toSuccessForm(budgetId));
    }
}
