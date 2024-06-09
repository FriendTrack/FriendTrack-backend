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

    @Mapping(target = "interactionId", source = "id.formId")
    @Mapping(target = "contactId", source = "id.contactId")
    ContactInteractionDto map(ContactInteraction contactInteraction);

    @Mapping(target = "id", source = "contactInteractionId")
    @Mapping(target = "form", source = "form")
    @Mapping(target = "contact", source = "contact")
    @Mapping(target = "happiness", source = "contactInteractionCreationDto.happiness")
    @Mapping(target = "sadness", source = "contactInteractionCreationDto.sadness")
    @Mapping(target = "fear", source = "contactInteractionCreationDto.fear")
    @Mapping(target = "disgust", source = "contactInteractionCreationDto.disgust")
    @Mapping(target = "anger", source = "contactInteractionCreationDto.anger")
    @Mapping(target = "surprise", source = "contactInteractionCreationDto.surprise")
    @Mapping(target = "interactionMark", source = "contactInteractionCreationDto.interactionMark")
    @Mapping(target = "interactionType", source = "contactInteractionCreationDto.interactionType")
    ContactInteraction map(
            Contact contact, ContactInteractionId contactInteractionId, Form form,
            ContactInteractionCreationDto contactInteractionCreationDto
    );
}
