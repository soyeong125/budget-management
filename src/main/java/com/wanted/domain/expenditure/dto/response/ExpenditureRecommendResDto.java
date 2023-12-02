package com.wanted.domain.expenditure.dto.response;

import com.wanted.domain.category.cost.entity.CostCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class ExpenditureRecommendResDto {

    private final Integer totalAmount;

    private final String expenditureMessage;

    @Builder
    private ExpenditureRecommendResDto(Integer totalAmount, Map<CostCategory, Integer> amountPerCategory, String expenditureMessage) {
        this.totalAmount = totalAmount;
        this.expenditureMessage = expenditureMessage;
    }
}
