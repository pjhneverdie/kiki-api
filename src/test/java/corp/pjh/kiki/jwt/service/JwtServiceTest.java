package corp.pjh.kiki.jwt.service;

import corp.pjh.kiki.common.dto.CustomException;
import corp.pjh.kiki.jwt.dto.Tokens;
import corp.pjh.kiki.jwt.exception.JwtExceptionCode;
import corp.pjh.kiki.jwt.util.AESUtil;
import corp.pjh.kiki.jwt.util.JwtUtil;
import corp.pjh.kiki.member.domain.Role;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private final long threeHoursInMilliSeconds = 60 * 60 * 3L * 1000;

    private final long thirtyDaysInMilliSeconds = 60 * 60 * 24L * 30 * 1000;

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AESUtil aesUtil;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Test
    void 토큰_재발급_성공_테스트() {
        // Given
        String refreshToken = "refreshToken";
        String subject = "kakao-3783863255";
        String name = "test";
        String role = Role.FREE.name();
        String newAccessToken = "newAccessToken";
        String newRefreshToken = "newRefreshToken";

        when(jwtUtil.isExpired(refreshToken)).thenReturn(false);
        when(jwtUtil.getTokenType(refreshToken)).thenReturn("refresh");

        when(jwtUtil.getSubject(refreshToken)).thenReturn(subject);
        when(jwtUtil.getName(refreshToken)).thenReturn(name);
        when(jwtUtil.getRole(refreshToken)).thenReturn(role);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(subject)).thenReturn(refreshToken);

        when(jwtUtil.createJwt("access", subject, name, role, threeHoursInMilliSeconds))
                .thenReturn(newAccessToken);
        when(jwtUtil.createJwt("refresh", subject, name, role, thirtyDaysInMilliSeconds))
                .thenReturn(newRefreshToken);

        // When
        Tokens tokens = jwtService.recreateTokens(refreshToken);

        // Then
        assertEquals(newAccessToken, tokens.getAccessToken());
        assertEquals(newRefreshToken, tokens.getRefreshToken());
    }

    @Test
    void 토큰_재발급_만료로_실패_테스트() {
        // Given
        String refreshToken = "refreshToken";

        when(jwtUtil.isExpired(refreshToken)).thenReturn(true);

        // When
        CustomException customException = assertThrows(CustomException.class, () -> jwtService.recreateTokens(refreshToken));

        // Then
        assertEquals(JwtExceptionCode.INVALID_TOKEN, customException.getExceptionCode());
    }

    @Test
    void 토큰_재발급_리프레시_토큰이_아니라_실패_테스트() {
        // Given
        String refreshToken = "refreshToken";

        when(jwtUtil.isExpired(refreshToken)).thenReturn(false);
        when(jwtUtil.getTokenType(refreshToken)).thenReturn("some");

        // When
        CustomException customException = assertThrows(CustomException.class, () -> jwtService.recreateTokens(refreshToken));

        // Then
        assertEquals(JwtExceptionCode.INVALID_TOKEN, customException.getExceptionCode());
    }

}
