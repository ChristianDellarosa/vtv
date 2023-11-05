package com.vtv.auth.repository.impl;

import com.vtv.auth.model.User;
import com.vtv.auth.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Set<User> users;

    public UserRepositoryImpl() {
        this.users = Set.of(
                User.builder().username("Christian01").password("password123").name("Christian").build(),
                new User("LucasPerez", "password1005", "Lucas"),
                new User("RominaDiaz", "password0912", "Romina")
        );
    }

    @Override
    public List<User> getAll() {
        return users.stream().toList();
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return users.stream()
                .filter(user -> user.username().equals(username))
                .findFirst();
    }
}
