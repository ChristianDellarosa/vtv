package com.vtv.auth.mock;

import com.vtv.auth.domain.User;

public class UserFactory {

    public static User buildUser(String username, String password) {
        return User.builder()
                .name("aName")
                .username(username)
                .password(password)
                .build();
    }

    public static User buildValidUser() {
        return User.builder()
                .name("Christian")
                .username("Christian")
                .password("password")
                .build();
    }

    public static User buildInvalidUser() {
        return User.builder()
                .name("Christian")
                .username("Christian")
                .password("aBadPassword")
                .build();
    }
}
