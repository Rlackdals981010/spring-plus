package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByNickname(String nickname);

    @Query("SELECT u.id FROM User u where u.nickname= :nickname")
    Long findUserResponseByNickname(@Param("nickname")String nickname);

}
