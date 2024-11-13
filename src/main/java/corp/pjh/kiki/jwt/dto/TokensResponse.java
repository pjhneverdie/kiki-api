package corp.pjh.kiki.jwt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokensResponse {

    private final String accessToken;

    private final String refreshToken;

}
