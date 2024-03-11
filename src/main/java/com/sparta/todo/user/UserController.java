package com.sparta.todo.user;

import com.sparta.todo.common.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<String>> signup(
        @Valid @RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.<String>builder()
            .statusCode(HttpStatus.OK.value())
            .message("회원가입이 완료되었습니다.")
            .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Void>> login(
        @RequestBody LoginRequestDto requestDto) {

        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, userService.login(requestDto))
            .body(ResponseDto.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("로그인 성공")
                .build());
    }
}
