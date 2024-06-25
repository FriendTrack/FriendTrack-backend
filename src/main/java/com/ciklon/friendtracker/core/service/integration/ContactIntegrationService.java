package com.ciklon.friendtracker.core.service.integration;

import com.ciklon.friendtracker.common.exception.CustomException;
import com.ciklon.friendtracker.common.exception.ExceptionType;
import com.ciklon.friendtracker.core.entity.Contact;
import com.ciklon.friendtracker.core.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactIntegrationService {

    private final ContactRepository contactRepository;

    public List<Contact> getContactsByIds(List<UUID> contactIds) {
        return contactRepository.findAllById(contactIds);
    }

    public Contact getContactById(UUID contactId) {
        return contactRepository.findById(contactId).
                orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST, "Contact not found"));
    }

    public List<UUID> getContactsByUserId(UUID userId) {
        return contactRepository.findContactIdsByUserId(userId);
    }

    public List<UUID> getContactsByUserIdAndToDate(UUID userId, LocalDate toDate) {
        return contactRepository.findContactIdsByUserIdAndToDate(userId, toDate);
    }
}
