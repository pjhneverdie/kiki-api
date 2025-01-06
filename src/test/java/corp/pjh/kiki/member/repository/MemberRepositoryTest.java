package corp.pjh.kiki.member.repository;

import corp.pjh.kiki.member.domain.Member;
import corp.pjh.kiki.member.domain.Role;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 멤버_저장_테스트() {
        // Given
        Member member = new Member(null, "subject", "name", "thumbUrl", Role.FREE);

        // When
        memberRepository.save(member);

        // Then
        assertNotNull(member.getId());
    }

    @Test
    public void subject로_멤버_조회_테스트() {
        // Given
        Member member = new Member(null, "subject", "name", "thumbUrl", Role.FREE);
        memberRepository.save(member);

        // When
        Member foundMember = memberRepository.findBySubject(member.getSubject());

        // Then
        assertEquals(member, foundMember);
    }

    @Test
    public void 멤버_삭제_테스트() {
        // Given
        Member member = new Member(null, "subject", "name", "thumbUrl", Role.FREE);
        memberRepository.save(member);

        // When
        memberRepository.delete(member);

        // Then
        Member foundMember = memberRepository.findBySubject(member.getSubject());

        assertNull(foundMember);
    }

}