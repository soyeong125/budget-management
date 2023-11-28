package com.wanted.domain.category.cost.api;

import com.wanted.domain.category.cost.constants.CategoryName;
import com.wanted.global.format.response.ResponseApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cost-categories")
public class CostCategoryController {
    /**
     * 모든 비용 카테고리 목록을 조회한다.
     *
     * @return 전체 카테고리 목록
     */
    @GetMapping
    public ResponseEntity<ResponseApi> getCostCategories() {
        List<CategoryName> categoryNames = List.of(CategoryName.values());
        return ResponseEntity.ok(ResponseApi.toSuccessForm(categoryNames));
    }
}
