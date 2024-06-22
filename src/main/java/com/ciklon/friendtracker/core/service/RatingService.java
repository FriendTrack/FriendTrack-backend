package com.ciklon.friendtracker.core.service;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import com.ciklon.friendtracker.api.dto.enums.RatingCalculationType;
import com.ciklon.friendtracker.api.dto.rating.AverageRatingPaginationResponse;
import com.ciklon.friendtracker.api.dto.rating.RatingDto;
import com.ciklon.friendtracker.api.dto.rating.RatingPaginationResponse;
import com.ciklon.friendtracker.common.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final DataValidator dataValidator;
    private final RatingCalculationService ratingCalculationService;

    public RatingPaginationResponse getContactsRating(
            UUID userId,
            int page,
            int size,
            LocalDate fromDate,
            LocalDate toDate,
            FieldType fieldType,
            RatingCalculationType ratingCalculationType
    ) {
        dataValidator.validateDates(fromDate, toDate);
        List<RatingDto> ratings = ratingCalculationService.calculateRatings(userId, fromDate, toDate, fieldType,
                                                                            ratingCalculationType, page, size
        );
        return new RatingPaginationResponse(size, page, ratings.size() / size + 1, ratings);
    }

    public RatingDto getRatingByContactId(
            UUID userId, UUID contactId, LocalDate fromDate, LocalDate toDate,
            FieldType fieldType, RatingCalculationType ratingCalculationType
    ) {
        dataValidator.validateDates(fromDate, toDate);
        return ratingCalculationService.getRatingByContactId(
                userId,
                contactId,
                fromDate,
                toDate,
                fieldType,
                ratingCalculationType
        );
    }
}
