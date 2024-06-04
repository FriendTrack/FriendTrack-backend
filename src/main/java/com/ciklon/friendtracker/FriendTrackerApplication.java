package com.ciklon.friendtracker;

import com.ciklon.friendtracker.common.exception.handler.EnableApiExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableApiExceptionHandler
@SpringBootApplication
public class FriendTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendTrackerApplication.class, args);
    }

}
