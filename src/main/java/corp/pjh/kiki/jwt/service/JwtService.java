package corp.pjh.kiki.jwt.service;

import corp.pjh.kiki.common.dto.CustomException;

import corp.pjh.kiki.jwt.dto.Tokens;
import corp.pjh.kiki.jwt.exception.JwtExceptionCode;

import corp.pjh.kiki.jwt.util.AESUtil;
import corp.pjh.kiki.jwt.util.JwtUtil;

import corp.pjh.kiki.security.oauth2.dto.MemberPrincipal;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final long threeHoursInMilliSeconds = 60 * 60 * 3L * 1000;

    private final long thirtyDaysInMilliSeconds = 60 * 60 * 24L * 30 * 1000;

    private final JwtUtil jwtUtil;

    private final AESUtil aesUtil;

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * recreateTokens, 토큰 재발급
     */
    public Tokens recreateTokens(String token) {
        try {
            if (!jwtUtil.isExpired(token) && jwtUtil.getTokenType(token).equals("refresh")) {
                String subject = jwtUtil.getSubject(token);

                if (redisTemplate.opsForValue().get(subject) != null && redisTemplate.opsForValue().get(subject).equals(token)) {
                    String name = jwtUtil.getName(token);
                    String role = jwtUtil.getRole(token);

                    String accessToken = jwtUtil.createJwt("access", subject, name, role, threeHoursInMilliSeconds);
                    String refreshToken = jwtUtil.createJwt("refresh", subject, name, role, thirtyDaysInMilliSeconds);

                    return new Tokens(accessToken, refreshToken);
                }
            }

            /**
             * 만료됐거나 refresh 토큰이 아닌 경우
             */
            throw new CustomException(JwtExceptionCode.INVALID_TOKEN);
        } catch (JwtException e) {
            /**
             * 토큰이 위조됐을 경우
             */
            throw new CustomException(JwtExceptionCode.INVALID_TOKEN);
        }
    }

    /**
     * createTokens, 토큰 발급
     */
    public Tokens createTokens(MemberPrincipal principal) {
        String subject = principal.getSubject();
        String name = principal.getName();
        String role = principal.getAuthorities().stream().toList().get(0).getAuthority();

        String accessToken = jwtUtil.createJwt("access", subject, name, role, threeHoursInMilliSeconds);
        String refreshToken = jwtUtil.createJwt("refresh", subject, name, role, thirtyDaysInMilliSeconds);

        return new Tokens(accessToken, refreshToken);
    }

    public void saveRefreshToken(String token) {
        String subject = jwtUtil.getSubject(token);

        redisTemplate.opsForValue().set(subject, token, Duration.ofMillis(thirtyDaysInMilliSeconds));
    }

    public String encrypt(Tokens tokens) throws Exception {
        String toString = tokens.getAccessToken() + " " + tokens.getRefreshToken();
        return aesUtil.encrypt(toString);
    }

}
