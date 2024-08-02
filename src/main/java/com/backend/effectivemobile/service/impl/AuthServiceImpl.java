package com.backend.effectivemobile.service.impl;

import com.backend.effectivemobile.dto.request.LoginRequest;
import com.backend.effectivemobile.dto.request.RegistrationRequest;
import com.backend.effectivemobile.dto.response.AuthenticationResponse;
import com.backend.effectivemobile.entity.User;
import com.backend.effectivemobile.entity.enums.Role;
import com.backend.effectivemobile.exception.IncorrectDataException;
import com.backend.effectivemobile.exception.NotFoundException;
import com.backend.effectivemobile.exception.UserAlreadyExistException;
import com.backend.effectivemobile.repository.UserRepository;
import com.backend.effectivemobile.security.jwt.JwtService;
import com.backend.effectivemobile.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден!"));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword()) && user.getEmail().equals(request.getEmail())) {
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .build();
        } else {
            throw new IncorrectDataException("Данные введены неправильно!");
        }
    }

    @Override
    public String registration(RegistrationRequest request) {
        if (userRepository.findByEmail(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Пользователь с email: " + request.getEmail() + " уже существует!");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);

        return "Регистрация прошла успешно!";
    }
}
