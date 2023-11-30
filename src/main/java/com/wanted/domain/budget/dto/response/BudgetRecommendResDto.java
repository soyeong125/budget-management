package com.wanted.domain.budget.dto.response;

import java.util.Map;
import com.wanted.domain.category.cost.constants.CategoryName;
import lombok.Getter;

@Getter
public class BudgetRecommendResDto {

    Map<CategoryName, Integer> recommendBudgets;

    public BudgetRecommendResDto(Map<CategoryName, Integer> recommendBudgets) {
        this.recommendBudgets = recommendBudgets;
    }
}
