package com.wanted.domain.expenditure.dto.request;

import com.wanted.domain.category.cost.entity.CostCategory;
import com.wanted.domain.expenditure.entity.Expenditure;
import com.wanted.domain.member.entity.Member;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class ExpenditureCreateReqDto {
    @NotNull(message = "사용자 ID를 입력해주세요")
    private Long memberId;

    @NotEmpty(message = "카테고리 이름을 입력해주세요")
    private String categoryName;

    @PositiveOrZero(message = "지출 금액은 0 이상이어야 합니다")
    private Integer amount;

    @NotEmpty(message = "메모를 입력해주세요")
    private String memo;

    @NotNull(message = "합계 제외 여부를 입력해주세요")
    private Boolean isExcluded;

    @Builder
    private ExpenditureCreateReqDto(Long memberId, String categoryName, Integer amount, String memo,
                                          Boolean isExcluded) {
        this.memberId = memberId;
        this.categoryName = categoryName;
        this.amount = amount;
        this.memo = memo;
        this.isExcluded = isExcluded;
    }
}

