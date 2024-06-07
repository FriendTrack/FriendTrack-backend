package com.ciklon.friendtracker.core.service.integration;

import com.ciklon.friendtracker.core.entity.Contact;
import com.ciklon.friendtracker.core.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactIntegrationService {

    private final ContactRepository contactRepository;

    public List<Contact> getContactsByIds(List<UUID> contactIds) {
        return contactRepository.findAllById(contactIds);
    }
}
