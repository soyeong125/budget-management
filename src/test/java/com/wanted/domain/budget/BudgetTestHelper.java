package com.wanted.domain.budget;

import com.wanted.domain.budget.entity.Budget;
import com.wanted.domain.category.CategoryTestHelper;
import com.wanted.domain.member.MemberTestHelper;

/**
 * 예산 관련 테스트 헬퍼
 */
public class BudgetTestHelper {

    public static Budget createBudget() {
        return Budget.builder()
                .id(1L)
                .member(MemberTestHelper.createMememberWithId())
                .category(CategoryTestHelper.createBudgetCategory())
                .cost(100000)
                .build();
    }
}
