package corp.pjh.kiki.jwt.controller;

import corp.pjh.kiki.common.dto.ApiResponse;
import corp.pjh.kiki.jwt.dto.Tokens;
import corp.pjh.kiki.for_test.ControllerTestBase;
import corp.pjh.kiki.jwt.service.JwtService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReissueController.class)
class ReissueControllerTest extends ControllerTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @Test
    public void 토큰_재발급_테스트() throws Exception {
        // Given
        Tokens tokens = new Tokens("abcdefg", "hijklmnop");

        when(jwtService.recreateTokens(any(String.class))).thenReturn(tokens);
        doNothing().when(jwtService).saveRefreshToken(any(String.class));
        when(jwtService.encrypt(any(Tokens.class))).thenReturn("encrypted");

        // When
        ResultActions resultActions = mockMvc.perform(post("/reissue")
                .header("Authorization", "Bearer qrstuvwxyz"));

        // Then
        ApiResponse<String> apiResponse = createApiResponse("encrypted");

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(apiResponse)));
    }

}