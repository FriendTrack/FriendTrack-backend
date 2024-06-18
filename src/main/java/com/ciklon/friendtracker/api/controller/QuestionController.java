package com.ciklon.friendtracker.api.controller;

import com.ciklon.friendtracker.api.constant.ApiPaths;
import com.ciklon.friendtracker.api.dto.PaginationResponse;
import com.ciklon.friendtracker.api.dto.question.*;
import com.ciklon.friendtracker.api.dto.rating.UserAnswerCreationDto;
import com.ciklon.friendtracker.api.dto.rating.UserAnswerDto;
import com.ciklon.friendtracker.core.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Question", description = "API для управления вопросами и ответами для них")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping(ApiPaths.USER_ANSWER)
    @Operation(summary = "Добавить ответ пользователя на вопрос", description = "Добавление ответа пользователя на вопрос.")
    public UserAnswerDto addAnswer(@RequestBody UserAnswerCreationDto userAnswerCreationDto) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return questionService.addUserAnswer(userAnswerCreationDto, userId);
    }

    @DeleteMapping(ApiPaths.USER_ANSWER_BY_ID)
    @Operation(summary = "Удалить ответ пользователя на вопрос", description = "Удаление ответа пользователя на вопрос.")
    public UserAnswerDto deleteUserAnswer(@PathVariable("id") UUID answerId) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return questionService.deleteUserAnswer(answerId, userId);
    }

    @GetMapping(ApiPaths.QUESTION)
    @Operation(summary = "Получение списка вопросов", description = "Получение списка вопросов.")
    public PaginationResponse<QuestionDto> getQuestions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;
        return questionService.getAllQuestions(page, size);
    }

    @GetMapping(ApiPaths.QUESTION_BY_ID)
    @Operation(summary = "Получение вопроса по id", description = "Получение вопроса по id.")
    public QuestionDto getQuestionById(@PathVariable UUID id) {
        return questionService.getQuestionById(id);
    }

    @GetMapping(ApiPaths.USER_ANSWERS)
    @Operation(summary = "Получение ответов пользователя", description = "Получение всех ответов пользователя.")
    public PaginationResponse<UserAnswerDto> getAllUserAnswers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "userId") UUID userId
    ) {
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;
        return questionService.getAllUserAnswers(page, size, userId);
    }

    @GetMapping(ApiPaths.USER_ANSWERS_BY_CONTACT)
    @Operation(summary = "Получение ответов пользователя по контакту", description = "Получение всех ответов " +
            "пользователя для конкретного контакта.")
    public PaginationResponse<UserAnswerDto> getUserAnswersByContactId(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable("id") UUID contactId
    ) {
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return questionService.getUserAnswersByContactId(page, size, contactId, userId);
    }

    @PostMapping(ApiPaths.QUESTION)
    @Operation(summary = "Создать новый вопрос", description = "Создание нового вопроса с ответами.")
    public QuestionDto createNewQuestion(@RequestBody QuestionCreationDto questionCreationDto) {
        return questionService.createNewQuestion(questionCreationDto);
    }

    @DeleteMapping(ApiPaths.QUESTION_BY_ID)
    @Operation(summary = "Удалить вопрос", description = "Удаление вопроса по ID.")
    public QuestionDto deleteQuestion(@PathVariable("id") UUID questionId) {
        return questionService.deleteQuestion(questionId);
    }

    @PutMapping(ApiPaths.QUESTION_BY_ID)
    @Operation(summary = "Редактировать вопрос", description = "Редактирование вопроса по ID.")
    public QuestionDto editQuestion(
            @PathVariable("id") UUID questionId,
            @RequestBody UpdateQuestionDto updateQuestionDto
    ) {
        return questionService.editQuestion(questionId, updateQuestionDto);
    }

    @PutMapping(ApiPaths.QUESTION_ANSWER_BY_ID)
    @Operation(summary = "Редактировать ответ на вопрос", description = "Редактирование ответа на вопрос по ID.")
    public QuestionAnswerDto editAnswer(
            @PathVariable("id") UUID answerId,
            @RequestBody UpdateAnswerDto updateAnswerDto
    ) {
        return questionService.editAnswer(answerId, updateAnswerDto);
    }

    @PutMapping(ApiPaths.QUESTION_ANSWERS)
    @Operation(summary = "Обновить список ответов для вопроса (НЕ удаляет старые)", description = """
            ДОбавление списка ответов для конкретного вопроса.""")
    public List<QuestionAnswerDto> updateAnswerListForQuestion(
            @PathVariable("id") UUID questionId,
            @RequestBody List<UpdateAnswerDto> updateAnswerDtos
    ) {
        return questionService.updateAnswerListForQuestion(questionId, updateAnswerDtos);
    }

    @DeleteMapping(ApiPaths.QUESTION_ANSWER_BY_ID)
    @Operation(summary = "Удалить ответ на вопрос", description = "Удаление ответа на вопрос по ID.")
    public QuestionAnswerDto deleteAnswer(@PathVariable("id") UUID answerId) {
        return questionService.deleteAnswer(answerId);
    }

    @DeleteMapping(ApiPaths.ALL_QUESTION_ANSWERS_BY_QUESTION_ID)
    @Operation(summary = "Удалить все ответы для вопроса", description = "Удаление всех ответов на вопрос по ID.")
    public List<QuestionAnswerDto> deleteAllAnswersByQuestionId(@PathVariable("id") UUID questionId) {
        return questionService.deleteAllAnswersByQuestionId(questionId);
    }
}

