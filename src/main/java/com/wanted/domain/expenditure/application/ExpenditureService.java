package com.wanted.domain.expenditure.application;

import com.wanted.domain.category.cost.constants.CategoryName;
import com.wanted.domain.category.cost.dao.CostCategoryRepository;
import com.wanted.domain.category.cost.entity.CostCategory;
import com.wanted.domain.expenditure.dao.ExpenditureRepository;
import com.wanted.domain.expenditure.dto.request.ExpenditureCreateReqDto;
import com.wanted.domain.expenditure.dto.request.ExpenditureUpdateReqDto;
import com.wanted.domain.expenditure.dto.response.ExpenditureDetailResponse;
import com.wanted.domain.expenditure.dto.response.ExpenditureRecommendResDto;
import com.wanted.domain.expenditure.entity.Expenditure;
import com.wanted.domain.member.dao.MemberRepository;
import com.wanted.domain.member.entity.Member;
import com.wanted.global.error.BusinessException;
import com.wanted.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

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

}
