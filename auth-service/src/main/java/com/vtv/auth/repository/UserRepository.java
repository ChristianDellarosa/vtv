package com.vtv.auth.repository;

import com.vtv.auth.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> getByUsername(String username);
}
