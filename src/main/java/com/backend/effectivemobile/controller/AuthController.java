package com.backend.effectivemobile.controller;

import com.backend.effectivemobile.dto.request.LoginRequest;
import com.backend.effectivemobile.dto.request.RegistrationRequest;
import com.backend.effectivemobile.dto.response.AuthenticationResponse;
import com.backend.effectivemobile.exception.handler.ExceptionResponse;
import com.backend.effectivemobile.service.AuthService;
import com.backend.effectivemobile.service.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;


    @PostMapping("/register")
    @Operation(summary = "Регистрация для клиента", description = "Ендпоинт для регистрации!",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "400", description = "User already exist exception!"
                    )
            })
    public String registration(@RequestBody RegistrationRequest request) {
        return authService.registration(request);
    }
    @PostMapping("/login")
    @Operation(summary = "Авторизация для всех пользователей", description = "Ендпоинт для авторизации и выдачи токена!",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "400", description = "Incorrect Data Exception!"
                    )
            })
    public AuthenticationResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
