package com.sparta.todo.todo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sparta.todo.common.ControllerTest;
import com.sparta.todo.common.TodoFixture;
import com.sparta.todo.common.UserFixture;
import com.sparta.todo.common.enumeration.ErrorCode;
import com.sparta.todo.common.exception.InvalidInputException;
import com.sparta.todo.user.User;
import java.time.LocalDateTime;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(TodoController.class)
class TodoControllerTest extends ControllerTest implements TodoFixture, UserFixture {

    @MockBean
    private TodoService todoService;

    @DisplayName("할일작성 생성 요청")
    @Test
    void createdTodo_success() throws Exception {
        // given // when
        var action = mockMvc.perform(post("/api/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(TEST_TODO_REQUEST_DTO)));

        // then
        action.andExpect(status().isCreated());
        verify(todoService, times(1))
            .createTodo(any(TodoRequestDto.class), eq(TEST_USER));
    }

    @Nested
    @DisplayName("할일카드 조회 요청")
    class getTodo {

        @DisplayName("할일카드 조회 요청 성공")
        @Test
        void getTodo_success() throws Exception {
            // given
            given(todoService.getTodo(eq(TEST_TODO_ID))).willReturn(TEST_TODO_RESPONSE_DTO);

            // when
            var action = mockMvc.perform(get("/api/todos/{todoId}", TEST_TODO_ID)
                .accept(MediaType.APPLICATION_JSON));

            // then
            action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value(TEST_TODO_TITLE))
                .andExpect(jsonPath("$.data.content").value(TEST_TODO_CONTENT));
        }

        @DisplayName("할일카드 조회 요청 실패 - 존재하지 않는 할일카드ID")
        @Test
        void getTodo_fail() throws Exception {
            // given
            given(todoService.getTodo(eq(TEST_TODO_ID))).willThrow(
                new InvalidInputException(ErrorCode.NOT_FOUND_TODO));

            // when
            var action = mockMvc.perform(get("/api/todos/{todoId}", TEST_TODO_ID)
                .accept(MediaType.APPLICATION_JSON));

            // then
            action
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                    .value(ErrorCode.NOT_FOUND_TODO.getMessage()));
        }
    }

    @DisplayName("할일카드 전체 조회")
    @Test
    void getTodoList_success() throws Exception {
        // given
        var testTodo1 = TodoTestUtils.get(TEST_TODO, TEST_TODO_ID,
            LocalDateTime.now(), TEST_USER);
        var testTodo2 = TodoTestUtils.get(TEST_ANOTHER_TODO, TEST_ANOTHER_TODO_ID,
            LocalDateTime.now().minusMinutes(1), TEST_ANOTHER_USER);

        given(todoService.getTodoList()).willReturn(List.of(
            new TodoResponseDto(testTodo1),
            new TodoResponseDto(testTodo2)));

        // when
        var action = mockMvc.perform(get("/api/todos")
            .accept(MediaType.APPLICATION_JSON));

        // then
        action
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[*].title")
                .value(Matchers.containsInAnyOrder(TEST_TODO_TITLE,
                    TEST_ANOTHER_TODO.getTitle())))
            .andExpect(jsonPath("$.data[*].content")
                .value(Matchers.containsInAnyOrder(TEST_TODO_CONTENT,
                    TEST_ANOTHER_TODO.getContent())));
    }

    @Nested
    @DisplayName("할일카드 수정 요청")
    class putTodo {

        @DisplayName("할일카드 수정 요청 성공")
        @Test
        void putTodo_success() throws Exception {
            // given
            doNothing().when(todoService)
                .updateTodo(eq(TEST_TODO_ID), any(TodoRequestDto.class), any(User.class));

            // when
            var action = mockMvc.perform(put("/api/todos/{todoId}", TEST_TODO_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_TODO_REQUEST_DTO)));

            // then
            action.andExpect(status().isOk());
            verify(todoService, times(1)).updateTodo(eq(TEST_TODO_ID), any(TodoRequestDto.class),
                any(User.class));
        }

        @DisplayName("할일카드 수정 요청 실패 - 작성자가 아님")
        @Test
        void updateTodo_fail() throws Exception {
            // given
            doThrow(new InvalidInputException(ErrorCode.NOT_VALID_USER)).when(todoService)
                .updateTodo(eq(TEST_TODO_ID), any(TodoRequestDto.class), any(User.class));

            // when
            var action = mockMvc.perform(put("/api/todos/{todoId}", TEST_TODO_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_TODO_REQUEST_DTO)));

            // then
            action.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                    .value(ErrorCode.NOT_VALID_USER.getMessage()));
        }
    }
}
