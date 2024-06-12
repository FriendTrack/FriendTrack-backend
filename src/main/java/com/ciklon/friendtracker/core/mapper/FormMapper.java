package com.ciklon.friendtracker.core.mapper;

import com.ciklon.friendtracker.api.dto.form.*;
import com.ciklon.friendtracker.core.entity.Form;
import com.ciklon.friendtracker.core.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FormMapper {
    FormMapper INSTANCE = Mappers.getMapper(FormMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contactInteractions", ignore = true)
    @Mapping(target = "mood", source = "formCreationDto.mood")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Form map(FormCreationDto formCreationDto, User user);

    @Mapping(target = "userId", source = "form.user.id")
    @Mapping(target = "contactInteractions", source = "contactInteractionDtoList")
    FormDto map(Form form, List<ContactInteractionDto> contactInteractionDtoList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contactInteractions", ignore = true)
    @Mapping(target = "mood", source = "updateFormDto.mood")
    @Mapping(target = "date", source = "updateFormDto.date")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Form map(@MappingTarget Form form, UpdateFormDto updateFormDto);


    @Mapping(target = "userId", source = "form.user.id")
    ShortFormDto map(Form form);
}
