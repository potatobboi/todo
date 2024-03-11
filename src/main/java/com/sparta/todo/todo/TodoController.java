package com.sparta.todo.todo;

import com.sparta.todo.common.ResponseDto;
import com.sparta.todo.user.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/todos")
@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<ResponseDto<TodoResponseDto>> createTodo(
        @Valid @RequestBody TodoRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TodoResponseDto responseDto = todoService.createTodo(requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            ResponseDto.<TodoResponseDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("할일카드 작성 성공")
                .data(responseDto)
                .build()
        );
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<ResponseDto<TodoResponseDto>> getTodo(
        @PathVariable Long todoId) {
        TodoResponseDto responseDto = todoService.getTodo(todoId);

        return ResponseEntity.status(HttpStatus.OK.value())
            .body(ResponseDto.<TodoResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message("할일카드 단건 조회 성공")
                .data(responseDto)
                .build());
    }
}