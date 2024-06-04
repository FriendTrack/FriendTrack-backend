package com.ciklon.friendtracker.api.dto;


public record TokenDto(
        String accessToken,
        String refreshToken
) {
}
