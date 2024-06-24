package com.ciklon.friendtracker.core.service;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import com.ciklon.friendtracker.api.dto.enums.RatingCalculationType;
import com.ciklon.friendtracker.api.dto.form.ContactInteractionDto;
import com.ciklon.friendtracker.api.dto.form.ExtendedContactInteractionDto;
import com.ciklon.friendtracker.api.dto.rating.*;
import com.ciklon.friendtracker.common.exception.CustomException;
import com.ciklon.friendtracker.common.exception.ExceptionType;
import com.ciklon.friendtracker.core.constant.RatingProps;
import com.ciklon.friendtracker.core.mapper.RatingMapper;
import com.ciklon.friendtracker.core.repository.ContactInteractionRepository;
import com.ciklon.friendtracker.core.repository.UserAnswerRepository;
import com.ciklon.friendtracker.core.service.integration.ContactIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingCalculationService {

    private final int METRIC_COUNT = 5;
    private final ContactInteractionRepository contactInteractionRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final RatingMapper ratingMapper;
    private final RatingProps ratingProps;
    private final ContactIntegrationService contactIntegrationService;

    public List<RatingDto> calculateRatings(
            UUID userId,
            LocalDate fromDate,
            LocalDate toDate,
            FieldType fieldType,
            RatingCalculationType ratingCalculationType,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return switch (ratingCalculationType) {
            case FORMS -> calculateRatingsByForms(userId, fromDate, toDate, fieldType, pageable);
            case QUESTIONS -> calculateRatingsByQuestions(userId, fieldType, pageable);
            case ALL -> calculateRatingsByAll(userId, fromDate, toDate, fieldType, pageable);
        };
    }

    public RatingDto getRatingByContactId(
            UUID userId,
            UUID contactId,
            LocalDate fromDate,
            LocalDate toDate,
            FieldType fieldType,
            RatingCalculationType ratingCalculationType
    ) {
        return switch (ratingCalculationType) {
            case FORMS -> getContactRatingByForms(userId, contactId, fromDate, toDate, fieldType);
            case QUESTIONS -> getContactRatingByQuestions(userId, contactId, fieldType);
            case ALL -> getContactRatingByAll(userId, contactId, fromDate, toDate, fieldType);
        };
    }

    public AverageRatingPaginationResponse getAverageContactsRating(
            UUID userId,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<AverageRatingDto> averageRatings = new ArrayList<>(calculateAverageRatings(userId, pageable));

        List<UUID> contactIds = contactIntegrationService.getContactsByUserId(userId);
        List<AverageRatingDto> finalAverageRatings = averageRatings;
        List<AverageRatingDto> blankRatings = contactIds.stream()
                .filter(contactId -> finalAverageRatings.stream().noneMatch(rating -> rating.getContactId().equals(contactId)))
                .map(AverageRatingDto::new)
                .toList();
        averageRatings.addAll(blankRatings);
        int totalPage = averageRatings.size() / size + 1;
        averageRatings = averageRatings.stream()
                .skip((long) size * (page - 1))
                .limit(size)
                .toList();
        return new AverageRatingPaginationResponse(size, page, totalPage, averageRatings);
    }

    private List<RatingDto> calculateRatingsByForms(
            UUID userId, LocalDate fromDate, LocalDate toDate, FieldType fieldType, Pageable pageable
    ) {
        List<ContactInteractionDto> contactInteractions =
                contactInteractionRepository.findAllByUserIdAndDateBetween(
                        userId,
                        fromDate,
                        toDate,
                        pageable
                );

        Map<UUID, List<ContactInteractionDto>> contactInteractionsMap =
                contactInteractions.stream().collect(Collectors.groupingBy(ContactInteractionDto::contactId));

        return contactInteractionsMap.entrySet().stream()
                .map(entry -> collectRatingSumsForForms(entry.getKey(), entry.getValue(), fieldType))
                .map(ratingMapper::mapToRatingDto).toList();
    }


    private ContactInteractionsRatingDto collectRatingSumsForForms(
            UUID contactId, List<ContactInteractionDto> contactInteractions, FieldType fieldType
    ) {
        ContactInteractionsRatingDto ratingDto = new ContactInteractionsRatingDto(contactId);

        switch (fieldType) {
            case COMMUNICATION:
                contactInteractions.forEach(dto -> ratingDto.setCommunicationRatingSum(
                        ratingDto.getCommunicationRatingSum() + dto.communication()));
                break;
            case RESPECT:
                contactInteractions.forEach(dto -> ratingDto.setRespectRatingSum(
                        ratingDto.getRespectRatingSum() + dto.respect()));
                break;
            case TRUST:
                contactInteractions.forEach(dto -> ratingDto.setTrustRatingSum(
                        ratingDto.getTrustRatingSum() + dto.trust()));
                break;
            case EMPATHY:
                contactInteractions.forEach(dto -> ratingDto.setEmpathyRatingSum(
                        ratingDto.getEmpathyRatingSum() + dto.empathy()));
                break;
            case TIME:
                contactInteractions.forEach(dto -> ratingDto.setTimeRatingSum(
                        ratingDto.getTimeRatingSum() + dto.time()));
                break;
            case ALL:
                contactInteractions.forEach(dto -> {
                    ratingDto.setInteractionCount(ratingDto.getInteractionCount() + 1);
                    ratingDto.setCommunicationRatingSum(ratingDto.getCommunicationRatingSum() + dto.communication());
                    ratingDto.setRespectRatingSum(ratingDto.getRespectRatingSum() + dto.respect());
                    ratingDto.setTrustRatingSum(ratingDto.getTrustRatingSum() + dto.trust());
                    ratingDto.setEmpathyRatingSum(ratingDto.getEmpathyRatingSum() + dto.empathy());
                    ratingDto.setTimeRatingSum(ratingDto.getTimeRatingSum() + dto.time());
                });
                break;
        }

        return ratingDto;
    }

    private List<RatingDto> calculateRatingsByQuestions(
            UUID userId, FieldType fieldType, Pageable pageable
    ) {
        List<UserAnswerForCalculationDto> userAnswersForCalculation =
                userAnswerRepository.findAllByUserIdAndDateBetweenAndFieldType(userId, fieldType, pageable);

        Map<UUID, List<UserAnswerForCalculationDto>> userAnswersForCalculationMap = userAnswersForCalculation.stream()
                .collect(Collectors.groupingBy(UserAnswerForCalculationDto::contactId));

        return userAnswersForCalculationMap.entrySet().stream()
                .map(entry -> collectRatingSumsByQuestionAnswers(entry.getKey(), entry.getValue(), fieldType))
                .map(ratingMapper::mapToRatingDto).toList();
    }

    private QuestionAnswersRatingDto collectRatingSumsByQuestionAnswers(
            UUID contactId, List<UserAnswerForCalculationDto> userAnswersForCalculation, FieldType fieldType
    ) {
        QuestionAnswersRatingDto ratingDto = new QuestionAnswersRatingDto(contactId);
        if (userAnswersForCalculation == null || userAnswersForCalculation.isEmpty()) {
            return ratingDto;
        }
        switch (fieldType) {
            case COMMUNICATION:
                userAnswersForCalculation.forEach(dto -> ratingDto.setCommunicationRatingSum(
                        ratingDto.getCommunicationRatingSum() + dto.value()));
                break;
            case RESPECT:
                userAnswersForCalculation.forEach(dto -> ratingDto.setRespectRatingSum(
                        ratingDto.getRespectRatingSum() + dto.value()));
                break;
            case TRUST:
                userAnswersForCalculation.forEach(dto -> ratingDto.setTrustRatingSum(
                        ratingDto.getTrustRatingSum() + dto.value()));
                break;
            case EMPATHY:
                userAnswersForCalculation.forEach(dto -> ratingDto.setEmpathyRatingSum(
                        ratingDto.getEmpathyRatingSum() + dto.value()));
                break;
            case TIME:
                userAnswersForCalculation.forEach(dto -> ratingDto.setTimeRatingSum(
                        ratingDto.getTimeRatingSum() + dto.value()));
                break;
            case ALL:
                userAnswersForCalculation.forEach(dto -> {
                    ratingDto.setQuestionAnswerCount(ratingDto.getQuestionAnswerCount() + 1);
                    ratingDto.setCommunicationRatingSum(ratingDto.getCommunicationRatingSum() + dto.value());
                    ratingDto.setRespectRatingSum(ratingDto.getRespectRatingSum() + dto.value());
                    ratingDto.setTrustRatingSum(ratingDto.getTrustRatingSum() + dto.value());
                    ratingDto.setEmpathyRatingSum(ratingDto.getEmpathyRatingSum() + dto.value());
                    ratingDto.setTimeRatingSum(ratingDto.getTimeRatingSum() + dto.value());
                });
                break;
            default:
                throw new CustomException(
                        ExceptionType.ILLEGAL,
                        "При подсчете рейтинга ответов на вопросы получен некорректный тип поля"
                );
        }

        return ratingDto;
    }

    private List<RatingDto> calculateRatingsByAll(
            UUID userId, LocalDate fromDate, LocalDate toDate, FieldType fieldType, Pageable pageable
    ) {
        Map<UUID, RatingDto> ratingsByForms =
                calculateRatingsByForms(userId, fromDate, toDate, fieldType, pageable).stream()
                        .collect(Collectors.toMap(RatingDto::getContactId, rating -> rating));

        Map<UUID, RatingDto> ratingsByQuestions = calculateRatingsByQuestions(userId, fieldType, pageable).stream()
                .collect(Collectors.toMap(RatingDto::getContactId, rating -> rating));

        ratingsByQuestions.forEach((contactId, ratingByQuestion) -> ratingsByForms.merge(
                contactId,
                ratingByQuestion,
                ratingMapper::mapToRatingDtoWithWeights
        ));

        return ratingsByForms.values().stream().peek(rating -> rating.setCalculationType(RatingCalculationType.ALL))
                .toList();
    }

    private RatingDto getContactRatingByAll(
            UUID userId,
            UUID contactId,
            LocalDate fromDate,
            LocalDate toDate,
            FieldType fieldType
    ) {
        RatingDto ratingByForms = getContactRatingByForms(userId, contactId, fromDate, toDate, fieldType);
        RatingDto ratingByQuestions = getContactRatingByQuestions(userId, contactId, fieldType);

        return ratingMapper.mapToRatingDtoWithWeights(ratingByForms, ratingByQuestions);
    }

    private RatingDto getContactRatingByQuestions(UUID userId, UUID contactId, FieldType fieldType) {
        List<UserAnswerForCalculationDto> userAnswersForCalculation =
                userAnswerRepository.findAllByUserIdAndContactIdAndFieldType(userId, contactId, fieldType);

        if (userAnswersForCalculation == null || userAnswersForCalculation.isEmpty()) {
            return new RatingDto(contactId, RatingCalculationType.QUESTIONS, 0, 0, 0, 0, 0, 0, 0);
        }

        return ratingMapper.mapToRatingDto(collectRatingSumsByQuestionAnswers(
                contactId,
                userAnswersForCalculation,
                fieldType
        ));
    }

    private RatingDto getContactRatingByForms(
            UUID userId, UUID contactId, LocalDate fromDate, LocalDate toDate, FieldType fieldType
    ) {
        List<ContactInteractionDto> contactInteractions =
                contactInteractionRepository.findAllByUserIdAndContactIdAndDateBetween(
                        userId,
                        contactId,
                        fromDate,
                        toDate
                );

        if (contactInteractions.isEmpty()) {
            return new RatingDto(contactId, RatingCalculationType.FORMS, 0, 0, 0, 0, 0, 0, 0);
        }

        return ratingMapper.mapToRatingDto(collectRatingSumsForForms(contactId, contactInteractions, fieldType));
    }

    private List<AverageRatingDto> calculateAverageRatings(UUID userId, Pageable pageable) {
        List<ExtendedContactInteractionDto> contactInteractions =
                contactInteractionRepository.findAllByUserId(userId);

        List<UserAnswerForCalculationDto> userAnswersForCalculation =
                userAnswerRepository.findAllByUserId(userId);

        Map<UUID, List<ExtendedContactInteractionDto>> contactInteractionsMap =
                contactInteractions.stream().collect(Collectors.groupingBy(ExtendedContactInteractionDto::contactId));

        Map<UUID, List<UserAnswerForCalculationDto>> userAnswersForCalculationMap =
                userAnswersForCalculation.stream()
                        .collect(Collectors.groupingBy(UserAnswerForCalculationDto::contactId));

        return contactInteractionsMap.entrySet().stream()
                .map(entry -> calculateAverageRating(entry.getKey(), entry.getValue(),
                                                     userAnswersForCalculationMap.get(entry.getKey())
                ))
                .sorted((first, second) -> Double.compare(second.getAverageRating(), first.getAverageRating()))
                .toList()
                ;
    }

    private AverageRatingDto calculateAverageRating(
            UUID contactId,
            List<ExtendedContactInteractionDto> contactInteractions,
            List<UserAnswerForCalculationDto> userAnswersForCalculation
    ) {
        AverageRatingDto averageRatingDto = new AverageRatingDto(contactId);
        if (contactInteractions == null) {
            contactInteractions = List.of();
        }
        if (userAnswersForCalculation == null) {
            userAnswersForCalculation = List.of();
        }

        if (contactInteractions.isEmpty() && userAnswersForCalculation.isEmpty()) {
            return averageRatingDto;
        }

        contactInteractions.sort((first, second) -> second.date().compareTo(first.date()));
        ExtendedContactInteractionDto lastInteraction = contactInteractions.removeLast();

        int interactionCount = contactInteractions.size();
        double interactionWeightedSums = calculateWeightedRatingSumByInteractions(contactInteractions); // сумма всех взвешенных оценок по взаимодействиям
        double userAnswerWeightedSums = calculateWeightedUserAnswerSum(userAnswersForCalculation); // сумма всех взвешенных оценок по ответам на вопросы
        double lastInteractionWeightedSum = calculateWeightedLastInteractionSum(lastInteraction); // взвешенная оценка последнего взаимодействия

        double totalWeightedSum = calculateTotalWeightedSum(interactionWeightedSums, userAnswerWeightedSums); // общая сумма взвешенных оценок
        double totalWeightedCount = calculateTotalWeightedCount(interactionCount, userAnswersForCalculation.size()); // общее количество взвешенных оценок

        double oldAverageRating = calculateOldAverageRating(totalWeightedCount, totalWeightedSum);
        double newAverageRating = calculateNewAverageRating(totalWeightedCount, totalWeightedSum, lastInteractionWeightedSum);

        averageRatingDto.setOldAverageRating(oldAverageRating);
        averageRatingDto.setAverageRating(newAverageRating);
        averageRatingDto.setContactId(contactId);
        averageRatingDto.setRatingIncreased(newAverageRating > oldAverageRating);
        if (lastInteraction != null) {
            averageRatingDto.setLastInteractionDate(lastInteraction.date());
        }
        return averageRatingDto;
    }

    private double calculateWeightedLastInteractionSum(ExtendedContactInteractionDto lastInteraction) {
        if (lastInteraction == null) {
            return 0;
        }

        return (lastInteraction.communication() + lastInteraction.respect() + lastInteraction.trust() +
                lastInteraction.empathy() + lastInteraction.time()) * ratingProps.getInteractionWeight();
    }


    private double calculateTotalWeightedSum(
            double interactionSums,
            double userAnswersSums
    ) {
        return interactionSums + userAnswersSums;
    }

    private double calculateTotalWeightedCount(
            int interactionCount,
            int userAnswerCount
    ) {
        return interactionCount * ratingProps.getInteractionWeight() + userAnswerCount * ratingProps.getAnswerWeight();
    }

    private double calculateWeightedUserAnswerSum(List<UserAnswerForCalculationDto> userAnswersForCalculation) {
        if (userAnswersForCalculation == null || userAnswersForCalculation.isEmpty()) {
            return 0;
        }
        return userAnswersForCalculation.stream().mapToDouble(UserAnswerForCalculationDto::value).sum() *
                ratingProps.getAnswerWeight();
    }

    private double calculateOldAverageRating(
            double totalWeightedCount,
            double totalWeightedSum
    ) {
        return totalWeightedCount == 0 ? 0 : totalWeightedSum / (totalWeightedCount * METRIC_COUNT);
    }

    private double calculateNewAverageRating(
            double totalWeightedCount,
            double totalWeightedSum,
            double weightedLastInteractionSum
    ) {
        if (weightedLastInteractionSum == 0) {
            return calculateOldAverageRating(totalWeightedCount, totalWeightedSum);
        }
        if (totalWeightedCount == 0) {
            return 0;
        }

        double delimiter = (totalWeightedCount + ratingProps.getInteractionWeight()) * METRIC_COUNT;
        return (totalWeightedSum + weightedLastInteractionSum) / delimiter;
    }


    private double calculateWeightedRatingSumByInteractions(List<ExtendedContactInteractionDto> contactInteractions) {
        double communicationRatingSum = 0;
        double respectRatingSum = 0;
        double trustRatingSum = 0;
        double empathyRatingSum = 0;
        double timeRatingSum = 0;

        for (ExtendedContactInteractionDto interaction : contactInteractions) {
            communicationRatingSum += interaction.communication();
            respectRatingSum += interaction.respect();
            trustRatingSum += interaction.trust();
            empathyRatingSum += interaction.empathy();
            timeRatingSum += interaction.time();
        }

        return (communicationRatingSum + respectRatingSum + trustRatingSum + empathyRatingSum + timeRatingSum)
                * ratingProps.getInteractionWeight();
    }


}
