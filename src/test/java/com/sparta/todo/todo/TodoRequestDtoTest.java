package com.sparta.todo.todo;

import static org.assertj.core.api.Assertions.assertThat;

import com.sparta.todo.common.TodoFixture;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TodoRequestDtoTest implements TodoFixture {

    @DisplayName("할일작성 요청 Dto 생성")
    @Nested
    class createTodoRequestDto {

        @DisplayName("할일작성 요청 Dto 생성 성공")
        @Test
        void createTodoRequestDto_success() {
            // given
            var todoRequestDto = new TodoRequestDto(TEST_TODO_TITLE, TEST_TODO_CONTENT);

            // when
            var violations = validate(todoRequestDto);

            // then
            assertThat(violations).isEmpty();
        }

        @DisplayName("할일요청 요청 Dto 생성 실패 - 비어있는 title")
        @Test
        void createTodoRequestDto_fail_nullTitle() {
            // given
            var todoRequestDto = new TodoRequestDto("", TEST_TODO_CONTENT);

            // when
            var violations = validate(todoRequestDto);

            // then
            assertThat(violations).hasSize(1);
            assertThat(violations)
                .extracting("message")
                .contains("제목은 필수입니다.");
        }

        @DisplayName("할일요청 요청 Dto 생성 실패 - 비어있는 content")
        @Test
        void createTodoRequestDto_fail_nullContent() {
            // given
            var todoRequestDto = new TodoRequestDto(TEST_TODO_TITLE, "");

            // when
            var violations = validate(todoRequestDto);

            // then
            assertThat(violations).hasSize(1);
            assertThat(violations)
                .extracting("message")
                .contains("내용은 필수입니다.");
        }
    }

    private Set<ConstraintViolation<TodoRequestDto>> validate(TodoRequestDto todoRequestDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(todoRequestDto);
    }
}
