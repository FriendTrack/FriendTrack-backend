package com.ciklon.friendtracker.api.controller;

import com.ciklon.friendtracker.api.constant.ApiPaths;
import com.ciklon.friendtracker.api.dto.enums.FieldType;
import com.ciklon.friendtracker.api.dto.enums.RatingCalculationType;
import com.ciklon.friendtracker.api.dto.rating.RatingPaginationResponse;
import com.ciklon.friendtracker.core.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Rating", description = "API для управления рейтингом контактов")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping(ApiPaths.RATING)
    @Operation(summary = "Получение рейтинга всех контактов", description = "Получение рейтинга контакта.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение рейтинга"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public RatingPaginationResponse getRating(
            @RequestParam(value = "page", defaultValue = "1") int page,

            @RequestParam(value = "size", defaultValue = "10") int size,

            @Schema(description = "Дата начала периода", example = "2021-01-01")
            @RequestParam(value = "fromDate", required = false) LocalDate fromDate,

            @Schema(description = "Дата окончания периода", example = "2021-01-01")
            @RequestParam(value = "toDate", required = false) LocalDate toDate,

            @Schema(description = """
                    Тип поля (если выбрать, например, TIME и выбрать calculationType = FORMS, то другие поля будут равны 0)""",
                    example = "ALL",
                    allowableValues = {"TIME", "EMPATHY", "TRUST", "COMMUNICATION", "RESPECT", "ALL"})
            @RequestParam(value = "fieldType", required = false, defaultValue = "ALL"
            ) FieldType fieldType,

            @Schema(description = "Тип расчета рейтинга", example = "ALL", allowableValues = {"FORMS", "QUESTIONS", "ALL"})
            @RequestParam(value = "calculationType", required = false, defaultValue = "ALL")
                    RatingCalculationType calculationType

    ) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;
        return ratingService.getContactsRating(userId, page, size, fromDate, toDate, fieldType, calculationType);
    }

//    @GetMapping(ApiPaths.RATING_BY_CONTACT_ID)
//    @Operation(summary = "Получение рейтинга контакта", description = "Получение рейтинга контакта.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Успешное получение рейтинга"),
//            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
//            @ApiResponse(responseCode = "404", description = "Контакт не найден"),
//            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
//    })
//    public RatingDto getRatingByContactId(@PathVariable("id") String contactId) {
//        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return ratingService.getRatingByContactId(contactId, userId);
//    }
}
