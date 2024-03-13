package com.sparta.todo.comment;

import com.sparta.todo.common.enumeration.ErrorCode;
import com.sparta.todo.common.exception.InvalidInputException;
import com.sparta.todo.todo.Todo;
import com.sparta.todo.todo.TodoRepository;
import com.sparta.todo.user.User;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<CommentResponseDto> getComments(Long todoId) {
        findTodo(todoId);
        List<Comment> commentList = commentRepository.findAllByTodoIdOrderByCreatedAtDesc(todoId);

        return commentList.stream()
            .map(CommentResponseDto::new)
            .toList();
    }

    @Transactional
    public void updateComment(Long todoId, Long commentId, CommentRequestDto requestDto, User user) {
            findTodo(todoId);
            Comment comment = findComment(commentId);

            validateUser(comment.getUser().getId(), user.getId());
            comment.update(requestDto);
    }

    @Transactional
    public void deleteComment(Long todoId, Long commentId, User user) {
        findTodo(todoId);
        Comment comment = findComment(commentId);

        validateUser(comment.getUser().getId(), user.getId());
        validateTodo(comment.getTodo().getId(), todoId);

        commentRepository.delete(comment);
    }

    private Todo findTodo(Long todoId) {

        return todoRepository.findById(todoId).orElseThrow(
            () -> new InvalidInputException(ErrorCode.NOT_FOUND_TODO)
        );
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> new InvalidInputException(ErrorCode.NOT_FOUND_COMMENT)
        );
    }

    private void validateUser(Long writerId, Long inputId) {
        if (!Objects.equals(writerId, inputId)) {
            throw new InvalidInputException(ErrorCode.NOT_VALID_USER);
        }
    }

    private void validateTodo(Long todoId, Long inputId) {
        if (!Objects.equals(todoId, inputId)) {
            throw new InvalidInputException(ErrorCode.NOT_VALID_TODO);
        }
    }
}
