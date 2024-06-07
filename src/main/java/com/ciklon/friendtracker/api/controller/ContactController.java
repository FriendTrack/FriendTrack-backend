package com.ciklon.friendtracker.api.controller;

import com.ciklon.friendtracker.api.constant.ApiPaths;
import com.ciklon.friendtracker.api.dto.contact.ContactCreationDto;
import com.ciklon.friendtracker.api.dto.contact.ContactDto;
import com.ciklon.friendtracker.api.dto.contact.ContactPaginationResponse;
import com.ciklon.friendtracker.api.dto.contact.UpdateContactDto;
import com.ciklon.friendtracker.core.service.ContactService;
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
@Tag(name = "Contact", description = "API для управления контактами")
public class ContactController {

    private final ContactService contactService;

    @PostMapping(ApiPaths.CONTACT)
    @Operation(summary = "Создание контакта",
            description = "Создает новый контакт для текущего пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное создание контакта"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ContactDto createContact(@Validated @RequestBody ContactCreationDto creationDto) {
        UUID creatorId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return contactService.createContact(creationDto, creatorId);
    }

    @PostMapping(ApiPaths.CONTACT_LIST)
    @Operation(summary = "Создание списка контактов",
            description = "Создает новый список контактов для текущего пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное создание контакта"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public List<ContactDto> createContacts(@Validated @RequestBody List<ContactCreationDto> creationDtoList) {
        UUID creatorId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return contactService.createContacts(creationDtoList, creatorId);
    }

    @PutMapping(ApiPaths.CONTACT_BY_ID)
    @Operation(summary = "Обновление контакта", description = "Полное обновление существующего контакта.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное обновление контакта"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Контакт не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ContactDto updateContact(
            @PathVariable("id") UUID contactId,
            @Validated @RequestBody UpdateContactDto updateContactDto
    ) {
        return contactService.updateContact(contactId, updateContactDto);
    }

    @PatchMapping(ApiPaths.CONTACT_BY_ID)
    @Operation(summary = "Частичное обновление контакта", description = """
                    Частичное обновление существующего контакта (если поле null, то оно не будет обновлено).
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное обновление контакта"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Контакт не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ContactDto partialUpdateContact(
            @PathVariable("id") UUID contactId,
            @Validated @RequestBody UpdateContactDto updateContactDto
    ) {
        return contactService.partialUpdateContact(contactId, updateContactDto);
    }

    @DeleteMapping(ApiPaths.CONTACT_BY_ID)
    @Operation(summary = "Удаление контакта", description = "Удаляет существующий контакт по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное удаление контакта"),
            @ApiResponse(responseCode = "404", description = "Контакт не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public UUID deleteContact(@PathVariable("id") UUID contactId) {
        contactService.deleteContact(contactId);
        return contactId;
    }

    @GetMapping(ApiPaths.CONTACT_BY_ID)
    @Operation(summary = "Получение контакта по ID",
            description = "Возвращает данные существующего контакта по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение контакта"),
            @ApiResponse(responseCode = "404", description = "Контакт не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ContactDto getContactById(@PathVariable("id") UUID contactId) {
        return contactService.getContactById(contactId);
    }

    @GetMapping(ApiPaths.CONTACT)
    @Operation(summary = "Получение списка контактов",
            description = "Возвращает список контактов текущего пользователя с пагинацией.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка контактов"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    public ContactPaginationResponse getContactList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        UUID creatorId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        page = page < 1 ? 1 : page;
        size = size < 1 ? 10 : size;
        return contactService.getContactList(page, size, creatorId);
    }
}
