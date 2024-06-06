package com.ciklon.friendtracker.api.dto.contact;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public record UpdateContactDto(
        @NotEmpty
        String name,
        String details,
        String link,
        LocalDate birthDate
) {
}
