package com.sparta.todo.todo;

import com.sparta.todo.user.User;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.SerializationUtils;

public class TodoTestUtils {

    public static Todo get(Todo todo, User user) {
        return get(todo, 1L, LocalDateTime.now(), user);
    }

    public static Todo get(
        Todo todo,
        Long id,
        LocalDateTime createdAt,
        User user
    ) {
        var newTodo = SerializationUtils.clone(todo);
        ReflectionTestUtils.setField(newTodo, Todo.class, "id", id, Long.class);
        ReflectionTestUtils.setField(newTodo, Todo.class, "createdAt", createdAt, LocalDateTime.class);
        ReflectionTestUtils.setField(newTodo, "user", user, User.class);
        return newTodo;

    }

}

