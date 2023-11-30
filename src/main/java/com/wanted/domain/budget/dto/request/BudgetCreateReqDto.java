package com.wanted.domain.budget.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예산 생성 요청 DTO
 */
@Getter
@NoArgsConstructor
public class BudgetCreateReqDto {
    @NotNull(message = "사용자 ID를 입력해주세요")
    private Long memberId;
    @NotNull(message = "카테고리를 선택해주세요")
    private String categoryName;
    @PositiveOrZero(message = "예산은 0 이상이어야 합니다")
    private Integer cost;

    @Builder
    private BudgetCreateReqDto(Long memberId, String categoryName, Integer cost) {
        this.memberId = memberId;
        this.categoryName = categoryName;
        this.cost = cost;
    }
}
