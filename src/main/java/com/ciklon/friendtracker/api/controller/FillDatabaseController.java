package com.ciklon.friendtracker.api.controller;

import com.ciklon.friendtracker.core.service.FillDatabaseService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequiredArgsConstructor
public class FillDatabaseController {

    private final FillDatabaseService fillDatabaseService;

    @PostMapping("/api/v1/fill-database")
    public void fillDatabase() {
        fillDatabaseService.performFillDatabase();
    }
}
