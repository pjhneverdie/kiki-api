package corp.pjh.kiki.member.service;

import corp.pjh.kiki.member.domain.Member;
import corp.pjh.kiki.member.dto.MemberResponse;
import corp.pjh.kiki.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponse getMemberResponse(String subject) {
        Member foundMember = memberRepository.findBySubject(subject);

        return new MemberResponse(foundMember.getName(), foundMember.getThumbUrl(), foundMember.getRole());
    }

}
