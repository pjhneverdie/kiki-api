package corp.pjh.kiki.member.controller;

import corp.pjh.kiki.for_test.ControllerTestBase;
import corp.pjh.kiki.for_test.WithTestUser;
import corp.pjh.kiki.common.dto.ApiResponse;
import corp.pjh.kiki.member.domain.Role;
import corp.pjh.kiki.member.dto.MemberResponse;
import corp.pjh.kiki.member.service.MemberService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends ControllerTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    @WithTestUser
    public void 회원_정보_조회_테스트() throws Exception {
        // Given
        MemberResponse memberResponse = new MemberResponse("pjh", "thumbUrl", Role.FREE);

        when(memberService.getMemberResponse(any(String.class))).thenReturn(memberResponse);

        // When
        ResultActions resultActions = mockMvc.perform(get("/member/me"));

        // Then
        ApiResponse<MemberResponse> apiResponse = createApiResponse(memberResponse);

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(apiResponse)));
    }

    @Test
    @WithTestUser
    public void 회원_탈퇴_테스트() throws Exception {
        // Given
        doNothing().when(memberService).deleteMember(any(String.class));

        // When
        ResultActions resultActions = mockMvc.perform(delete("/member"));

        // Then
        ApiResponse<Void> apiResponse = createApiResponse(null);

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(apiResponse)));
    }

}