package corp.pjh.kiki.jwt.util;

import io.jsonwebtoken.security.SignatureException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("testtttttttttttttttttttttttttttttttt");
    }

    @Test
    void 토큰_생성_테스트() {
        // Given

        // When
        String token = jwtUtil.createJwt("access", "test", "test", "test", 60000);

        // Then
        assertNotNull(token);
    }

    @Test
    void 토큰_타입_테스트() {
        // Given
        String token = jwtUtil.createJwt("access", "test", "test", "test", 60000);

        // When
        String tokenType = jwtUtil.getTokenType(token);

        // Then
        assertEquals("access", tokenType);
    }

    @Test
    void 토큰_클레임_subject_확인_테스트() {
        // Given
        String token = jwtUtil.createJwt("access", "test", "test", "test", 60000);

        // When
        String subject = jwtUtil.getSubject(token);

        // Then
        assertEquals("test", subject);
    }

    @Test
    void 토큰_클레임_name_확인_테스트() {
        // Given
        String token = jwtUtil.createJwt("access", "test", "test", "test", 60000);

        // When
        String name = jwtUtil.getName(token);

        // Then
        assertEquals("test", name);
    }

    @Test
    void 토큰_클레임_role_확인_테스트() {
        // Given
        String token = jwtUtil.createJwt("access", "test", "test", "test", 60000);

        // When
        String role = jwtUtil.getRole(token);

        // Then
        assertEquals("test", role);
    }

    @Test
    void 토큰_유효_검증_테스트() {
        // Given
        String token = jwtUtil.createJwt("access", "test", "test", "test", 60000);

        // When
        boolean expired = jwtUtil.isExpired(token);

        // Then
        assertFalse(expired);
    }

    @Test
    void 토큰_만료_검증_테스트() {
        // Given
        String token = jwtUtil.createJwt("access", "test", "test", "test", -60000);

        // When
        boolean expired = jwtUtil.isExpired(token);

        // Then
        assertTrue(expired);
    }

    @Test
    void 토큰_위조_검증_테스트() {
        // Given
        String token = jwtUtil.createJwt("access", "test", "test", "test", 60000);

        // When
        String modifiedToken = token.substring(0, token.length() - 1) + (token.charAt(token.length() - 1) == 'a' ? 'b' : 'a');

        // Then
        assertThrows(SignatureException.class, () -> {
            jwtUtil.getTokenType(modifiedToken);
        });
    }

}