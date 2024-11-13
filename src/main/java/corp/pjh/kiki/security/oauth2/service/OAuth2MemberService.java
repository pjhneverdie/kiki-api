package corp.pjh.kiki.security.oauth2.service;

import corp.pjh.kiki.member.domain.Member;
import corp.pjh.kiki.member.domain.Role;
import corp.pjh.kiki.member.repository.MemberRepository;
import corp.pjh.kiki.security.oauth2.dto.KakaoResponse;
import corp.pjh.kiki.security.oauth2.dto.MemberPrincipal;
import corp.pjh.kiki.security.oauth2.dto.OAuth2Response;

import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2Response oAuth2Response;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        switch (registrationId) {
            case "kakao":
                oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
                break;

            default:
                return null;
        }

        String subject = oAuth2Response.getProvider() + "-" + oAuth2Response.getSubject();
        Member foundMember = memberRepository.findBySubject(subject);

        if (foundMember == null) {
            Member member = new Member(null, subject, oAuth2Response.getName(), oAuth2Response.getThumbUrl(), Role.FREE);
            memberRepository.save(member);

            return new MemberPrincipal(member.getSubject(), member.getName(), member.getRole());
        } else {
            foundMember.updateMember(oAuth2Response.getName(), oAuth2Response.getThumbUrl());
            return new MemberPrincipal(foundMember.getSubject(), foundMember.getName(), foundMember.getRole());
        }
    }

}
