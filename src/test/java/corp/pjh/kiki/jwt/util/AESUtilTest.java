package corp.pjh.kiki.jwt.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AESUtilTest {

    private final AESUtil aesUtil = new AESUtil("local-secret-key", "local-secret-key");

    @Test
    public void 암호화_테스트() throws Exception {
        // Given
        String input = "eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlblR5cGUiOiJhY2Nlc3MiLCJzdWJqZWN0Ijoia2FrYW8tMzc4Mzg2MzI1NSIsIm5hbWUiOiLrsJXsp4TtmIEiLCJyb2xlIjoiRlJFRSIsImlhdCI6MTczMTMwNjgxMCwiZXhwIjoxNzMxMzkzMjEwfQ.A24bEJd4QEXSSJKlq95mY1XIZSvueSmdihxJlOMYS6E eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlblR5cGUiOiJyZWZyZXNoIiwic3ViamVjdCI6Imtha2FvLTM3ODM4NjMyNTUiLCJuYW1lIjoi67CV7KeE7ZiBIiwicm9sZSI6IkZSRUUiLCJpYXQiOjE3MzEzMDY4MTAsImV4cCI6MTczMTM5MzIxMH0.1oNgv0uWLTH8WOiBQxA86mRzLFIYGrPDk0onPwaLwT0";

        // When
        String encrypted = aesUtil.encrypt(input);

        // Then
        assertEquals(input, aesUtil.decrypt(encrypted));
    }

}