package com.ciklon.friendtracker.common;

import com.ciklon.friendtracker.common.exception.CustomException;
import com.ciklon.friendtracker.common.exception.ExceptionType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataValidator {

    public void validateDates(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && fromDate.isAfter(LocalDate.now())) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "Дата начала периода не может быть в будущем");
        }
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new CustomException(
                    ExceptionType.BAD_REQUEST,
                    "Дата начала периода не может быть позже даты окончания"
            );
        }
    }
}
