package com.sparta.todo.common;

import com.sparta.todo.todo.Todo;
import com.sparta.todo.todo.TodoRequestDto;
import com.sparta.todo.todo.TodoResponseDto;

public interface TodoFixture extends UserFixture {

    Long TEST_TODO_ID = 1L;
    Long TEST_ANOTHER_TODO_ID = 2L;
    String TEST_TODO_TITLE = "title";
    String TEST_TODO_CONTENT = "content";

    Todo TEST_TODO = Todo.builder()
        .title(TEST_TODO_TITLE)
        .content(TEST_TODO_CONTENT)
        .user(TEST_USER)
        .build();

    Todo TEST_TODO2 = Todo.builder()
        .title(TEST_TODO_TITLE)
        .content(TEST_TODO_CONTENT)
        .user(TEST_USER)
        .build();

    Todo TEST_ANOTHER_TODO = Todo.builder()
        .title(ANOTHER_PREFIX + TEST_TODO_TITLE)
        .content(ANOTHER_PREFIX + TEST_TODO_CONTENT)
        .user(TEST_ANOTHER_USER)
        .build();


    TodoResponseDto todoResponseDto1 = new TodoResponseDto(TEST_TODO);
    TodoResponseDto todoResponseDto2 = new TodoResponseDto(TEST_TODO2);

    TodoRequestDto TEST_TODO_REQUEST_DTO = TodoRequestDto.builder()
        .title(TEST_TODO_TITLE)
        .content(TEST_TODO_CONTENT)
        .build();

    TodoResponseDto TEST_TODO_RESPONSE_DTO = TodoResponseDto.builder()
        .title(TEST_TODO_TITLE)
        .content(TEST_TODO_CONTENT)
        .build();
}
