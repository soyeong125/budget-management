package com.wanted.domain.budget.api;

import static com.wanted.global.error.ErrorCode.BUDGET_CATEGORY_NOT_FOUND;
import static com.wanted.global.error.ErrorCode.MEMBER_NOT_FOUND;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wanted.budgetManagement.config.restdocs.AbstractRestDocsTests;
import com.wanted.domain.budget.BudgetTestHelper;
import com.wanted.domain.budget.application.BudgetService;
import com.wanted.domain.budget.dto.request.BudgetCreateReqDto;
import com.wanted.domain.budget.entity.Budget;
import com.wanted.global.error.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BudgetController.class)
class BudgetControllerTest extends AbstractRestDocsTests {

    static final String BUDGET_URL = "/api/v1/budgets";
    static final Budget budget = BudgetTestHelper.createBudget();

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BudgetService budgetService;

    @Nested
    @DisplayName("예산 설정 관련 컨트롤러 테스트")
    class createBudget {
        @Test
        @DisplayName("예산 설정에 성공한다.")
        void 예산_설정에_성공한다() throws Exception {
            BudgetCreateReqDto reqDto = BudgetCreateReqDto.builder()
                    .memberId(budget.getId())
                    .categoryName(budget.getCategory().getName().name())
                    .cost(budget.getCost())
                    .build();

            given(budgetService.createBudget(any())).willReturn(budget.getId());

            mockMvc.perform(post(BUDGET_URL)
                            .contentType(APPLICATION_JSON).content(mapper.writeValueAsString(reqDto)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("존재하지 않는 사용자로 요청하면 실패한다")
        void 존재하지_않는_사용자로_요청하면_실패한다() throws Exception {
            Long invalidId = 10L;
            BudgetCreateReqDto reqDto = BudgetCreateReqDto.builder()
                    .memberId(invalidId)
                    .categoryName(budget.getCategory().getName().name())
                    .cost(budget.getCost())
                    .build();

            given(budgetService.createBudget(any())).willThrow(
                    new BusinessException(reqDto.getMemberId(), "memberId", MEMBER_NOT_FOUND)
            );

            mockMvc.perform(post(BUDGET_URL)
                            .contentType(APPLICATION_JSON).content(mapper.writeValueAsString(reqDto)))
                    .andExpect(status().is4xxClientError());
        }

        @Test
        @DisplayName("존재하지 않는 카테고리로 요청하면 실패한다")
        void 존재하지_않는_카테고리로_요청하면_실패한다() throws Exception {
            BudgetCreateReqDto reqDto = BudgetCreateReqDto.builder()
                    .memberId(budget.getId())
                    .categoryName("Invalid Category")
                    .cost(budget.getCost())
                    .build();

            given(budgetService.createBudget(any())).willThrow(
                    new BusinessException(reqDto.getCategoryName(), "categoryName", BUDGET_CATEGORY_NOT_FOUND)
            );

            mockMvc.perform(post(BUDGET_URL)
                            .contentType(APPLICATION_JSON).content(mapper.writeValueAsString(reqDto)))
                    .andExpect(status().isBadRequest());
        }
    }
}
