package com.ciklon.friendtracker.api.dto;


import lombok.Builder;

import java.util.UUID;

@Builder
public record JwtAuthorityDto(
        UUID userId,
        String accessToken,
        String refreshToken
) {
}
