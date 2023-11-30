package com.wanted.domain.budget.application;

import com.wanted.domain.budget.dao.BudgetRepository;
import com.wanted.domain.budget.dto.request.BudgetCreateReqDto;
import com.wanted.domain.budget.dto.request.BudgetRecommendReqDto;
import com.wanted.domain.budget.dto.response.BudgetRecommendResDto;
import com.wanted.domain.budget.entity.Budget;
import com.wanted.domain.category.cost.constants.CategoryName;
import com.wanted.domain.category.cost.dao.CostCategoryRepository;
import com.wanted.domain.category.cost.entity.CostCategory;
import com.wanted.domain.member.dao.MemberRepository;
import com.wanted.domain.member.entity.Member;
import com.wanted.global.error.BusinessException;
import com.wanted.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.wanted.global.error.ErrorCode.MEMBER_NOT_FOUND;

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
        Member member = findMemberById(reqDto.getMemberId());

        // 요청 dto로부터 예산 카테고리를 조회한다.
        CostCategory category = findBudgetByName(reqDto.getCategoryName());

        Budget budget = Budget.builder()
                .member(member)
                .category(category)
                .cost(reqDto.getCost())
                .build();

        Budget savedBudget = budgetRepository.save(budget);

        return savedBudget.getId();
    }

    /**
     * 예산 설계 추천
     * 예산 생성 기준은 기존 이용중인 사용자들이 설정한 평균 값.
     *
     * @param reqDto 예산 추천 요청 dto
     * @return 예산 설계 추천 결과
     */
    public BudgetRecommendResDto createBudgetRecommendation(BudgetRecommendReqDto reqDto) {

        // 요청 dto로부터 사용자를 조회한다.
        Member member = findMemberById(reqDto.getMemberId());

        // 기존 이용중인 사용자들이 설정한 예산 비율의 평균을 계산
        Map<CategoryName, Double> averageBudgetRates = calculateAverageBudgetRates();

        // 예산 총액을 카테고리 별 비율로 나눈다.
        Map<CategoryName, Integer> budgetRates = new HashMap<>();
        for (CategoryName categoryName : CategoryName.values()) {
            double rate = averageBudgetRates.getOrDefault(categoryName, 0.0);
            if (rate < 0.1) {
                rate = 0; // 10% 미만인 경우, ETC 카테고리로 처리
            }
            int budgetAmount = (int) (reqDto.getTotalBudgetAmount() * rate);
            budgetAmount = budgetAmount / 100 * 100; // 100원 단위로 반올림한다.
            budgetRates.put(categoryName, budgetAmount);
        }

        return new BudgetRecommendResDto(budgetRates);
    }

    //TODO
    /**
     * 기존 사용자들이 설정한 예산 비율의 평균 계산
     *
     * @return 카테고리별 예산 비율
     */
    private Map<CategoryName, Double> calculateAverageBudgetRates() {
        // 데이터베이스에서 사용자별 카테고리 예산 데이터를 가져와서 평균을 계산하는 로직
        Map<CategoryName, Double> averageRates = new HashMap<>();
        // 데이터베이스 조회 및 평균 계산 로직 구현...
        return averageRates;
    }

    /**
     * 예산 수정
     *
     * @param budgetId 수정할 예산 아이디
     * @param reqDto    수정 데이터 정보
     * @return 수정된 예산 아이디
     */
    @Transactional
    public Long updateDeposit(Long budgetId, BudgetCreateReqDto reqDto) {

        // 요청 dto로부터 사용자를 조회한다.
        Member member = findMemberById(reqDto.getMemberId());

        // 권한 확인
        validUserAccessDeposit(member, budgetId);

        // 해당 예산 찾기
        Budget budget = getBudgetById(budgetId);

        // 해당 카테고리 찾기
        CostCategory foundCategory =  findBudgetByName(reqDto.getCategoryName());

        // 수정
        budget.update(reqDto, foundCategory);

        return budget.getId();
    }

    /**
     * 회원이 해당 예산을 바꿀 수 있는 권한이 있는지 확인. 없으면 예외
     *
     * @param member    회원
     * @param budgetId  예산 id
     */
    private void validUserAccessDeposit(Member member, Long budgetId) {
        boolean isBudgetMatchMember = member.getBudgets().stream()
                .anyMatch(deposit -> deposit.getId().equals(budgetId));

        if (!isBudgetMatchMember) {
            throw new BusinessException(budgetId, "budgetId", ErrorCode.ACCESS_DENIED_EXCEPTION);
        }
    }

    /**
     * 아이디로 예산 찾기. 없으면 예외
     *
     * @param budgetId 찾을 id
     * @return 찾은 예산
     */
    private Budget getBudgetById(Long budgetId) {
        return budgetRepository.findById(budgetId).orElseThrow(
                () -> new BusinessException(budgetId, "depositId", ErrorCode.BUDGET_NOT_FOUND)
        );
    }

    /**
     * 예산 삭제
     *
     * @param budgetId 삭제할 예산
     */
    @Transactional
    public void deleteBudget(Long budgetId) {
        // 해당 예산 찾기
        Budget budget = getBudgetById(budgetId);
        budgetRepository.delete(budget);
    }

    /**
     * Id로 카테고리 찾기
     *
     * @param budgetName 찾을 예산명
     * @return 카테고리명
     */
    private CostCategory findBudgetByName(String budgetName) {
        return costCategoryRepository.findByName(budgetName).orElseThrow(
                () -> new BusinessException(budgetName, "categoryName", ErrorCode.BUDGET_CATEGORY_NOT_FOUND)
        );
    }


    /**
     * Id로 사용자 조회
     *
     * @param id
     * @return 사용자
     */
    public Member findMemberById(Long id){
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new BusinessException(id, "memberId", MEMBER_NOT_FOUND)
        );

        return member;
    }

}
