package com.wanted.domain.budget.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예산 생성 요청 DTO
 */
@Getter
@NoArgsConstructor
public class BudgetCreateReqDto {
    private Long memberId;
    private String categoryName;
    private Integer cost;

    @Builder
    private BudgetCreateReqDto(Long memberId, String categoryName, Integer cost) {
        this.memberId = memberId;
        this.categoryName = categoryName;
        this.cost = cost;
    }
}
