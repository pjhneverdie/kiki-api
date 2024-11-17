package corp.pjh.kiki.member.dto;

import corp.pjh.kiki.member.domain.Role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberResponse {

    private final String name;

    private final String thumbUrl;

    private final Role role;

}
