package com.ciklon.friendtracker.core.service;

import com.ciklon.friendtracker.api.dto.contact.ContactCreationDto;
import com.ciklon.friendtracker.api.dto.contact.ContactDto;
import com.ciklon.friendtracker.api.dto.contact.UpdateContactDto;
import com.ciklon.friendtracker.common.exception.CustomException;
import com.ciklon.friendtracker.common.exception.ExceptionType;
import com.ciklon.friendtracker.core.entity.Contact;
import com.ciklon.friendtracker.core.entity.User;
import com.ciklon.friendtracker.core.mapper.ContactMapper;
import com.ciklon.friendtracker.core.repository.ContactRepository;
import com.ciklon.friendtracker.core.service.integration.UserIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    private final UserIntegrationService userIntegrationService;

    public ContactDto createContact(ContactCreationDto creationDto, UUID creatorId) {
        User creator = userIntegrationService.getUserById(creatorId);
        Contact savedContact = contactRepository.save(
                contactMapper.map(creationDto, creator)
        );
        return contactMapper.map(savedContact);
    }

    public ContactDto updateContact(UUID contactId, UpdateContactDto updateContactDto) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Contact not found"));
        contact = contactMapper.map(contact, updateContactDto);
        return contactMapper.map(contactRepository.save(contact));
    }

    public ContactDto partialUpdateContact(UUID contactId, UpdateContactDto updateContactDto) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Contact not found"));
        contact = contactMapper.partialMap(contact, updateContactDto);
        return contactMapper.map(contactRepository.save(contact));
    }

    public void deleteContact(UUID contactId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Contact not found"));
        contactRepository.delete(contact);
    }

    public ContactDto getContactById(UUID contactId) {
        return contactMapper.map(
                contactRepository.findById(contactId)
                        .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Contact not found"))
        );
    }

    public Page<ContactDto> getContactList(int page, int size, UUID creatorId) {
        return contactRepository.findAllByUserId(
                creatorId,
                PageRequest.of(page, size)
        );
    }
}
