package corp.pjh.kiki.security.oauth2.dto;

public interface OAuth2Response {

    String getProvider();

    String getSubject();

    String getName();

    String getThumbUrl();

}
