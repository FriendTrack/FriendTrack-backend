package com.ciklon.friendtracker.core.mapper;

import com.ciklon.friendtracker.api.dto.rating.UserAnswerCreationDto;
import com.ciklon.friendtracker.api.dto.rating.UserAnswerDto;
import com.ciklon.friendtracker.core.entity.UserAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface UserAnswerMapper {

    UserAnswerMapper INSTANCE = Mappers.getMapper(UserAnswerMapper.class);

    UserAnswer map(UserAnswerCreationDto userAnswerCreationDto, UUID userId);

    @Mapping(target = "questionId", source = "userAnswer.question.id")
    @Mapping(target = "contactId", source = "userAnswer.contact.id")
    @Mapping(target = "answer", source = "userAnswer.questionAnswer.answer")
    UserAnswerDto map(UserAnswer userAnswer);
}
