package com.sparta.todo.comment;

import com.sparta.todo.common.enumeration.ErrorCode;
import com.sparta.todo.common.exception.InvalidInputException;
import com.sparta.todo.todo.Todo;
import com.sparta.todo.todo.TodoRepository;
import com.sparta.todo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;

    public CommentResponseDto createComment(Long todoId, CommentRequestDto requestDto, User user) {
        Todo todo = findTodo(todoId);

        Comment comment = Comment.builder()
            .todo(todo)
            .user(user)
            .content(requestDto.getContent())
            .build();

        return new CommentResponseDto(commentRepository.save(comment));
    }

    private Todo findTodo(Long todoId) {

        return todoRepository.findById(todoId).orElseThrow(
            () -> new InvalidInputException(ErrorCode.NOT_FOUND_POST)
        );
    }
}
