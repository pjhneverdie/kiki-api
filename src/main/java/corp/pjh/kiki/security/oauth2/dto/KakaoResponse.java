package corp.pjh.kiki.security.oauth2.dto;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getSubject() {
        return attribute.get("id").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");

        return properties.get("nickname").toString();
    }

    @Override
    public String getThumbUrl() {
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");

        return properties.get("profile_image").toString();
    }

}
