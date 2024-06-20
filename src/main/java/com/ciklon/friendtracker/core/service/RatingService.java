package com.ciklon.friendtracker.core.service;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import com.ciklon.friendtracker.api.dto.enums.RatingCalculationType;
import com.ciklon.friendtracker.api.dto.form.ContactInteractionDto;
import com.ciklon.friendtracker.api.dto.rating.*;
import com.ciklon.friendtracker.common.exception.CustomException;
import com.ciklon.friendtracker.common.exception.ExceptionType;
import com.ciklon.friendtracker.core.constant.RatingProps;
import com.ciklon.friendtracker.core.mapper.ContactInteractionMapper;
import com.ciklon.friendtracker.core.repository.ContactInteractionRepository;
import com.ciklon.friendtracker.core.repository.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final ContactInteractionRepository contactInteractionRepository;
    private final UserAnswerRepository userAnswerRepository;

    private final ContactInteractionMapper contactInteractionMapper;

    private final RatingProps ratingProps;

/*

Возможные доп условия для построения рейтинга:
- период взаимодействия
- период добавления
- варианты подсчета рейтинга: только формы, только ответы на вопросы, формы и ответы на вопросы
- фильтрация по типу взаимодействия
- период ОТСУТСТВИЯ взаимодействия (т.е. в зависимости от этого периода рейтинг будет уменьшаться относительно toDate)

Должна быть логика для:
- получение рейтинга всех пользователей без/с учетом доп условий
- получение рейтинга по конкретному контакту без/с учетом доп условий
- изменение констант для расчета рейтинга
- подсчет площади для графика рейтинга пользователей
- на основе минимального значения предлагать рекомендацию для улучшения рейтинга -> предлагать соответвующий вопрос


Дополнительно:
- можно сделать динамику взаимодействия с контактом (среди заполненных форм достать даты взаимодействия и построить
график)
 */


    /* рейтинг строится так:

    1. Берем contactInteraction и questionAnswer
    2. в зависимости от fieldType считаем сумму значений
       - если fieldType = ALL, то считаем сумму для каждого поля
     */

    public RatingPaginationResponse getContactsRating(
            UUID userId,
            int page,
            int size,
            LocalDate fromDate,
            LocalDate toDate,
            FieldType fieldType,
            RatingCalculationType ratingCalculationType
    ) {
        validateDates(fromDate, toDate);

        List<RatingDto> ratings =
                calculateRatings(userId, fromDate, toDate, fieldType, ratingCalculationType, page, size);

        return new RatingPaginationResponse(size, page, ratings.size(), ratings);
    }

    private List<RatingDto> calculateRatings(
            UUID userId,
            LocalDate fromDate,
            LocalDate toDate,
            FieldType fieldType,
            RatingCalculationType ratingCalculationType,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return switch (ratingCalculationType) {
            case FORMS -> calculateRatingsByForms(userId, fromDate, toDate, fieldType, pageable);
            case QUESTIONS -> calculateRatingsByQuestions(userId, fromDate, toDate, fieldType, pageable);
            case ALL -> calculateRatingsByAll(userId, fromDate, toDate, fieldType, pageable);
        };
    }

    private List<RatingDto> calculateRatingsByAll(
            UUID userId, LocalDate fromDate, LocalDate toDate, FieldType fieldType, Pageable pageable
    ) {

        Map<UUID, RatingDto> ratingsByForms = calculateRatingsByForms(userId, fromDate, toDate, fieldType, pageable)
                .stream()
                .collect(Collectors.toMap(RatingDto::getContactId, rating -> rating));

        Map<UUID, RatingDto> ratingsByQuestions =
                calculateRatingsByQuestions(userId, fromDate, toDate, fieldType, pageable)
                        .stream()
                        .collect(Collectors.toMap(RatingDto::getContactId, rating -> rating));

        ratingsByQuestions.forEach((contactId, ratingByQuestion) ->
                                           ratingsByForms.merge(
                                                   contactId,
                                                   ratingByQuestion,
                                                   this::mapToRatingDtoWithWeights
                                           ));

        return ratingsByForms.values().stream().toList();
    }


    private List<RatingDto> calculateRatingsByForms(
            UUID userId, LocalDate fromDate, LocalDate toDate, FieldType fieldType, Pageable pageable
    ) {
        List<ContactInteractionDto> contactInteractions =
                contactInteractionRepository.findAllByUserIdAndDateBetweenAndFieldType(
                                userId,
                                fromDate,
                                toDate,
                                pageable
                        )
                        .stream()
                        .map(contactInteractionMapper::map)
                        .toList();

        Map<UUID, List<ContactInteractionDto>> contactInteractionsMap =
                contactInteractions.stream().collect(Collectors.groupingBy(ContactInteractionDto::contactId));

        return contactInteractionsMap.entrySet()
                .stream()
                .map(entry -> collectRatingSumsForForms(entry.getKey(), entry.getValue(), fieldType))
                .map(this::mapToRatingDto)
                .toList();
    }


    private ContactInteractionsRatingDto collectRatingSumsForForms(
            UUID contactId,
            List<ContactInteractionDto> contactInteractions,
            FieldType fieldType
    ) {
        ContactInteractionsRatingDto ratingDto = new ContactInteractionsRatingDto(contactId);

        switch (fieldType) {
            case COMMUNICATION:
                contactInteractions.forEach(contactInteraction -> ratingDto.setCommunicationRatingSum(
                        ratingDto.getCommunicationRatingSum() + contactInteraction.communication()));
                break;

            case RESPECT:
                contactInteractions.forEach(contactInteraction -> ratingDto.setRespectRatingSum(
                        ratingDto.getRespectRatingSum() + contactInteraction.respect()));
                break;

            case TRUST:
                contactInteractions.forEach(contactInteraction -> ratingDto.setTrustRatingSum(
                        ratingDto.getTrustRatingSum() + contactInteraction.trust()));
                break;

            case EMPATHY:
                contactInteractions.forEach(contactInteraction -> ratingDto.setEmpathyRatingSum(
                        ratingDto.getEmpathyRatingSum() + contactInteraction.empathy()));
                break;

            case TIME:
                contactInteractions.forEach(contactInteraction -> ratingDto.setTimeRatingSum(
                        ratingDto.getTimeRatingSum() + contactInteraction.time()));
                break;

            case ALL:
                contactInteractions.forEach(contactInteraction -> {
                    ratingDto.setInteractionCount(ratingDto.getInteractionCount() + 1);
                    ratingDto.setCommunicationRatingSum(
                            ratingDto.getCommunicationRatingSum() + contactInteraction.communication());
                    ratingDto.setRespectRatingSum(ratingDto.getRespectRatingSum() + contactInteraction.respect());
                    ratingDto.setTrustRatingSum(ratingDto.getTrustRatingSum() + contactInteraction.trust());
                    ratingDto.setEmpathyRatingSum(ratingDto.getEmpathyRatingSum() + contactInteraction.empathy());
                    ratingDto.setTimeRatingSum(ratingDto.getTimeRatingSum() + contactInteraction.time());
                });
                break;
            default:
                throw new CustomException(
                        ExceptionType.ILLEGAL,
                        "При подсчете рейтинга форм получен некорректный тип поля"
                );
        }

        return ratingDto;
    }

    private List<RatingDto> calculateRatingsByQuestions(
            UUID userId, LocalDate fromDate, LocalDate toDate, FieldType fieldType, Pageable pageable
    ) {
        List<UserAnswerForCalculationDto> userAnswersForCalculation = userAnswerRepository
                .findAllByUserIdAndDateBetweenAndFieldType(userId, fromDate, toDate, fieldType, pageable);

        Map<UUID, List<UserAnswerForCalculationDto>> userAnswersForCalculationMap =
                userAnswersForCalculation.stream()
                        .collect(Collectors.groupingBy(UserAnswerForCalculationDto::contactId));

        return userAnswersForCalculationMap.entrySet()
                .stream()
                .map(entry -> collectRatingSumsByQuestionAnswers(entry.getKey(), entry.getValue(), fieldType))
                .map(this::mapToRatingDto)
                .toList();
    }


    private QuestionAnswersRatingDto collectRatingSumsByQuestionAnswers(
            UUID contactId,
            List<UserAnswerForCalculationDto> userAnswersForCalculation,
            FieldType fieldType
    ) {
        QuestionAnswersRatingDto ratingDto = new QuestionAnswersRatingDto(contactId);

        switch (fieldType) {
            case COMMUNICATION:
                userAnswersForCalculation.forEach(userAnswerForCalculation -> ratingDto.setCommunicationRatingSum(
                        ratingDto.getCommunicationRatingSum() + (userAnswerForCalculation.isPositive() ? 1 : -1)));
                break;

            case RESPECT:
                userAnswersForCalculation.forEach(userAnswerForCalculation -> ratingDto.setRespectRatingSum(
                        ratingDto.getRespectRatingSum() + (userAnswerForCalculation.isPositive() ? 1 : -1)));
                break;

            case TRUST:
                userAnswersForCalculation.forEach(userAnswerForCalculation -> ratingDto.setTrustRatingSum(
                        ratingDto.getTrustRatingSum() + (userAnswerForCalculation.isPositive() ? 1 : -1)));
                break;

            case EMPATHY:
                userAnswersForCalculation.forEach(userAnswerForCalculation -> ratingDto.setEmpathyRatingSum(
                        ratingDto.getEmpathyRatingSum() + (userAnswerForCalculation.isPositive() ? 1 : -1)));
                break;

            default:
                throw new CustomException(
                        ExceptionType.ILLEGAL,
                        "При подсчете рейтинга ответов на вопросы получен некорректный тип поля"
                );
        }

        return ratingDto;
    }

    private RatingDto mapToRatingDto(QuestionAnswersRatingDto dto) {
        int questionAnswerCount = dto.getQuestionAnswerCount();
        return new RatingDto(
                dto.getContactId(),
                RatingCalculationType.QUESTIONS,
                0,
                questionAnswerCount,
                (double) dto.getRespectRatingSum() / questionAnswerCount,
                (double) dto.getTrustRatingSum() / questionAnswerCount,
                (double) dto.getEmpathyRatingSum() / questionAnswerCount,
                (double) dto.getQuestionAnswerCount() / questionAnswerCount,
                (double) dto.getTimeRatingSum() / questionAnswerCount
        );
    }

    private RatingDto mapToRatingDto(ContactInteractionsRatingDto dto) {
        int interactionCount = dto.getInteractionCount();
        return new RatingDto(
                dto.getContactId(),
                RatingCalculationType.FORMS,
                interactionCount,
                0,
                (double) dto.getRespectRatingSum() / interactionCount,
                (double) dto.getTrustRatingSum() / interactionCount,
                (double) dto.getEmpathyRatingSum() / interactionCount,
                (double) dto.getCommunicationRatingSum() / interactionCount,
                (double) dto.getTimeRatingSum() / interactionCount
        );
    }

    private RatingDto mapToRatingDtoWithWeights(RatingDto first, RatingDto second) {
        return new RatingDto(
                first.getContactId(),
                RatingCalculationType.ALL,
                first.getInteractionCount() + second.getInteractionCount(),
                first.getQuestionAnswerCount() + second.getQuestionAnswerCount(),
                calculateWeightedValue(first.getTimeRating(), second.getTimeRating(), first.getInteractionCount(),
                                       second.getQuestionAnswerCount()
                ),
                calculateWeightedValue(first.getCommunicationRating(), second.getCommunicationRating(),
                                       first.getInteractionCount(), second.getQuestionAnswerCount()
                ),
                calculateWeightedValue(first.getRespectRating(), second.getRespectRating(), first.getInteractionCount(),
                                       second.getQuestionAnswerCount()
                ),
                calculateWeightedValue(first.getTrustRating(), second.getTrustRating(), first.getInteractionCount(),
                                       second.getQuestionAnswerCount()
                ),
                calculateWeightedValue(first.getEmpathyRating(), second.getEmpathyRating(), first.getInteractionCount(),
                                       second.getQuestionAnswerCount()
                )
        );
    }

    private double calculateWeightedValue(
            double interactionRating,
            double answerRating,
            int interactionCount,
            int answerCount
    ) {
        double weightedValue = (interactionRating * ratingProps.getInteractionWeight() +
                        answerRating * ratingProps.getAnswerWeight()) / (interactionCount + answerCount);
        return Math.max(ratingProps.getMinRating(), Math.min(ratingProps.getMaxRating(), weightedValue));
    }

    private void validateDates(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && fromDate.isAfter(LocalDate.now())) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "Дата начала периода не может быть в будущем");
        }
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new CustomException(
                    ExceptionType.BAD_REQUEST,
                    "Дата начала периода не может быть позже даты окончания"
            );
        }
    }
}
