package com.wanted.global.util;

import com.wanted.domain.category.cost.constants.CategoryName;
import com.wanted.domain.category.cost.dao.CostCategoryRepository;
import com.wanted.domain.category.cost.entity.CostCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 더미 데이터 초기화용 클래스
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final CostCategoryRepository budgetCategoryRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        initBudgetCategory();
    }

    /**
     * 비용 카테고리 enum 데이터를 db에 삽입한다.
     */
    private void initBudgetCategory() {
        for (CategoryName name : CategoryName.values()) {
            budgetCategoryRepository.save(CostCategory.builder()
                    .name(name)
                    .build());
        }
    }
}
