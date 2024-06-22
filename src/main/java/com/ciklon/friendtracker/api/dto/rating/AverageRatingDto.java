package com.ciklon.friendtracker.api.dto.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AverageRatingDto{
        @Schema(description = "Идентификатор пользователя", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID contactId;

        @Schema(description = "Средний рейтинг", example = "0.5")
        double averageRating;

        @Schema(description = "Средний рейтинг до обновления", example = "0.5")
        double oldAverageRating;

        @Schema(description = "Дата последнего взаимодействия", example = "2021-12-31")
        LocalDate lastInteractionDate;

        @Schema(description = "Флаг изменения рейтинга", example = "true")
        boolean isRatingIncreased;

        public AverageRatingDto(UUID contactId) {
            this.contactId = contactId;

            averageRating = 0.0;
            oldAverageRating = 0.0;
            lastInteractionDate = null;
            isRatingIncreased = false;
        }
}
