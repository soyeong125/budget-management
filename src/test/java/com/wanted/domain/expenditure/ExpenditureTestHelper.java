package com.wanted.domain.expenditure;

import com.wanted.domain.budget.BudgetCategoryTestHelper;
import com.wanted.domain.category.cost.constants.CategoryName;
import com.wanted.domain.expenditure.entity.Expenditure;
import com.wanted.domain.member.MemberTestHelper;

import java.util.Locale;

import static java.time.LocalDateTime.now;

/**
 * 지출 관련 테스트 헬퍼
 */
public class ExpenditureTestHelper {

    public static Expenditure createExpenditure() {
        return Expenditure.builder()
                .id(1L)
                .member(MemberTestHelper.createMememberWithId())
                .category(BudgetCategoryTestHelper.createBudgetCategory())
                .cost(10000)
                .time(now())
                .memo("test memo")
                .isExcluded(false)
                .build();
    }
}
