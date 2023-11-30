package com.wanted.domain.budget.application;

import com.wanted.domain.budget.BudgetTestHelper;
import com.wanted.domain.budget.dao.BudgetRepository;
import com.wanted.domain.budget.dto.request.BudgetCreateReqDto;
import com.wanted.domain.budget.entity.Budget;
import com.wanted.domain.category.cost.dao.CostCategoryRepository;
import com.wanted.domain.member.dao.MemberRepository;
import com.wanted.global.error.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    static final Budget budget = BudgetTestHelper.createBudget();

    @InjectMocks
    BudgetService budgetService;

    @Mock
    BudgetRepository budgetRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    CostCategoryRepository budgetCategoryRepository;

    @Nested
    @DisplayName("예산 설정 관련 서비스 테스트")
    class createBudget {
        @Test
        @DisplayName("예산 설정에 성공한다.")
        void 예산_설정에_성공힌디() {
            BudgetCreateReqDto reqDto = BudgetCreateReqDto.builder()
                    .memberId(budget.getMember().getId())
                    .categoryName(budget.getCategory().getName().name())
                    .cost(budget.getCost())
                    .build();

            given(memberRepository.findById(any())).willReturn(Optional.of(budget.getMember()));
            given(budgetCategoryRepository.findByName(any())).willReturn(Optional.of(budget.getCategory()));
            given(budgetRepository.save(any())).willReturn(budget);

            Long budgetId = budgetService.createBudget(reqDto);

            assertThat(budgetId).isEqualTo(budget.getId());
        }

        @Test
        @DisplayName("존재하지 않는 사용자로 요청하면 실패")
        void 존재하지_않는_사용자로_요청하면_실패() {
            Long wrongId = 10L;
            BudgetCreateReqDto reqDto = BudgetCreateReqDto.builder()
                    .memberId(wrongId)
                    .categoryName(budget.getCategory().getName().name())
                    .cost(budget.getCost())
                    .build();

            given(memberRepository.findById(any())).willReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> budgetService.createBudget(reqDto));
        }

        @Test
        @DisplayName("존재하지 않는 카테고리로 요청하면 실패")
        void 존재하지_않는_카테고리로_요청하면_실패() {
            BudgetCreateReqDto param = BudgetCreateReqDto.builder()
                    .memberId(budget.getMember().getId())
                    .categoryName("Invalid Category")
                    .cost(budget.getCost())
                    .build();

            given(memberRepository.findById(any())).willReturn(Optional.of(budget.getMember()));
            given(budgetCategoryRepository.findByName(any())).willReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> budgetService.createBudget(param));
        }
    }
}
