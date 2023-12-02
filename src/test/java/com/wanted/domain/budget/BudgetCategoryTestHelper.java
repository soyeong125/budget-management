package com.wanted.domain.budget;

import com.wanted.domain.category.cost.constants.CategoryName;
import com.wanted.domain.category.cost.entity.CostCategory;

/**
 * 예산 카테고리 관련 테스트 헬퍼
 */
public class BudgetCategoryTestHelper {

    public static CostCategory createBudgetCategory() {
        return CostCategory.builder()
                .id(1L)
                .name(CategoryName.Food)
                .build();
    }
}
