package com.wanted.domain.expenditure.application;

import com.wanted.domain.budget.dao.BudgetRepository;
import com.wanted.domain.budget.entity.Budget;
import com.wanted.domain.category.cost.constants.CategoryName;
import com.wanted.domain.category.cost.dao.CostCategoryRepository;
import com.wanted.domain.category.cost.entity.CostCategory;
import com.wanted.domain.expenditure.dao.ExpenditureRepository;
import com.wanted.domain.expenditure.dto.request.ExpenditureCreateReqDto;
import com.wanted.domain.expenditure.dto.request.ExpenditureUpdateReqDto;
import com.wanted.domain.expenditure.dto.response.ExpenditureDetailResponse;
import com.wanted.domain.expenditure.dto.response.ExpenditureRecommendResDto;
import com.wanted.domain.expenditure.dto.response.ExpenditureTodayResDto;
import com.wanted.domain.expenditure.entity.Expenditure;
import com.wanted.domain.member.dao.MemberRepository;
import com.wanted.domain.member.entity.Member;
import com.wanted.global.error.BusinessException;
import com.wanted.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wanted.global.error.ErrorCode.BUDGET_CATEGORY_NOT_FOUND;
import static com.wanted.global.error.ErrorCode.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenditureService {

    // 예산을 초과했더라도 추천받는 최소 금액
    private static final int MINIMUM_EXPENDITURE_AMOUNT = 5000;

    private final MemberRepository memberRepository;
    private final ExpenditureRepository expenditureRepository;
    private final CostCategoryRepository categoryRepository;
    private final BudgetRepository budgetRepository;

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
     * 지출을 수정한다.
     *
     * @param expenditureId 수정할 지출의 ID
     * @param reqDto 지출 수정 요청 파라미터
     * @return 수정된 지출의 ID
     */
    public Long updateExpenditure(Long expenditureId, ExpenditureUpdateReqDto reqDto) {

        Expenditure expenditure = expenditureRepository.findById(expenditureId).orElseThrow(
                () -> new BusinessException(expenditureId, "expenditureId", ErrorCode.EXPENDITURE_NOT_FOUND)
        );

        expenditure.update(reqDto.toEntity());

        return expenditure.getId();
    }

    /**
     * 지출을 상세 조회한다.
     *
     * @param expenditureId 조회할 지출의 ID
     * @return 지출 상세 조회값
     */
    public ExpenditureDetailResponse getExpenditure(Long expenditureId) {
        Expenditure expenditure = expenditureRepository.findById(expenditureId).orElseThrow(
                () -> new BusinessException(expenditureId, "expenditureId", ErrorCode.EXPENDITURE_NOT_FOUND)
        );

        return new ExpenditureDetailResponse(expenditure);
    }

    /**
     * 지출을 삭제한다.
     *
     * @param expenditureId 삭제할 지출의 ID
     * @return 삭제된 지출의 ID
     */
    public Long deleteExpenditure(Long expenditureId) {

        Expenditure expenditure = expenditureRepository.findById(expenditureId).orElseThrow(
                () -> new BusinessException(expenditureId, "expenditureId", ErrorCode.EXPENDITURE_NOT_FOUND)
        );

        expenditureRepository.delete(expenditure);

        return expenditureId;
    }

    /**
     * 오늘 사용 가능한 지출 추천 금액을 반환한다.
     *
     * @param account 사용자 계정
     * @return 추천 지출 금액
     */
    public ExpenditureRecommendResDto createExpenditureRecommendation(String account) {

        // 요청한 사용자를 조회한다.
        Member member = memberRepository.findByAccount(account).orElseThrow(
                () -> new BusinessException(account, "account", MEMBER_NOT_FOUND)
        );

        // 현재 남아있는 예산을 이번 달의 남은 일수로 나누어 오늘 지출 가능한 총액을 구한다.
        Integer todayBudget = calculateTodayBudget(member);

        // 지출 추천 메시지를 생성한다.
        String expenditureMessage = createExpenditureMessage(todayBudget);

        return ExpenditureRecommendResDto.builder()
                .totalAmount(todayBudget)
                .expenditureMessage(expenditureMessage)
                .build();
    }

    /**
     * TODO
     * 남은 예산을 이번 달의 남은 일수로 나누어 오늘 사용 가능한 지출 총액을 계산한다.
     *
     * @param member 사용자
     * @return 오늘 사용 가능한 지출 총액
     */
    private Integer calculateTodayBudget(Member member) {
        //사용자의 전체 예산 구하기
        //이번달 사용한 금액 계산
        //예산 - 사용한 금액 계산
        //양수면, 남은금액 / 남은 일수 -> 사용가능한 지출 총액 계산

        return 0;
    }

    /**
     * TODO
     * 지출 추천 메시지를 생성한다.
     *
     * @param remaining 남은 금액
     * @return 지출 추천 메시지
     */
    private String createExpenditureMessage(Integer remaining) {
        //남은 금액이 1.0 이상이면 훌륭함
        //남은 금액이 1.0 이면 잘하고 있음
        //남은 금액이 1.0 이하면 나쁘지 않은 편입니다.
        //남은 금액이 음수이면, 초과했습니다.

        return "";
    }

    /**
     * 오늘 지출 정보를 생성한다.
     *
     * @param account 사용자 계정명
     * @return 오늘 지출 정보
     */
    public ExpenditureTodayResDto createExpenditureToday(String account) {

        // 요청한 사용자를 조회한다.
        Member member = memberRepository.findByAccount(account).orElseThrow(
                () -> new BusinessException(account, "account", MEMBER_NOT_FOUND)
        );

        // 사용자의 지출 목록을 조회한다.
        List<Expenditure> expenditures = expenditureRepository.findAllByMemberId(member.getId());

        // 지출 총 합계를 계산한다.
        Integer totalAmount = calculateTotalAmount(expenditures);

        // 카테고리별 지출 합계를 계산한다.
        Map<CategoryName, Integer> amountPerCategory = calculateAmountPerCategory(expenditures);

        // 사용자의 카테고리별 예산 비율을 계산한다.
        Map<CategoryName, Double> categoryRates = calculateCategoryRates(member);

        // 현재 남아있는 예산을 이번 달의 남은 일수로 나누어 적절 지출 가능한 총액을 구한다.
        Integer properTotalAmount = calculateTodayBudget(member);

        // 적절 지출 총액을 예산 비율만큼 나누어 카테고리 별 적절 지출 금액을 만든다.
        Map<CategoryName, Integer> properAmountPerCategory = calculateAmountPerCategoryByRates(categoryRates,
                properTotalAmount);

        // 위험도를 계산한다.
        Map<CategoryName, Integer> dangerRates = calculateDangerRates(amountPerCategory, properAmountPerCategory);

        return ExpenditureTodayResDto.builder()
                .totalAmount(totalAmount)
                .amountPerCategory(amountPerCategory)
                .properTotalAmount(properTotalAmount)
                .properAmountPerCategory(properAmountPerCategory)
                .dangerRates(dangerRates)
                .build();
    }

    /**
     * 지출 총 합계를 계산한다.
     *
     * @param expenditures 지출 목록
     * @return 지출 총 합계
     */
    private Integer calculateTotalAmount(List<Expenditure> expenditures) {
        return expenditures.stream()
                .mapToInt(Expenditure::getCost)
                .sum();
    }

    /**
     * 카테고리별 지출 합계를 계산한다.
     *
     * @param expenditures 지출 목록
     * @return 카테고리별 지출 합계
     */
    private Map<CategoryName, Integer> calculateAmountPerCategory(List<Expenditure> expenditures) {
        return expenditures.stream()
                .collect(Collectors.groupingBy(
                        expenditure -> expenditure.getCategory().getName(),
                        Collectors.summingInt(Expenditure::getCost)
                ));
    }

    /**
     * 사용자의 카테고리별 예산 비율을 계산한다.
     *
     * @param member 사용자
     * @return 카테고리별 예산 비율
     */
    private Map<CategoryName, Double> calculateCategoryRates(Member member) {
        //TODO
        //사용자의 카테고리별 예산 비율 불러오기
        int totalBudgetAmount = 0; //사용자의 전체 예산 불러오기
        List<Budget> budgets = budgetRepository.findAllByMemberId(member.getId());

        return budgets.stream()
                .collect(Collectors.toMap(
                        budget -> budget.getCategory().getName(),
                        budget -> (double) budget.getCost() / totalBudgetAmount
                ));
    }

    /**
     * TODO
     * 카테고리별 예산 비율을 기준으로 예산 총액을 나누어 카테고리별 지출액을 계산한다.
     *
     * @param categoryRates 카테고리별 예산 비율
     * @param budget 예산 총액
     * @return 카테고리별 지출액
     */
    private Map<CategoryName, Integer> calculateAmountPerCategoryByRates(Map<CategoryName, Double> categoryRates,
                                                                         int budget) {
        return new HashMap<>();
    }

    /**
     * 카테고리별 적정 금액, 지출금액의 차이인 위험도를 계산한다.
     *
     * @param amountPerCategory 카테고리별 지출 금액
     * @param properAmountPerCategory 카테고리별 적정 금액
     * @return 카테고리별 위험도 (퍼센티지)
     */
    private Map<CategoryName, Integer> calculateDangerRates(Map<CategoryName, Integer> amountPerCategory,
                                                            Map<CategoryName, Integer> properAmountPerCategory) {
        return amountPerCategory.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (int) ((double) entry.getValue() / properAmountPerCategory.get(entry.getKey())) * 100
                ));
    }
}
