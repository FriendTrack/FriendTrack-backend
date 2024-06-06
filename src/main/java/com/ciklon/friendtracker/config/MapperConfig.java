package com.ciklon.friendtracker.config;


import com.ciklon.friendtracker.core.mapper.ContactMapper;
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
}