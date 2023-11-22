package com.vtv.auth.service;

public interface TokenService {

    String generate(String userId);

    void validate(String token);
}
