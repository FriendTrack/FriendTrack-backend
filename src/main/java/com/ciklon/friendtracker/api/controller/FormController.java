package com.ciklon.friendtracker.api.controller;

import com.ciklon.friendtracker.api.constant.ApiPaths;
import com.ciklon.friendtracker.api.dto.form.*;
import com.ciklon.friendtracker.core.service.FormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Forms", description = "API для управления формамми")
public class FormController {

    private final FormService formService;

    @PostMapping(ApiPaths.FORM)
    @Operation(summary = "Создание новой заполненной формы", description = """
            Создает новую форму для текущего пользователя.""")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Форма успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные формы"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public FormDto createForm(@Validated @RequestBody FormCreationDto formCreationDto) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return formService.createForm(userId, formCreationDto);
    }

    @PatchMapping(ApiPaths.FORM_BY_ID)
    @Operation(summary = "Обновление формы", description = "Обновляет данные формы.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Форма успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные формы"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ShortFormDto updateForm(
            @PathVariable("id") UUID formId,
            @Validated @RequestBody UpdateFormDto updateFormDto
    ) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return formService.updateForm(formId, userId, updateFormDto);
    }

    @PatchMapping(ApiPaths.FORM_BY_ID)
    @Operation(summary = "Обновление контактов взаимодействия в форме", description = "Обновляет данные формы " +
            "взаимодейстия пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Форма успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные формы"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public FormDto updateFormContactInteractions(
            @PathVariable("id") UUID formId,
            @Validated @RequestBody List<ContactInteractionCreationDto> creationDtos
    ) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return formService.updateContactInteractions(formId, userId, creationDtos);
    }

    @DeleteMapping(ApiPaths.FORM_BY_ID)
    @Operation(summary = "Удаление формы", description = "Удаляет форму.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Форма успешно удалена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные формы"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public void deleteForm(@PathVariable("id") UUID formId) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        formService.deleteForm(formId, userId);
    }

    @GetMapping(ApiPaths.FORM_BY_ID)
    @Operation(summary = "Получение формы", description = "Получает форму.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Форма успешно получена"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные формы"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public FormDto getForm(@PathVariable("id") UUID formId) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return formService.getForm(formId, userId);
    }
}