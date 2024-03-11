package com.sparta.todo.user;

import com.sparta.todo.common.enumeration.ErrorCode;
import com.sparta.todo.common.exception.DuplicateUsernameException;
import com.sparta.todo.common.exception.InvalidInputException;
import com.sparta.todo.jwt.JwtUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto requestDto) {
        // 비밀번호 일치 확인
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            throw new InvalidInputException(ErrorCode.INVALID_PASSWORD);
        }

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new DuplicateUsernameException(username);
        }

        // 유저 등록
        User user = User.builder()
            .username(username)
            .password(password)
            .build();

        userRepository.save(user);
    }

    public String login(LoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 등록횐 유저 확인
        User user = userRepository.findByUsername(username).orElseThrow(
            () -> new InvalidInputException(ErrorCode.USER_NOT_FOUND)
        );

        // 비밀번호 확인
        validatePassword(user, password);

        return jwtUtil.createToken(user.getUsername());
    }

    private void validatePassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidInputException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
