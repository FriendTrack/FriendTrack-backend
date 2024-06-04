package com.ciklon.friendtracker.api.controller;

import com.ciklon.friendtracker.api.constant.ApiPaths;
import com.ciklon.friendtracker.api.dto.*;
import com.ciklon.friendtracker.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(ApiPaths.REGISTER)
    public JwtAuthorityDto register(@RequestBody RegistrationRequestDto registrationRequestDto) {
        return userService.register(registrationRequestDto);
    }

    @PostMapping(ApiPaths.LOGIN)
    public JwtAuthorityDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @PostMapping(ApiPaths.LOGOUT)
    public void logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        userService.logout(logoutRequestDto);
    }

    @PostMapping(ApiPaths.REFRESH)
    public TokenDto refresh(@RequestBody TokenDto tokenDto) {
        return userService.refresh(tokenDto.refreshToken());
    }

    @PostMapping(ApiPaths.ACCESS)
    public TokenDto getAccessToken(@RequestBody TokenDto tokenDto) {
        return userService.getAccessToken(tokenDto.refreshToken());
    }


}
