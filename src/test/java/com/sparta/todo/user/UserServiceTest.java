package com.sparta.todo.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sparta.todo.common.UserFixture;
import com.sparta.todo.common.exception.DuplicateUsernameException;
import com.sparta.todo.jwt.JwtUtil;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest implements UserFixture {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtUtil jwtUtil;

    @Nested
    @DisplayName("회원가입")
    class signUp {

        @Test
        @DisplayName("회원가입 성공")
        void signup_success() {
            //given
            //when & then
            assertDoesNotThrow(() -> userService.signup(TEST_SIGN_UP_REQUEST_DTO));
            verify(userRepository, times(1))
                .save(any(User.class));
        }

        @Test
        @DisplayName("회원가입 실패 - 중복된 이름")
        void signup_fail() {
            //given
            var testUser = TEST_USER;
            given(userRepository.findByUsername(testUser.getUsername()))
                .willReturn(Optional.of(testUser));

            //when & then
            assertThatThrownBy(() -> userService.signup(TEST_SIGN_UP_REQUEST_DTO))
                .isInstanceOf(DuplicateUsernameException.class);
        }
    }
}
