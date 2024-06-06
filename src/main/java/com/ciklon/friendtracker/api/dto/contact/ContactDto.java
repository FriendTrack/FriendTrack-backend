package com.ciklon.friendtracker.api.dto.contact;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

public record ContactDto(
        @Schema(description = "Идентификатор контакта")
        UUID id,

        @Schema(description = "Имя контакта")
        String name,

        @Schema(description = "Дополнительные детали о контакте")
        String details,

        @Schema(description = "Ссылка на профиль или ресурс, связанный с контактом")
        String link,

        @Schema(description = "Дата рождения контакта")
        LocalDate birthDate,

        @Schema(description = "Идентификатор пользователя, которому принадлежит контакт")
        UUID userId
) {
}
