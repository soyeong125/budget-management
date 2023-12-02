package com.wanted.domain.expenditure.dto.response;

import com.wanted.domain.category.cost.constants.CategoryName;
import com.wanted.domain.category.cost.entity.CostCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class ExpenditureTodayResDto {

    // 오늘 지출 총 금액
    private Integer totalAmount;

    // 카테고리 별 지출 금액
    private Map<CategoryName, Integer> amountPerCategory;

    // 총 지출 적절 금액
    private Integer properTotalAmount;

    // 카테고리 별 적절 금액
    private Map<CategoryName, Integer> properAmountPerCategory;

    // 카테고리 별 적정 금액, 지출금액의 차이인 위험도
    private Map<CategoryName, Integer> dangerRates;

    @Builder
    private ExpenditureTodayResDto(Integer totalAmount, Map<CategoryName, Integer> amountPerCategory,
                                     Integer properTotalAmount, Map<CategoryName, Integer> properAmountPerCategory,
                                     Map<CategoryName, Integer> dangerRates) {
        this.totalAmount = totalAmount;
        this.amountPerCategory = amountPerCategory;
        this.properTotalAmount = properTotalAmount;
        this.properAmountPerCategory = properAmountPerCategory;
        this.dangerRates = dangerRates;
    }
}
