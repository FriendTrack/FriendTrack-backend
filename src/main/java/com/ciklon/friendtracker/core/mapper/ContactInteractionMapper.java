package com.ciklon.friendtracker.core.mapper;

import com.ciklon.friendtracker.api.dto.form.ContactInteractionCreationDto;
import com.ciklon.friendtracker.api.dto.form.ContactInteractionDto;
import com.ciklon.friendtracker.core.entity.Contact;
import com.ciklon.friendtracker.core.entity.ContactInteraction;
import com.ciklon.friendtracker.core.entity.Form;
import com.ciklon.friendtracker.core.entity.embedabble.ContactInteractionId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactInteractionMapper {
    
    ContactInteractionMapper INSTANCE = Mappers.getMapper(ContactInteractionMapper.class);

    @Mapping(target = "formId", source = "id.formId")
    @Mapping(target = "contactId", source = "id.contactId")
    ContactInteractionDto map(ContactInteraction contactInteraction);

    @Mapping(target = "id", source = "contactInteractionId")
    @Mapping(target = "form", source = "form")
    @Mapping(target = "contact", source = "contact")
    @Mapping(target = "communication", source = "contactInteractionCreationDto.communication")
    @Mapping(target = "empathy", source = "contactInteractionCreationDto.empathy")
    @Mapping(target = "trust", source = "contactInteractionCreationDto.trust")
    @Mapping(target = "time", source = "contactInteractionCreationDto.time")
    @Mapping(target = "respect", source = "contactInteractionCreationDto.respect")
    ContactInteraction map(
            Contact contact, ContactInteractionId contactInteractionId, Form form,
            ContactInteractionCreationDto contactInteractionCreationDto
    );
}
