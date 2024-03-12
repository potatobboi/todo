package com.sparta.todo.todo;

import com.sparta.todo.common.enumeration.ErrorCode;
import com.sparta.todo.common.exception.InvalidInputException;
import com.sparta.todo.user.User;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoResponseDto createTodo(TodoRequestDto requestDto, User user) {
        Todo todo = Todo.builder()
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .user(user)
            .build();

        return new TodoResponseDto(todoRepository.save(todo));
    }

    public TodoResponseDto getTodo(Long todoId) {
        Todo todo = findTodo(todoId);

        return new TodoResponseDto(todo);
    }

    public List<TodoResponseDto> getTodoList() {
        List<Todo> todoList = todoRepository.findAll(Sort.by(Direction.DESC, "createdAt"));

        return todoList.stream()
            .map(TodoResponseDto::new)
            .toList();
    }

    @Transactional
    public void updateTodo(Long todoId, TodoRequestDto requestDto, User user) {
        Todo todo = findTodo(todoId);
        validateUser(todo.getUser().getId(), user.getId());

        todo.update(requestDto);
    }

    private Todo findTodo(Long todoId) {

        return todoRepository.findById(todoId).orElseThrow(
            () -> new InvalidInputException(ErrorCode.NOT_FOUND_POST)
        );
    }

    private void validateUser(Long writerId, Long inputId) {
        if (!Objects.equals(writerId, inputId)) {
            throw new InvalidInputException(ErrorCode.NOT_VALID_USER);
        }
    }
}
