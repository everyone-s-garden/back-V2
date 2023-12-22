package com.garden.back.docs.report;

import com.garden.back.docs.RestDocsSupport;
import com.garden.back.report.ReportController;
import com.garden.back.report.request.ReportGardenRequest;
import com.garden.back.report.service.ReportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReportRestDocs extends RestDocsSupport {

    ReportService reportService = mock(ReportService.class);

    @Override
    protected Object initController() {
        return new ReportController(reportService);
    }

    @DisplayName("텃밭에 대해 신고하는 API DOCS")
    @Test
    void reportGarden() throws Exception {
        Long gardenId = 1L;
        ReportGardenRequest request = sut.giveMeBuilder(ReportGardenRequest.class)
            .set("content", "허위로 등록된 텃밭 입니디.")
            .set("reportType", "FAKED_SALE")
                .sample();
        mockMvc.perform(post("/v1/reports/{gardenId}", gardenId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("report-garden",
                pathParameters(
                    parameterWithName("gardenId").description("텃밭의 id")
                ),
                requestFields(
                    fieldWithPath("content").type(STRING).description("신고 내용"),
                    fieldWithPath("reportType").type(STRING).description("신고 타입(FAKED_SALE,SPAMMING, SWEAR_WORD, SENSATIONAL, PERSONAL_INFORMATION_EXPOSURE, COMMENTS 중 하나만 가능)")
                ),
                responseHeaders(
                    headerWithName("Location").description("생성된 신고의 id를 포함한 url")
                )
            ));
    }
}
