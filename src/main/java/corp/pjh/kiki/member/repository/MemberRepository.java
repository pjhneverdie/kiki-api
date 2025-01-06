package corp.pjh.kiki.member.repository;

import corp.pjh.kiki.member.domain.Member;

import jakarta.persistence.EntityManager;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findBySubject(String subject) {
        String query = "SELECT m FROM Member m WHERE m.subject=:subject";

        List<Member> result = em.createQuery(query, Member.class)
                .setParameter("subject", subject)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    public void delete(Member member) {
        em.remove(member);
    }

}
