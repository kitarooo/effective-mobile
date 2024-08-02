package com.backend.effectivemobile.service;

import com.backend.effectivemobile.dto.request.LoginRequest;
import com.backend.effectivemobile.dto.request.RegistrationRequest;
import com.backend.effectivemobile.dto.response.AuthenticationResponse;

public interface AuthService {
    AuthenticationResponse login(LoginRequest request);
    String registration(RegistrationRequest request);
}
