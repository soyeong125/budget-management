package com.wanted.domain.expenditure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.budgetManagement.config.restdocs.AbstractRestDocsTests;
import com.wanted.domain.expenditure.ExpenditureTestHelper;
import com.wanted.domain.expenditure.application.ExpenditureService;
import com.wanted.domain.expenditure.dto.request.ExpenditureCreateReqDto;
import com.wanted.domain.expenditure.dto.request.ExpenditureUpdateReqDto;
import com.wanted.domain.expenditure.dto.response.ExpenditureDetailResponse;
import com.wanted.domain.expenditure.entity.Expenditure;
import com.wanted.domain.member.MemberTestHelper;
import com.wanted.domain.member.entity.Member;
import com.wanted.global.error.BusinessException;
import com.wanted.global.error.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExpenditureController.class)
class ExpenditureControllerTest extends AbstractRestDocsTests {

    private final static String EXPENDITURE_URL = "/api/v1/expenditure";

    @MockBean
    private ExpenditureService expenditureService;

    @Autowired
    ObjectMapper mapper;

    private Member member;
    private Expenditure expenditure;
    @BeforeEach
    void setUp(){

        member = MemberTestHelper.createMememberWithId();
        expenditure = ExpenditureTestHelper.createExpenditure();
    }

    @Nested
    @DisplayName("지출 생성 관련 컨트롤러 테스트")
    class writeExpenditure {

        @Test
        @DisplayName("지출 생성에 성공한다.")
        void 지출_생성에_성공한다() throws Exception {
            ExpenditureCreateReqDto reqDto = ExpenditureCreateReqDto.builder()
                    .memberId(expenditure.getMember().getId())
                    .categoryName(expenditure.getCategory().getName().name())
                    .amount(expenditure.getCost())
                    .memo(expenditure.getMemo())
                    .isExcluded(expenditure.getIsExcluded())
                    .build();

            given(expenditureService.createExpenditure(any())).willReturn(1L);


            mockMvc.perform(post(EXPENDITURE_URL)
                            .contentType(APPLICATION_JSON).content(mapper.writeValueAsString(reqDto)))
                    .andExpect(status().isCreated());
        }


        @Test
        @DisplayName("카테고리가 없으면, 지출 생성에 실패한다.")
        void 카테고리가_없으면_지출_작성에_실패한다() throws Exception {
            ExpenditureCreateReqDto reqDto = ExpenditureCreateReqDto.builder()
                    .memberId(expenditure.getMember().getId())
                    .categoryName("none")
                    .amount(expenditure.getCost())
                    .memo(expenditure.getMemo())
                    .isExcluded(expenditure.getIsExcluded())
                    .build();

            given(expenditureService.createExpenditure(any())).willThrow(
                    new BusinessException(55L, "categoryId", ErrorCode.BUDGET_CATEGORY_NOT_FOUND)
            );

            mockMvc.perform(post(EXPENDITURE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(reqDto)))
                    .andExpect(status().is4xxClientError());
        }
    }

    @Nested
    @DisplayName("지출 수정 관련 컨트롤러 테스트")
    class updateExpenditure {
        @Test
        @DisplayName("지출 수정에 성공한다.")
        void 지출_수정에_성공_한다() throws Exception {
            ExpenditureUpdateReqDto reqDto = ExpenditureUpdateReqDto.builder()
                    .memberId(expenditure.getMember().getId())
                    .amount(expenditure.getCost())
                    .memo(expenditure.getMemo())
                    .isExcluded(expenditure.getIsExcluded())
                    .build();

            given(expenditureService.updateExpenditure(any(), any())).willReturn(expenditure.getId());

            mockMvc.perform(put(EXPENDITURE_URL + "/" + expenditure.getId())
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(reqDto))
                    )
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("지출 수정에 실패한다.")
        void 지출_수정에_실패_한다() throws Exception {
            Integer wrongAmount = -1000;
            ExpenditureUpdateReqDto reqDto = ExpenditureUpdateReqDto.builder()
                    .memberId(expenditure.getMember().getId())
                    .amount(wrongAmount)
                    .memo(expenditure.getMemo())
                    .isExcluded(expenditure.getIsExcluded())
                    .build();

            mockMvc.perform(put(EXPENDITURE_URL + "/" + expenditure.getId())
                            .contentType(APPLICATION_JSON)
                            .content(mapper.writeValueAsString(reqDto))
                    )
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("지출 상세 조회 관련 컨트롤러 테스트")
    class getExpenditure {
        @Test
        @DisplayName("지출 상세 조회에 성공한다,")
        void 지출_상세_조회에_성공한다() throws Exception {
            ExpenditureDetailResponse expenditureDetail = new ExpenditureDetailResponse(expenditure);
            given(expenditureService.getExpenditure(any())).willReturn(expenditureDetail);

            mockMvc.perform(get(EXPENDITURE_URL + "/" + expenditure.getId())
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("존재하지 않는 지출을 조회하면 실패한다")
        void 존재하지_않는_지출을_조회하면_실패한다() throws Exception {
            int wrongExpenditureId = 100;
            given(expenditureService.getExpenditure(any())).willThrow(new BusinessException(wrongExpenditureId, "expenditureId", ErrorCode.EXPENDITURE_NOT_FOUND));

            mockMvc.perform(get(EXPENDITURE_URL + "/" + wrongExpenditureId)
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("지출 삭제관련 컨트롤러 테스트")
    class deleteExpenditure {
        @Test
        @DisplayName("지출 삭제에 성공한다.")
        void 지출_삭제에_성공한다() throws Exception {
            given(expenditureService.getExpenditure(any())).willReturn(new ExpenditureDetailResponse(expenditure));
            given(expenditureService.deleteExpenditure(any())).willReturn(expenditure.getId());

            mockMvc.perform(delete(EXPENDITURE_URL + "/" + expenditure.getId())
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("존재하지 않는 지출을 삭제하면 실패한다")
        void 존재하지_않는_지출을_삭제하면_실패한다() throws Exception {
            int wrongExpenditureId = 100;
            given(expenditureService.getExpenditure(any())).willReturn(new ExpenditureDetailResponse(expenditure));
            given(expenditureService.deleteExpenditure(any())).willThrow(new BusinessException(wrongExpenditureId, "expenditureId", ErrorCode.EXPENDITURE_NOT_FOUND));

            mockMvc.perform(delete(EXPENDITURE_URL + "/" + wrongExpenditureId)
                            .contentType(APPLICATION_JSON)
                    )
                    .andExpect(status().is4xxClientError());
        }
    }
}