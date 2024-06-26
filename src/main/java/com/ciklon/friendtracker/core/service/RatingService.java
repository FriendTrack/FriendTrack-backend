package com.ciklon.friendtracker.core.service;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import com.ciklon.friendtracker.api.dto.enums.PeriodType;
import com.ciklon.friendtracker.api.dto.enums.RatingCalculationType;
import com.ciklon.friendtracker.api.dto.rating.CalculatedRatingDto;
import com.ciklon.friendtracker.api.dto.rating.RatingDto;
import com.ciklon.friendtracker.api.dto.rating.RatingPaginationResponse;
import com.ciklon.friendtracker.api.dto.recommendation.RecommendationDto;
import com.ciklon.friendtracker.common.DataValidator;
import com.ciklon.friendtracker.core.repository.RecommendationRepository;
import com.ciklon.friendtracker.core.service.integration.ContactIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final DataValidator dataValidator;
    private final RatingCalculationService ratingCalculationService;
    private final ContactIntegrationService contactIntegrationService;
    private final RecommendationRepository recommendationRepository;

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
        List<RatingDto> ratings = new ArrayList<>(ratingCalculationService.calculateRatings(userId, fromDate, toDate, fieldType, ratingCalculationType, page, size));

        List<UUID> contactIds = contactIntegrationService.getContactsByUserIdAndToDate(userId, toDate.atStartOfDay());
        List<RatingDto> finalRatings = ratings;
        List<RatingDto> emptyRatings = contactIds.stream()
                .filter(contactId -> finalRatings.stream().noneMatch(rating -> rating.getContactId().equals(contactId)))
                .map(contactId -> new RatingDto(contactId, ratingCalculationType, 0, 0, 0, 0, 0, 0, 0))
                .toList();
        if (!emptyRatings.isEmpty()) {
            ratings.addAll(emptyRatings);
        }
        int totalPage = ratings.size() / size + 1;
        ratings = ratings.stream()
                .skip((long) (page - 1) * size)
                .limit(size)
                .toList();
        return new RatingPaginationResponse(size, page, totalPage, ratings);
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

    public List<CalculatedRatingDto> getCalculatedRatingsGraph(UUID userId, UUID contactId, PeriodType periodType) {
        contactIntegrationService.existsContact(userId, contactId);
        List<CalculatedRatingDto> calculatedRatingDtos = ratingCalculationService.getCalculatedRatings(
                userId,
                contactId,
                periodType
        );

        calculatedRatingDtos.forEach(calculatedRatingDto -> {
            RecommendationDto recommendationDto =
                    recommendationRepository.findByFieldType(getFieldTypeWithMinRating(calculatedRatingDto));
            calculatedRatingDto.setTitle(recommendationDto.title());
            calculatedRatingDto.setDescription(recommendationDto.description());
        });

        return calculatedRatingDtos;
    }

    private FieldType getFieldTypeWithMinRating(CalculatedRatingDto calculatedRatingDto) {
        double minRating = calculatedRatingDto.getTimeRating();
        FieldType fieldType = FieldType.TIME;

        if (calculatedRatingDto.getCommunicationRating() < minRating) {
            minRating = calculatedRatingDto.getCommunicationRating();
            fieldType = FieldType.COMMUNICATION;
        }
        if (calculatedRatingDto.getRespectRating() < minRating) {
            minRating = calculatedRatingDto.getRespectRating();
            fieldType = FieldType.RESPECT;
        }
        if (calculatedRatingDto.getTrustRating() < minRating) {
            minRating = calculatedRatingDto.getTrustRating();
            fieldType = FieldType.TRUST;
        }
        if (calculatedRatingDto.getEmpathyRating() < minRating) {
            fieldType = FieldType.EMPATHY;
        }

        return fieldType;
    }
}
