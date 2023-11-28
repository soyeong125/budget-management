package com.wanted.domain.category.cost.api;

import com.wanted.budgetManagement.config.restdocs.AbstractRestDocsTests;
import com.wanted.domain.category.cost.constants.CategoryName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CostCategoryController.class)
class CostCategoryControllerTest extends AbstractRestDocsTests {

    private static final String COST_CATEGORY_URL = "/api/v1/cost-categories";

    @Nested
    @DisplayName("카테고리 전체 목록 조회")
    class getBudgetCategories {
        @Test
        @DisplayName("카테고리 전체 목록 조회에 성공한다.")
        void 카테고리_전체_목록_조회에_성공한다() throws Exception {
            mockMvc.perform(get(COST_CATEGORY_URL)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(CategoryName.values().length));
        }
    }

}