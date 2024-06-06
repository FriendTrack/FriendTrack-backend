package com.ciklon.friendtracker.api.dto.contact;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public record ContactCreationDto(
        @NotEmpty
        @Schema(description = "Имя контакта, обязательное поле")
        String name,

        @Schema(description = "Дополнительные детали о контакте")
        String details,

        @Schema(description = "Ссылка на профиль или ресурс, связанный с контактом")
        String link,

        @Schema(description = "Дата рождения контакта")
        LocalDate birthDate
) {
}
