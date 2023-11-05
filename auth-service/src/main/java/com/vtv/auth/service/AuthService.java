package com.vtv.auth.service;

import com.vtv.auth.model.SignIn;

public interface AuthService {
    SignIn signIn(String basicAuthToken);
}
