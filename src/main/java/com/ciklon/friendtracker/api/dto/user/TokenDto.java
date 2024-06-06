package com.ciklon.friendtracker.api.dto.user;


public record TokenDto(
        String accessToken,
        String refreshToken
) {
}
