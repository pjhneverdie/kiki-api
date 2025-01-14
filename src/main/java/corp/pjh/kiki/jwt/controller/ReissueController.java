package corp.pjh.kiki.jwt.controller;

import corp.pjh.kiki.common.dto.CustomException;
import corp.pjh.kiki.common.dto.ApiResponse;
import corp.pjh.kiki.jwt.dto.Tokens;
import corp.pjh.kiki.jwt.service.JwtService;
import corp.pjh.kiki.jwt.exception.JwtExceptionCode;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final JwtService jwtService;

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<String>> reissue(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            Tokens tokens = jwtService.recreateTokens(authorization.substring(7));

            jwtService.saveRefreshToken(tokens.getRefreshToken());
            try {
                return ResponseEntity.ok(new ApiResponse<>(jwtService.encrypt(tokens)));
            } catch (Exception e) {
                throw new CustomException(JwtExceptionCode.TOKEN_ENCODE_FAILED);
            }
        }

        throw new CustomException(JwtExceptionCode.INVALID_TOKEN);
    }

}
