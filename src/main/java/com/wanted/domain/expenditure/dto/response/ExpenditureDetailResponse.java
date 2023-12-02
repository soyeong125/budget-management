package com.wanted.domain.expenditure.dto.response;

import java.time.LocalDateTime;


import com.wanted.domain.expenditure.entity.Expenditure;
import lombok.Getter;

@Getter
public class ExpenditureDetailResponse {

    private final Long memberId;
    private final Long budgetCategoryId;
    private final Integer amount;
    private final String memo;
    private final Boolean isExcluded;
    private final LocalDateTime createdTime;

    public ExpenditureDetailResponse(Expenditure expenditure) {
        this.memberId = expenditure.getMember().getId();
        this.budgetCategoryId = expenditure.getCategory().getId();
        this.amount = expenditure.getCost();
        this.memo = expenditure.getMemo();
        this.isExcluded = expenditure.getIsExcluded();
        this.createdTime = expenditure.getTime();
    }
}
