package org.example.expert.domain.user.service;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;




@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
        validateNewPassword(userChangePasswordRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }

    private static void validateNewPassword(UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
        }
    }

    // 유저 100만개 생성
    @Transactional
    public void createUsersBulk() {
        int bulk = 1000000;
        int nicknameLength = 8;
        String password = "1234QWER!!";
        UserRole userRole = UserRole.ROLE_USER;
        String gmail = "@gmail.com";

        Set<String> nicknames = new HashSet<>();
        Random random = new Random();

        while (nicknames.size() < bulk) {
            StringBuilder nickname = new StringBuilder(nicknameLength);
            for (int i = 0; i < nicknameLength; i++) {
                int index = random.nextInt(CHARACTERS.length());
                nickname.append(CHARACTERS.charAt(index));
            }
            nicknames.add(nickname.toString());
        }

        String[] nicknameArray = nicknames.toArray(new String[0]);

        for(String nickName : nicknameArray){
            User newUser = new User(
                    nickName+gmail,
                    password,
                    nickName,
                    userRole
            );
            userRepository.save(newUser);
        }

    }

    public Long getUserFromBulk(String nickname) {
//
//        User user = userRepository.findByNickname(nickname).orElseThrow(() -> new InvalidRequestException("User not found"));
//        return user.getId();
        return userRepository.findUserResponseByNickname(nickname);
    }

    // 인덱스를 사용한 조회

}
