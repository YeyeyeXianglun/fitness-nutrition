package com.fit.service;

import com.fit.dto.auth.LoginRequest;
import com.fit.dto.auth.LoginResponse;
import com.fit.dto.auth.RegisterRequest;
public interface AuthService {
    LoginResponse register(RegisterRequest req);
    LoginResponse login(LoginRequest req);
}
