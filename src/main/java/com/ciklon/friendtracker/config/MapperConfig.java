package com.ciklon.friendtracker.config;


import com.ciklon.friendtracker.core.mapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public UserMapper userMapper() {
        return UserMapper.INSTANCE;
    }

    @Bean
    public ContactMapper contactMapper() {
        return ContactMapper.INSTANCE;
    }

    @Bean
    public FormMapper formMapper() {
        return FormMapper.INSTANCE;
    }

    @Bean
    public ContactInteractionMapper contactInteractionMapper() {
        return ContactInteractionMapper.INSTANCE;
    }

    @Bean
    public QuestionMapper questionMapper() {
        return QuestionMapper.INSTANCE;
    }

    @Bean
    public UserAnswerMapper userAnswerMapper() {
        return UserAnswerMapper.INSTANCE;
    }
}