package com.ciklon.friendtracker.api.dto;

import java.util.UUID;

public record LogoutRequestDto(
    UUID userId
) {
}
