package com.ciklon.friendtracker.api.dto.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CalculatedRatingDto {

        @Schema(description = "Идентификатор контакта", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID contactId;

        @Schema(description = "Количество взаимодействий", example = "5")
        int interactionCount;

        @Schema(description = "Количество ответов на вопросы", example = "3")
        int questionAnswerCount;

        @Schema(description = "Количество взаимодействий", example = "5")
        double averageRating;

        @Schema(description = "Рейтинг времяпровождения", example = "0.5")
        double timeRating;

        @Schema(description = "Рейтинг коммуникации", example = "0.5")
        double communicationRating;

        @Schema(description = "Рейтинг уважения", example = "0.5")
        double respectRating;

        @Schema(description = "Рейтинг доверия", example = "0.5")
        double trustRating;

        @Schema(description = "Рейтинг эмпатии", example = "0.5")
        double empathyRating;

        @Schema(description = "Дата последнего взаимодействия", example = "2021-12-31")
        LocalDate lastInteractionDate;

        @Schema(description = "Заголовок рекомендация взаимодействий", example = "Больше времени проводите вместе")
        String title;

        @Schema(description = "Описание рекомендации", example = "* тут какая-то рекомендация *")
        String description;

        public CalculatedRatingDto(UUID contactId, LocalDate date) {
            this.contactId = contactId;
            this.interactionCount = 0;
            this.questionAnswerCount = 0;
            this.averageRating = 0.0;
            this.timeRating = 0.0;
            this.communicationRating = 0.0;
            this.respectRating = 0.0;
            this.trustRating = 0.0;
            this.empathyRating = 0.0;
            this.lastInteractionDate = date;
        }
}
