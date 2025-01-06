package corp.pjh.kiki.common.web.controller;

import corp.pjh.kiki.common.dto.CustomException;
import corp.pjh.kiki.common.dto.ExceptionCode;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Base64;

@Profile("!testcase")
@Controller
@RequiredArgsConstructor
public class KakaopayRedirectController {
    @Value("${kakaopay-redirect-base-url}")
    private String baseUrl;

    @GetMapping("/pay")
    public void kakaopayRedirect(@RequestParam String mobileWebUrl, HttpServletResponse response) {
        try {
            String url = new String(Base64.getUrlDecoder().decode(mobileWebUrl));

            if (url.startsWith(baseUrl)) {
                response.sendRedirect(url);
                return;
            }

            response.sendRedirect("/expired");
        } catch (Exception e) {
            try {
                response.sendRedirect("/expired");
            } catch (Exception e2) {
                throw new CustomException(new ExceptionCode() {

                    @Override
                    public String codeName() {
                        return "REDIRECT_FAILED";
                    }

                    @Override
                    public HttpStatusCode httpStatusCode() {
                        return HttpStatusCode.valueOf(500);
                    }

                });
            }
        }
    }
}
