package corp.pjh.kiki.common.web.config;

import corp.pjh.kiki.security.filter.LoginCheckFilter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!testcase")
@Configuration
public class ServletFilterConfig {

    @Bean
    public FilterRegistrationBean<LoginCheckFilter> registration(LoginCheckFilter filter) {
        FilterRegistrationBean<LoginCheckFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

}
