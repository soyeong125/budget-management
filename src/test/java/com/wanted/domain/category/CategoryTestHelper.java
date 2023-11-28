package com.wanted.domain.category;

import com.wanted.domain.category.cost.constants.CategoryName;
import com.wanted.domain.category.cost.entity.CostCategory;

public class CategoryTestHelper {
    public static CostCategory createBudgetCategory() {
        return CostCategory.builder()
                .id(1L)
                .name(CategoryName.Food)
                .build();
    }
}
