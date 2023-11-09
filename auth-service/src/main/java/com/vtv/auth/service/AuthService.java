package com.vtv.auth.service;

import com.vtv.auth.domain.SignIn;

public interface AuthService {
    SignIn signIn(String basicAuthToken);
}
