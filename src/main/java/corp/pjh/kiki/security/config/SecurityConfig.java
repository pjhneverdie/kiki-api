package corp.pjh.kiki.security.config;

import corp.pjh.kiki.security.filter.LoginCheckFilter;
import corp.pjh.kiki.security.oauth2.handler.AuthenticationEntryPointHandler;
import corp.pjh.kiki.security.oauth2.handler.OAuth2FailureHandler;
import corp.pjh.kiki.security.oauth2.handler.OAuth2SuccessHandler;
import corp.pjh.kiki.security.oauth2.service.OAuth2MemberService;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Profile("!testcase")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${security.use-debug-mode:false}")
    boolean webSecurityDebug;

    private final OAuth2MemberService oAuth2MemberService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final AuthenticationEntryPointHandler authenticationEntryPointHandler;

    private final LoginCheckFilter loginCheckFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .debug(webSecurityDebug)
                .ignoring()
                .requestMatchers("/app", "/.well-known/assetlinks.json", "/reissue", "/favicon.ico");
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors((cors) -> {
            cors.configurationSource(new CorsConfigurationSource() {

                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.addAllowedOrigin("*");
                    corsConfiguration.addAllowedMethod("*");
                    corsConfiguration.addAllowedHeader("*");

                    return corsConfiguration;
                }
            });
        });

        http.authorizeHttpRequests((auth) -> auth.anyRequest().authenticated());

        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig.userService(oAuth2MemberService))
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
        );

        http.exceptionHandling((exceptionConfig) -> exceptionConfig.authenticationEntryPoint(authenticationEntryPointHandler));

        http.addFilterBefore(loginCheckFilter, OAuth2AuthorizationRequestRedirectFilter.class);

        return http.build();
    }

}