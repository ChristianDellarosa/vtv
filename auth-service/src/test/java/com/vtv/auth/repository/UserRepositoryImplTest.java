package com.vtv.auth.repository;

import com.vtv.auth.domain.User;
import com.vtv.auth.repository.impl.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.vtv.auth.mock.UserFactory.buildValidUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryImplTest {
    @InjectMocks
    private UserRepositoryImpl userRepository;

    @Test
    public void getByUsername_ExistingUser() {
        final var validUser = buildValidUser();
        Optional<User> user = userRepository.getByUsername(validUser.username());

        assertTrue(user.isPresent());
        assertEquals("Christian", user.get().username());
        assertEquals("password123", user.get().password());
        assertEquals("Christian", user.get().name());
    }

    @Test
    public void getByUsername_NonExistingUser() {
        Optional<User> user = userRepository.getByUsername("invalid-username");
        assertFalse(user.isPresent());
        assertEquals(user, Optional.empty());
    }
}
