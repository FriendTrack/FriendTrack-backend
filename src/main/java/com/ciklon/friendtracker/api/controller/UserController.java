package com.ciklon.friendtracker.api.controller;

import com.ciklon.friendtracker.api.constant.ApiPaths;
import com.ciklon.friendtracker.api.dto.user.*;
import com.ciklon.friendtracker.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    @PostMapping(ApiPaths.REGISTER)
    @Operation(summary = "Регистрация пользователя", description = "Регистрирует нового пользователя и возвращает JWT" +
            " токены")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная регистрация"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "409", description = "Конфликт данных. Вероятно пользователь с таким email уже существует."),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @SecurityRequirements
    public JwtAuthorityDto register(@Validated @RequestBody RegistrationRequestDto registrationRequestDto) {
        return userService.register(registrationRequestDto);
    }

    @PostMapping(ApiPaths.LOGIN)
    @Operation(summary = "Вход пользователя", description = "Авторизует пользователя и возвращает JWT токены")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход"),
            @ApiResponse(responseCode = "401", description = "Неверный логин или пароль"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @SecurityRequirements
    public JwtAuthorityDto login(@Validated @RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @PostMapping(ApiPaths.LOGOUT)
    @Operation(summary = "Выход пользователя", description = "Деавторизует пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный выход"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "403", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public void logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        userService.logout(logoutRequestDto);
    }

    @PostMapping(ApiPaths.REFRESH)
    @Operation(summary = "Обновление токена", description = "Обновляет refresh токен, используя старый refresh токен.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное обновление токена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "403", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public TokenDto refresh(@RequestParam String refreshToken) {
        return userService.refresh(refreshToken);
    }

    @PostMapping (ApiPaths.ACCESS)
    @Operation(summary = "Получение нового access токена", description = "Возвращает новый access токен используя refresh токен.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение access токена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public TokenDto getAccessToken(@RequestParam String refreshToken) {
        return userService.getAccessToken(refreshToken);
    }


}
