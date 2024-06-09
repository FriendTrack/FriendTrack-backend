package com.ciklon.friendtracker.core.service;

import com.ciklon.friendtracker.api.dto.form.*;
import com.ciklon.friendtracker.common.exception.CustomException;
import com.ciklon.friendtracker.common.exception.ExceptionType;
import com.ciklon.friendtracker.core.entity.Contact;
import com.ciklon.friendtracker.core.entity.ContactInteraction;
import com.ciklon.friendtracker.core.entity.Form;
import com.ciklon.friendtracker.core.entity.User;
import com.ciklon.friendtracker.core.entity.embedabble.ContactInteractionId;
import com.ciklon.friendtracker.core.mapper.ContactInteractionMapper;
import com.ciklon.friendtracker.core.mapper.FormMapper;
import com.ciklon.friendtracker.core.repository.ContactInteractionRepository;
import com.ciklon.friendtracker.core.repository.FormRepository;
import com.ciklon.friendtracker.core.service.integration.ContactIntegrationService;
import com.ciklon.friendtracker.core.service.integration.UserIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;
    private final FormMapper formMapper;

    private final ContactInteractionRepository contactInteractionRepository;
    private final ContactInteractionMapper contactInteractionMapper;

    private final UserIntegrationService userIntegrationService;
    private final ContactIntegrationService contactIntegrationService;

    public FormDto createForm(UUID userId, FormCreationDto formCreationDto) {
        try {
            User user = userIntegrationService.getUserById(userId);

            Form form = formRepository.save(
                    formMapper.map(formCreationDto, user)
            );

            List<Contact> contacts =
                    contactIntegrationService.getContactsByIds(
                            formCreationDto.contactInteractions().stream()
                                    .map(ContactInteractionCreationDto::contactId)
                                    .collect(Collectors.toList())
                    );

            List<ContactInteraction> contactInteractions = formCreationDto.contactInteractions()
                    .stream()
                    .map(contactInteractionCreationDto -> {
                        Contact contact = contacts.stream()
                                .filter(c -> c.getId().equals(contactInteractionCreationDto.contactId()))
                                .findFirst()
                                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Contact not found"));
                        return contactInteractionMapper.map(
                                contact,
                                new ContactInteractionId(contact.getId(), form.getId()),
                                form,
                                contactInteractionCreationDto
                        );
                    })
                    .toList();

            contactInteractions = contactInteractionRepository.saveAll(contactInteractions);
            form.setContactInteractions(contactInteractions);
            formRepository.save(form);

            List<ContactInteractionDto> contactInteractionDtoList = contactInteractions.stream()
                    .map(contactInteractionMapper::map)
                    .toList();

            return formMapper.map(form, contactInteractionDtoList);
        } catch (Exception e) {
            throw new CustomException(ExceptionType.FATAL, "Error creating form");
        }

    }

    public FormDto updateForm(UUID formId, UUID userId, UpdateFormDto updateFormDto) {
        throw new CustomException(ExceptionType.FATAL, "not implemented");
//        Form form = formRepository.findById(formId)
//                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Form not found"));
//        if (!form.getUser().getId().equals(userId)) {
//            throw new CustomException(ExceptionType.FORBIDDEN, "Form does not belong to the user");
//        }
//        throw new CustomException(ExceptionType.FATAL, "not implemented");
    }

    public void deleteForm(UUID formId, UUID userId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Form not found"));
        if (!form.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionType.FORBIDDEN, "Form does not belong to the user");
        }
        formRepository.delete(form);
    }

    public FormDto getForm(UUID formId, UUID userId) {
        Form form = getFormIfBelongToUser(formId, userId);
        List<ContactInteractionDto> contactInteractionDtoList = form.getContactInteractions().stream()
                .map(contactInteractionMapper::map)
                .toList();
        return formMapper.map(form, contactInteractionDtoList);
    }

    private Form getFormIfBelongToUser(UUID formId, UUID userId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Form not found"));
        if (!form.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionType.FORBIDDEN, "Form does not belong to the user");
        }
        return form;
    }

}
