package com.ciklon.friendtracker.api.dto.user;

import java.util.UUID;

public record LogoutRequestDto(
    UUID userId
) {
}
