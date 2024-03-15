package com.sparta.todo.todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sparta.todo.common.TodoFixture;
import com.sparta.todo.user.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest implements TodoFixture {

    @InjectMocks
    TodoService todoService;

    @Mock
    TodoRepository todoRepository;

    @DisplayName("할일카드 생성")
    @Test
    void createPost() {
        // given
        given(todoRepository.save(any(Todo.class))).willReturn(TEST_TODO);

        // when & then
        assertDoesNotThrow(() -> todoService
            .createTodo(TEST_TODO_REQUEST_DTO, TEST_USER));
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @DisplayName("게시글 조회")
    @Test
    void getTodo() {
        // given
        var testTodo = TodoTestUtils.get(TEST_TODO, TEST_USER);
        given(todoRepository.findById(eq(TEST_TODO_ID))).willReturn(Optional.of(testTodo));

        // when
        var result = todoService.getTodo(TEST_TODO_ID);

        // then
        assertThat(result).isEqualTo(new TodoResponseDto(testTodo));
    }

    @DisplayName("게시글 전체 조회")
    @Test
    void getPostList() {
        // given
        var testTodo1 = TodoTestUtils.get(TEST_TODO, 1L,
            LocalDateTime.now(), TEST_USER);
        var testTodo2 = TodoTestUtils.get(TEST_TODO, 2L,
            LocalDateTime.now().minusMinutes(1), TEST_USER);
        given(todoRepository.findAll(Sort.by(Direction.DESC, "createdAt")))
            .willReturn(List.of(testTodo1, testTodo2));

        // when
        var result = todoService.getTodoList();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo(new TodoResponseDto(testTodo1));
        assertThat(result.get(1)).isEqualTo(new TodoResponseDto(testTodo2));
    }

    @DisplayName("게시글 수정")
    @Test
    void updatePost() {
        // given
        ReflectionTestUtils.setField(TEST_USER, User.class, "id", TEST_USER_ID, Long.class);
        var testTodo = TodoTestUtils.get(TEST_TODO, TEST_USER);
        given(todoRepository.findById(eq(TEST_TODO_ID))).willReturn(Optional.of(testTodo));

        // when
        var request = new TodoRequestDto("updateTitle", "updateContent");

        // then
        assertDoesNotThrow(() ->
            todoService.updateTodo(TEST_TODO_ID, request, TEST_USER));
        assertThat(testTodo.getTitle()).isEqualTo("updateTitle");
        assertThat(testTodo.getContent()).isEqualTo("updateContent");
    }
}
