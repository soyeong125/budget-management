package com.wanted.domain.expenditure.dto.request;
import com.wanted.domain.expenditure.entity.Expenditure;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 지출 수정 요청 파라미터
 */
@Getter
@NoArgsConstructor
public class ExpenditureUpdateReqDto {
    @NotNull(message = "사용자 ID를 입력해주세요")
    private Long memberId;

    @PositiveOrZero(message = "지출 금액은 0 이상이어야 합니다")
    private Integer amount;

    @NotEmpty(message = "메모를 입력해주세요")
    private String memo;

    @NotNull(message = "합계 제외 여부를 입력해주세요")
    private Boolean isExcluded;

    @Builder
    private ExpenditureUpdateReqDto(Long memberId, Integer amount, String memo, Boolean isExcluded) {
        this.memberId = memberId;
        this.amount = amount;
        this.memo = memo;
        this.isExcluded = isExcluded;
    }

    public Expenditure toEntity() {
        return Expenditure.builder()
                .cost(this.amount)
                .memo(this.memo)
                .isExcluded(this.isExcluded)
                .build();
    }
}
