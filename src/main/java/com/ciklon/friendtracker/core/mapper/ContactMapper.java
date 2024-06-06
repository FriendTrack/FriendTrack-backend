package com.ciklon.friendtracker.core.mapper;

import com.ciklon.friendtracker.api.dto.contact.ContactCreationDto;
import com.ciklon.friendtracker.api.dto.contact.ContactDto;
import com.ciklon.friendtracker.api.dto.contact.UpdateContactDto;
import com.ciklon.friendtracker.core.entity.Contact;
import com.ciklon.friendtracker.core.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);
    @Mapping(target = "user", source = "user")
    Contact map(ContactCreationDto creationDto, User user);

    @Mapping(target = "userId", source = "contact.user.id")
    ContactDto map(Contact contact);

    @Mapping(target = "contact.name", source = "updateContactDto.name", nullValuePropertyMappingStrategy =
            org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contact.details", source = "updateContactDto.details", nullValuePropertyMappingStrategy =
            org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contact.link", source = "updateContactDto.link", nullValuePropertyMappingStrategy =
            org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contact.birthDate", source = "updateContactDto.birthDate", nullValuePropertyMappingStrategy =
            org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "contact.updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "contact.createdAt", ignore = true)
    @Mapping(target = "contact.id", ignore = true)
    @Mapping(target = "contact.user", ignore = true)
    Contact partialMap(@MappingTarget Contact contact, UpdateContactDto updateContactDto);

    @Mapping(target = "contact.name", source = "updateContactDto.name")
    @Mapping(target = "contact.details", source = "updateContactDto.details")
    @Mapping(target = "contact.link", source = "updateContactDto.link")
    @Mapping(target = "contact.birthDate", source = "updateContactDto.birthDate")
    @Mapping(target = "contact.updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "contact.createdAt", ignore = true)
    @Mapping(target = "contact.id", ignore = true)
    @Mapping(target = "contact.user", ignore = true)
    Contact map(@MappingTarget Contact contact, UpdateContactDto updateContactDto);
}
