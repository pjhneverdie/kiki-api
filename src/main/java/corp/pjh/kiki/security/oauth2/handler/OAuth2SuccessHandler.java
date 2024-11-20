package corp.pjh.kiki.security.oauth2.handler;

import corp.pjh.kiki.jwt.dto.Tokens;
import corp.pjh.kiki.jwt.service.JwtService;
import corp.pjh.kiki.security.oauth2.dto.MemberPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${my-domain}")
    private String myDomain;

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        MemberPrincipal principal = (MemberPrincipal) authentication.getPrincipal();

        /**
         * 1. access, refresh 토큰 생성
         */
        Tokens tokens = jwtService.createTokens(principal);

        /**
         * 2. refresh 토큰은 레디스에 저장
         */
        jwtService.saveRefreshToken(tokens.getRefreshToken());

        /**
         * 3. 암호화 후 네이티브 앱으로 딥링킹
         */
        try {
            response.sendRedirect("http://" + myDomain + ":8080/home?tokens=" + jwtService.encrypt(tokens));
        } catch (Exception e) {
            response.sendRedirect("http://" + myDomain + ":8080/home");
        }
    }

}
