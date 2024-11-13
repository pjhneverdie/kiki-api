package corp.pjh.kiki.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import corp.pjh.kiki.common.dto.ApiResponse;
import corp.pjh.kiki.jwt.exception.JwtExceptionCode;
import corp.pjh.kiki.jwt.util.JwtUtil;

import corp.pjh.kiki.member.domain.Role;
import corp.pjh.kiki.security.oauth2.dto.MemberPrincipal;

import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginCheckFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        /**
         * LoginCheckFilter는 로그인 관련 처리만 담당
         */
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);

            try {
                /**
                 * 토큰이 만료됐거나 access 토큰이 아닌 경우
                 */
                if (jwtUtil.isExpired(token) || !jwtUtil.getTokenType(token).equals("access")) {
                    throw401(response, objectMapper);
                    return;
                }

                setSecurityContext(token);
            } catch (JwtException e) {
                throw401(response, objectMapper);
                return;
            }
        }

        /**
         * LoginCheckFilter는 로그인 관련 처리만 담당함
         */
        filterChain.doFilter(request, response);
    }

    private void setSecurityContext(String token) {
        String subject = jwtUtil.getSubject(token);
        String name = jwtUtil.getName(token);
        String role = jwtUtil.getRole(token);

        MemberPrincipal principal = new MemberPrincipal(subject, name, Role.valueOf(role));
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void throw401(HttpServletResponse response, ObjectMapper objectMapper) throws IOException {
        JwtExceptionCode exceptionCode = JwtExceptionCode.INVALID_TOKEN;

        ApiResponse<Void> apiResponse = new ApiResponse<>(null);
        apiResponse.setCodeName(exceptionCode.codeName());

        String stringApiResponse = objectMapper.writeValueAsString(apiResponse);

        response.setContentType("application/json");
        response.getWriter().write(stringApiResponse);
        response.setStatus(exceptionCode.httpStatusCode().value());
    }

}
