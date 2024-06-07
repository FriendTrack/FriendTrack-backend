package com.ciklon.friendtracker.core.mapper;

import com.ciklon.friendtracker.api.dto.form.ContactInteractionDto;
import com.ciklon.friendtracker.core.entity.ContactInteraction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactInteractionMapper {
    
    ContactInteractionMapper INSTANCE = Mappers.getMapper(ContactInteractionMapper.class);

    @Mapping(target = "interactionId", source = "id.formId")
    @Mapping(target = "contactId", source = "id.contactId")
    ContactInteractionDto map(ContactInteraction contactInteraction);
}
