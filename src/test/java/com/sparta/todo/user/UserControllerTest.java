package com.sparta.todo.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sparta.todo.common.ControllerTest;
import com.sparta.todo.common.enumeration.ErrorCode;
import com.sparta.todo.common.exception.DuplicateUsernameException;
import com.sparta.todo.common.exception.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(UserController.class)
public class UserControllerTest extends ControllerTest {

    @MockBean
    private UserService userService;

    @Nested
    @DisplayName("회원가입 요청")
    class signup {

        @DisplayName("회원가입 요청 성공")
        @Test
        void signUp_success() throws Exception {
            //given //when
            var action = mockMvc.perform(post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_SIGN_UP_REQUEST_DTO)));

            //then
            action.andExpect(status().isOk());
            verify(userService, times(1))
                .signup(any(SignupRequestDto.class));
        }

        @DisplayName("회원가입 요청 실패")
        @Test
        void signup_fail() throws Exception {
            //given
            doThrow(new DuplicateUsernameException(TEST_USER_NAME)).when(userService)
                .signup(any(SignupRequestDto.class));

            //when
            var action = mockMvc.perform(post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_SIGN_UP_REQUEST_DTO)));

            //then
            action.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("로그인 요청")
    class login {

        @DisplayName("로그인 요청 성공")
        @Test
        void login_success() throws Exception {
            // given // when
            var action = mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_LOGIN_REQUEST_DTO)));

            // then
            action.andExpect(status().isOk());
            verify(userService, times(1))
                .login(any(LoginRequestDto.class));
        }

        @DisplayName("로그인 요청 실패")
        @Test
        void login_fail() throws Exception {
            //given
            doThrow(new InvalidInputException(ErrorCode.USER_NOT_FOUND)).when(userService)
                .login(any(LoginRequestDto.class));

            //when
            var action = mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_LOGIN_REQUEST_DTO)));

            //then
            action.andExpect(status().isBadRequest());
        }
    }
}
