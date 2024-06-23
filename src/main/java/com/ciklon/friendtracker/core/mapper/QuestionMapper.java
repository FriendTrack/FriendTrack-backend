package com.ciklon.friendtracker.core.mapper;

import com.ciklon.friendtracker.api.dto.question.QuestionCreationDto;
import com.ciklon.friendtracker.api.dto.question.QuestionDto;
import com.ciklon.friendtracker.api.dto.question.UpdateQuestionDto;
import com.ciklon.friendtracker.core.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionMapper {

    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    QuestionDto map(Question question);

    @Mapping(target = "id", ignore = true)
    Question map(QuestionCreationDto questionCreationDto);

    @Mapping(target = "id", ignore = true)
    Question map(@MappingTarget Question question, UpdateQuestionDto updateQuestionDto);
}
