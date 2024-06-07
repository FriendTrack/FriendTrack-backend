package com.ciklon.friendtracker.config;


import com.ciklon.friendtracker.core.mapper.ContactInteractionMapper;
import com.ciklon.friendtracker.core.mapper.ContactMapper;
import com.ciklon.friendtracker.core.mapper.FormMapper;
import com.ciklon.friendtracker.core.mapper.UserMapper;
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
}