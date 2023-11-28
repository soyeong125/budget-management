package com.wanted.domain.budget.application;

import com.wanted.domain.budget.dao.BudgetRepository;
import com.wanted.domain.budget.dto.request.BudgetCreateReqDto;
import com.wanted.domain.budget.entity.Budget;
import com.wanted.domain.category.cost.dao.CostCategoryRepository;
import com.wanted.domain.category.cost.entity.CostCategory;
import com.wanted.domain.member.dao.MemberRepository;
import com.wanted.domain.member.entity.Member;
import com.wanted.global.error.BusinessException;
import com.wanted.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final MemberRepository memberRepository;
    private final CostCategoryRepository costCategoryRepository;

    /**
     * 예산 설정
     *
     * @param reqDto 예산 생성 요청 dto (유저 Id, 카테고리명, 금액)
     * @return 생성된 예산 Id
     */
    public Long createBudget(BudgetCreateReqDto reqDto) {
        // 요청 dto로부터 사용자를 조회한다.
        Member member = memberRepository.findById(reqDto.getMemberId()).orElseThrow(
                () -> new BusinessException(reqDto.getMemberId(), "memberId", ErrorCode.MEMBER_NOT_FOUND)
        );

        // 요청 dto로부터 예산 카테고리를 조회한다.
        CostCategory category = costCategoryRepository.findByName(reqDto.getCategoryName()).orElseThrow(
                () -> new BusinessException(reqDto.getCategoryName(), "categoryName", ErrorCode.BUDGET_CATEGORY_NOT_FOUND)
        );

        Budget budget = Budget.builder()
                .member(member)
                .category(category)
                .cost(reqDto.getCost())
                .build();

        Budget savedBudget = budgetRepository.save(budget);

        return savedBudget.getId();
    }
}
