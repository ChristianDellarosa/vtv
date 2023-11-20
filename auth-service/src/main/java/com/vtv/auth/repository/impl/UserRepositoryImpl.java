package com.vtv.auth.repository.impl;

import com.vtv.auth.domain.User;
import com.vtv.auth.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Set<User> users;

    public UserRepositoryImpl() {
        this.users = Set.of(
                User.builder().username("Christian").password("password123").name("Christian").build(),
                User.builder().username("LucasPerez").password("password1005").name("Lucas").build(),
                User.builder().username("RominaDiaz").password("password0912").name("Romina").build()
        );
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return users.stream()
                .filter(user -> user.username().equals(username))
                .findFirst();
    }
}
