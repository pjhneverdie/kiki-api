package corp.pjh.kiki.for_test;

import corp.pjh.kiki.member.domain.Role;
import corp.pjh.kiki.security.oauth2.dto.MemberPrincipal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class TestSecurityContextFactory implements WithSecurityContextFactory<WithTestUser> {

    @Override
    public SecurityContext createSecurityContext(WithTestUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        MemberPrincipal principal = new MemberPrincipal("subject", "pjh", Role.FREE);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );

        context.setAuthentication(authentication);

        return context;
    }

}
