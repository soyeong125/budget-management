package com.wanted.domain.expenditure.application;

import com.wanted.domain.category.cost.constants.CategoryName;
import com.wanted.domain.category.cost.dao.CostCategoryRepository;
import com.wanted.domain.category.cost.entity.CostCategory;
import com.wanted.domain.expenditure.dao.ExpenditureRepository;
import com.wanted.domain.expenditure.dto.request.ExpenditureCreateReqDto;
import com.wanted.domain.expenditure.entity.Expenditure;
import com.wanted.domain.member.dao.MemberRepository;
import com.wanted.domain.member.entity.Member;
import com.wanted.global.error.BusinessException;
import com.wanted.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wanted.global.error.ErrorCode.BUDGET_CATEGORY_NOT_FOUND;
import static com.wanted.global.error.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenditureService {

    private final MemberRepository memberRepository;
    private final ExpenditureRepository expenditureRepository;
    private final CostCategoryRepository categoryRepository;

    /**
     * 지출 작성
     *
     * @param reqDto 지출 작성에 필요한 데이터 정보
     * @return 작성된 지출의 id
     */
    @Transactional
    public Long createExpenditure(ExpenditureCreateReqDto reqDto) {

        Member member = memberRepository.findById(reqDto.getMemberId()).orElseThrow(
                () -> new BusinessException(reqDto.getMemberId(), "memberId", MEMBER_NOT_FOUND)
        );

        String categoryName = CategoryName.valueOf(reqDto.getCategoryName()).toString();
        CostCategory costCategory = categoryRepository.findByName(categoryName).orElseThrow(
                () -> new BusinessException(reqDto.getCategoryName(), "categoryName", BUDGET_CATEGORY_NOT_FOUND)
        );

        Expenditure expenditure = Expenditure.builder()
                .member(member)
                .category(costCategory)
                .cost(reqDto.getAmount())
                .memo(reqDto.getMemo())
                .isExcluded(reqDto.getIsExcluded())
                .build();
        Expenditure savedExpenditure = expenditureRepository.save(expenditure);

        return savedExpenditure.getId();
    }


    /**
     * Id로 비용 카테고리 찾기
     *
     * @param categoryId 카테고리 id
     * @return 찾은 카테고리
     */
    private CostCategory getCategoryById(Long categoryId) {
        CostCategory category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new BusinessException(categoryId, "categoryId", BUDGET_CATEGORY_NOT_FOUND)
        );

        return category;
    }
}
