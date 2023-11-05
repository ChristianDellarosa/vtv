package com.vtv.auth.repository;

import com.vtv.auth.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getByUsername(String username);
}
