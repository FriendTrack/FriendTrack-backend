package com.ciklon.friendtracker.core.service;

import com.ciklon.friendtracker.api.dto.PaginationResponse;
import com.ciklon.friendtracker.api.dto.question.*;
import com.ciklon.friendtracker.api.dto.rating.UserAnswerCreationDto;
import com.ciklon.friendtracker.api.dto.rating.UserAnswerDto;
import com.ciklon.friendtracker.common.exception.CustomException;
import com.ciklon.friendtracker.common.exception.ExceptionType;
import com.ciklon.friendtracker.core.entity.*;
import com.ciklon.friendtracker.core.mapper.QuestionAnswerMapper;
import com.ciklon.friendtracker.core.mapper.QuestionMapper;
import com.ciklon.friendtracker.core.mapper.UserAnswerMapper;
import com.ciklon.friendtracker.core.repository.QuestionAnswerRepository;
import com.ciklon.friendtracker.core.repository.QuestionRepository;
import com.ciklon.friendtracker.core.repository.UserAnswerRepository;
import com.ciklon.friendtracker.core.service.integration.ContactIntegrationService;
import com.ciklon.friendtracker.core.service.integration.UserIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final UserAnswerRepository userAnswerRepository;

    private final ContactIntegrationService contactIntegrationService;
    private final UserIntegrationService userIntegrationService;

    private final QuestionMapper questionMapper;
    private final QuestionAnswerMapper questionAnswerMapper;
    private final UserAnswerMapper userAnswerMapper;

    private <T, R> PaginationResponse<R> getPaginatedResponse(
            int page,
            int size,
            Function<PageRequest, Page<T>> fetchFunction,
            Function<T, R> mapFunction
    ) {
        Page<T> dtoPage = fetchFunction.apply(PageRequest.of(page - 1, size));
        return new PaginationResponse<>(
                dtoPage.getSize(),
                dtoPage.getNumber() + 1,
                dtoPage.getTotalPages(),
                dtoPage.getContent().stream()
                        .map(mapFunction)
                        .toList()
        );
    }

    public PaginationResponse<QuestionDto> getAllQuestions(int page, int size) {
        return getPaginatedResponse(page, size, questionRepository::findAll, questionMapper::map);
    }

    private PaginationResponse<UserAnswerDto> getUserAnswers(
            int page,
            int size,
            UUID userId,
            BiFunction<UUID, PageRequest, Page<UserAnswer>> fetchFunction,
            UserAnswerMapper userAnswerMapper
    ) {
        return getPaginatedResponse(
                page,
                size,
                pageRequest -> fetchFunction.apply(userId, pageRequest),
                userAnswerMapper::map
        );
    }

    public PaginationResponse<UserAnswerDto> getAllUserAnswers(
            int page, int size, UUID userId
    ) {
        return getUserAnswers(page, size, userId, userAnswerRepository::findAllByUserId, userAnswerMapper);
    }

    public PaginationResponse<UserAnswerDto> getUserAnswersByContactId(
            int page,
            int size,
            UUID contactId,
            UUID userId
    ) {
        return getUserAnswers(
                page, size, userId,
                (id, pageRequest) -> userAnswerRepository.findAllByUserIdAndContactId(contactId, id, pageRequest),
                userAnswerMapper
        );
    }

    public QuestionDto getQuestionById(UUID id) {
        return questionRepository.findById(id)
                .map(questionMapper::map)
                .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST, "Question not found"));
    }

    public UserAnswerDto addUserAnswer(UserAnswerCreationDto userAnswerCreationDto, UUID userId) {
        Question question = questionRepository.findById(userAnswerCreationDto.questionId())
                .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST, "Question not found"));
        QuestionAnswer questionAnswer = questionAnswerRepository.findById(userAnswerCreationDto.answerId())
                .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST, "Answer not found"));
        if (!questionAnswer.getQuestion().getId().equals(question.getId())) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "Answer does not belong to the question");
        }
        Contact contact = contactIntegrationService.getContactById(userAnswerCreationDto.contactId());
        User user = userIntegrationService.getUserById(userId);
        UserAnswer userAnswer = userAnswerRepository.save(new UserAnswer(question, questionAnswer, contact, user));
        return userAnswerMapper.map(userAnswer);
    }

    public QuestionDto createNewQuestion(QuestionCreationDto questionCreationDto) {
        if (questionCreationDto.question() == null || questionCreationDto.question().isBlank()) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "Question must not be empty");
        }
        if (questionCreationDto.answers() == null || questionCreationDto.answers().isEmpty()) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "Question must have at least one answer");
        }
        Question question = questionRepository.save(questionMapper.map(questionCreationDto));
        List<QuestionAnswer> questionAnswers = questionCreationDto.answers().stream()
                .map(dto -> new QuestionAnswer(dto.answer(), question))
                .toList();
        List<QuestionAnswerDto> questionAnswerDtos = questionAnswerRepository.saveAll(questionAnswers).stream()
                .map(questionAnswerMapper::map)
                .toList();
        return questionMapper.map(question, questionAnswerDtos);
    }

    public QuestionDto deleteQuestion(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        questionRepository.delete(question);
        return questionMapper.map(question);
    }

    public QuestionDto editQuestion(UUID questionId, UpdateQuestionDto updateQuestionDto) {
        if (updateQuestionDto.question().isEmpty()) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "Question must not be empty");
        }
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Question not found"));
        return questionMapper.map(questionRepository.save(questionMapper.map(question, updateQuestionDto)));
    }

    public QuestionAnswerDto editAnswer(UUID answerId, UpdateAnswerDto updateAnswerDto) {
        if (updateAnswerDto.answer().isEmpty()) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "Answer must not be empty");
        }
        QuestionAnswer questionAnswer = questionAnswerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Answer not found"));
        questionAnswer.setAnswer(updateAnswerDto.answer());
        return questionAnswerMapper.map(questionAnswerRepository.save(questionAnswer));
    }

    @Transactional
    public List<QuestionAnswerDto> updateAnswerListForQuestion(
            UUID questionId,
            List<UpdateAnswerDto> updateAnswerDtos
    ) {
        if (updateAnswerDtos.isEmpty()) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "Question must have at least one answer");
        }
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Question not found"));
        List<QuestionAnswer> questionAnswers = updateAnswerDtos.stream()
                .map(updateAnswerDto -> new QuestionAnswer(updateAnswerDto.answer(), question))
                .toList();
        return questionAnswerRepository.saveAll(questionAnswers).stream()
                .map(questionAnswerMapper::map)
                .toList();
    }

    public QuestionAnswerDto deleteAnswer(UUID answerId) {
        QuestionAnswer questionAnswer = questionAnswerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Answer not found"));
        questionAnswerRepository.delete(questionAnswer);
        return questionAnswerMapper.map(questionAnswer);
    }

    @Transactional
    public List<QuestionAnswerDto> deleteAllAnswersByQuestionId(UUID questionId) {
        return questionAnswerRepository.deleteAllByQuestionId(questionId).stream()
                .map(questionAnswerMapper::map)
                .toList();
    }

    public UserAnswerDto deleteUserAnswer(UUID answerId, UUID userId) {
        UserAnswer userAnswer = userAnswerRepository.findById(answerId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Answer not found"));
        if (!userAnswer.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionType.FORBIDDEN, "You can delete only your answers");
        }
        userAnswerRepository.delete(userAnswer);
        return userAnswerMapper.map(userAnswer);
    }
}
