package com.sparta.todo.common;

import com.sparta.todo.user.LoginRequestDto;
import com.sparta.todo.user.SignupRequestDto;
import com.sparta.todo.user.User;

public interface UserFixture {

    String ANOTHER_PREFIX = "another-";
    String REPOSITORY_PREFIX = "r";
    Long TEST_USER_ID = 1L;
    String TEST_USER_NAME = "username";
    String TEST_USER_PASSWORD = "TestPass!";
    String TEST_USER_PASSWORD_FAIL = "fail-" + TEST_USER_PASSWORD;

    User TEST_USER = User.builder()
        .username(TEST_USER_NAME)
        .password(TEST_USER_PASSWORD)
        .build();

    User TEST_ANOTHER_USER = User.builder()
        .username(ANOTHER_PREFIX + TEST_USER_NAME)
        .password(ANOTHER_PREFIX + TEST_USER_PASSWORD)
        .build();


    User TEST_REPOSITORY_USER = User.builder()
        .username(REPOSITORY_PREFIX + TEST_USER_NAME)
        .password(REPOSITORY_PREFIX + TEST_USER_PASSWORD)
        .build();

    SignupRequestDto TEST_SIGN_UP_REQUEST_DTO = SignupRequestDto.builder()
        .username(TEST_USER_NAME)
        .password(TEST_USER_PASSWORD)
        .passwordConfirm(TEST_USER_PASSWORD)
        .build();

    SignupRequestDto TEST_ANOTHER_SIGN_UP_REQUEST_DTO = SignupRequestDto.builder()
        .username("userid")
        .password(TEST_USER_PASSWORD)
        .passwordConfirm(TEST_USER_PASSWORD)
        .build();

    LoginRequestDto TEST_LOGIN_REQUEST_DTO = LoginRequestDto.builder()
        .username(TEST_USER_NAME)
        .password(TEST_USER_PASSWORD)
        .build();
}
