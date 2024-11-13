package corp.pjh.kiki.member.domain;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(nullable = false)
    private String thumbUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public void updateMember(String name, String thumbUrl) {
        this.name = name;
        this.thumbUrl = thumbUrl;
    }

    public void updateMember(Role role) {
        this.role = role;
    }

}
