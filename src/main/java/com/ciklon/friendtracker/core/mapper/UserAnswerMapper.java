package com.ciklon.friendtracker.core.mapper;

import com.ciklon.friendtracker.api.dto.rating.UserAnswerDto;
import com.ciklon.friendtracker.core.entity.UserAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserAnswerMapper {

    UserAnswerMapper INSTANCE = Mappers.getMapper(UserAnswerMapper.class);

    @Mapping(target = "questionId", source = "userAnswer.question.id")
    @Mapping(target = "contactId", source = "userAnswer.contact.id")
    @Mapping(target = "createdAt", source = "userAnswer.createdAt")
    UserAnswerDto map(UserAnswer userAnswer);
}
