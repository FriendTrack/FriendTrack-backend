package com.ciklon.friendtracker.api.dto;

import lombok.Builder;

import java.util.UUID;


@Builder
public record UserDto(
    UUID id,
    String username,
    String email,
    String login
) {
}