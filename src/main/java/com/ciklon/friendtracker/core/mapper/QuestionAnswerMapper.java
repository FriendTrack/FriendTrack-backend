package com.ciklon.friendtracker.core.mapper;

import com.ciklon.friendtracker.api.dto.question.QuestionAnswerDto;
import com.ciklon.friendtracker.core.entity.QuestionAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper
public interface QuestionAnswerMapper {

    QuestionAnswerMapper INSTANCE = Mappers.getMapper(QuestionAnswerMapper.class);

    QuestionAnswerDto map(QuestionAnswer questionAnswer);
}
