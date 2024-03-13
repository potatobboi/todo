package com.sparta.todo.comment;

import com.sparta.todo.common.ResponseDto;
import com.sparta.todo.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/todos/{todoId}/comments")
    public ResponseEntity<ResponseDto<CommentResponseDto>> createComment(
        @PathVariable Long todoId,
        @RequestBody CommentRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto responseDto =
            commentService.createComment(todoId, requestDto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK.value()).body(
            ResponseDto.<CommentResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
                .message("댓글 작성 성공")
                .data(responseDto)
                .build()
        );
    }
}
