package com.sparta.todo.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private String username;

    public UserDto(User user) {
        this.username = user.getUsername();
    }
}
