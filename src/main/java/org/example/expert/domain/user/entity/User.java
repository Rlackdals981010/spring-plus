package org.example.expert.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.enums.UserRole;

@Getter
@Entity
@NoArgsConstructor
// 테이블에 인덱스 추가
//@Table(name = "users",indexes = {@Index(name = "idx_user_nickname", columnList = "nickname")})
@Table(name = "users")
public class User extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    @Column
    private String nickname;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String email, String password, String nickname,UserRole userRole) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    private User(Long userId,String email, UserRole userRole) {
        this.id =userId;
        this.email = email;
        this.userRole = userRole;
    }

    public static User fromAuthUser(AuthUser authUser) {
        String userRole =authUser.getAuthorities().iterator().next().getAuthority();
//        String userRole = authUser.getAuthorities().toString().substring(1,authUser.getAuthorities().toString().length()-1);
        return new User(authUser.getId(),authUser.getEmail(), UserRole.of(userRole));
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
