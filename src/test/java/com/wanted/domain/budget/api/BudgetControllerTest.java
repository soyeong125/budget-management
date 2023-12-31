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
import com.wanted.global.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

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

        @Test
        @DisplayName("카테고리를 입력하지 않으면 실패한다")
        void 카테고리를_입력하지_않으면_실패한다() throws Exception {
            BudgetCreateReqDto reqDto = BudgetCreateReqDto.builder()
                    .memberId(budget.getId())
                    .categoryName("")
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

    @Nested
    @DisplayName("예산 수정 관련 컨트롤러 테스트")
    class updateBudget {

        @Test
        @DisplayName("예산 수정이 정상적으로 성공한다.")
        void 예산_수정이_정상적으로_성공한다() throws Exception {
            int normalCost = 10000; // 정상 값

            BudgetCreateReqDto reqDto = BudgetCreateReqDto.builder()
                    .memberId(budget.getId())
                    .categoryName("")
                    .cost(normalCost)
                    .build();

            given(budgetService.updateDeposit(any(), any())).willReturn(1L);

            mockMvc.perform(put(BUDGET_URL + "/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(reqDto)))
                    .andExpect(status().isCreated());
        }


        @Test
        @DisplayName("카테고리가 없으면, 예산 수정에 실패한다.")
        void 카테고리가_없으면_예산_수정에_실패한다() throws Exception {
            int normalCost = 10000; // 정상 값

            BudgetCreateReqDto reqDto = BudgetCreateReqDto.builder()
                    .memberId(budget.getId())
                    .categoryName("")
                    .cost(normalCost)
                    .build();

            given(budgetService.updateDeposit(any(), any())).willThrow(
                    new BusinessException(55L, "categoryId", BUDGET_CATEGORY_NOT_FOUND)
            );

            mockMvc.perform(put(BUDGET_URL + "/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(reqDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("예산 삭제 관련 컨트롤러 테스트")
    class deleteBudget {

        @Test
        @DisplayName("정상적으로 예산 삭제에 성공한다.")
        void 정상적으로_예산_삭제에_성공한다_204() throws Exception {
            mockMvc.perform(delete(BUDGET_URL + "/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("회원이 작성하지 않은 예산을 수정하면, 실패한다.")
        void 회원이_작성하지_않은_예산을_수정하면_실패한다_403() throws Exception {

            doThrow(new BusinessException(43, "budgetId", ErrorCode.ACCESS_DENIED_EXCEPTION))
                    .when(budgetService)
                    .deleteBudget(any());

            mockMvc.perform(delete(BUDGET_URL + "/1"))
                    .andExpect(status().isForbidden());
        }
    }
}
