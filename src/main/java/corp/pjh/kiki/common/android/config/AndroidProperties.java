package corp.pjh.kiki.common.android.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!testcase")
@Configuration
@ConfigurationProperties(prefix = "client.android")
@Getter
@Setter
public class AndroidProperties {
    private String packageName;
    private String sha256FromMyKey;
    private String sha256FromGoogleSigningKey;
}
