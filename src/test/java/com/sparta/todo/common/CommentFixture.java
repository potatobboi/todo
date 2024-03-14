package com.sparta.todo.common;

import com.sparta.todo.comment.Comment;
import com.sparta.todo.comment.CommentRequestDto;
import com.sparta.todo.comment.CommentResponseDto;

public interface CommentFixture extends UserFixture {

    Long TEST_COMMENT_ID = 1L;
    String TEST_COMMENT_CONTENT = "content";
    Long TEST_ANOTHER_COMMENT_ID = 2L;


    Comment TEST_COMMENT = Comment.builder()
        .content(TEST_COMMENT_CONTENT)
        .user(TEST_USER)
        .build();

    Comment TEST_ANOTHER_COMMENT = Comment.builder()
        .content(TEST_COMMENT_CONTENT)
        .user(TEST_ANOTHER_USER)
        .build();

    CommentRequestDto TEST_COMMENT_REQUEST_DTO = CommentRequestDto.builder()
        .content(TEST_COMMENT_CONTENT)
        .build();

    CommentRequestDto TEST_ANOTHER_COMMENT_REQUEST_DTO = CommentRequestDto.builder()
        .content(ANOTHER_PREFIX + TEST_COMMENT_CONTENT)
        .build();

    CommentResponseDto commentResponseDto1 = new CommentResponseDto(TEST_COMMENT);
    CommentResponseDto commentResponseDto2 = new CommentResponseDto(TEST_ANOTHER_COMMENT);
}
