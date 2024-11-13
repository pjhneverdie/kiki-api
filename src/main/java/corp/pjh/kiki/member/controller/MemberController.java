package corp.pjh.kiki.member.controller;

import corp.pjh.kiki.member.dto.MemberResponse;
import corp.pjh.kiki.member.service.MemberService;

import corp.pjh.kiki.security.oauth2.dto.MemberPrincipal;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public MemberResponse memberResponse() {
        MemberPrincipal principal = (MemberPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return memberService.getMemberResponse(principal.getSubject());
    }

}
