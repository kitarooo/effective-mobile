package com.backend.effectivemobile;

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
import com.backend.effectivemobile.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        LoginRequest request = new LoginRequest("aza@gmail.com", "aza");
        User user = new User("aza@gmail.com", "aza", "aza", "131313", Role.ROLE_USER, new ArrayList<>());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthenticationResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getAccessToken());
    }

    @Test
    void testLogin_UserNotFound() {
        LoginRequest request = new LoginRequest("azaza@gmail.com", "123");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> authService.login(request));
        assertEquals("Пользователь не найден!", thrown.getMessage());
    }

    @Test
    void testLogin_IncorrectPassword() {
        LoginRequest request = new LoginRequest("azaza@gmail.com", "123");
        User user = new User("aza@gmail.com", "aza", "aza", "131313", Role.ROLE_USER, new ArrayList<>());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        IncorrectDataException thrown = assertThrows(IncorrectDataException.class, () -> authService.login(request));
        assertEquals("Данные введены неправильно!", thrown.getMessage());
    }

    @Test
    void testRegistration_Success() {
        RegistrationRequest request = new RegistrationRequest("aza@gmail.com", "aza", "aza", "131313");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        String result = authService.registration(request);

        assertEquals("Регистрация прошла успешно!", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegistration_UserAlreadyExists() {
        // Создание запроса регистрации
        RegistrationRequest request = new RegistrationRequest(
                "aza@gmail.com",
                "aza",
                "131313",
                "aza"
        );
        User existingUser = User.builder()
                .username("aza")
                .password("aza")
                .phoneNumber("131313")
                .email("aza@gmail.com")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        UserAlreadyExistException thrown = assertThrows(UserAlreadyExistException.class, () -> authService.registration(request));

        assertEquals("Пользователь с email: aza@gmail.com уже существует!", thrown.getMessage());
    }
}
