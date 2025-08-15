package com.konecta.internship.Restaurant_POS_System.table_management;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ErrorDto {
    private String message;
    private Map<String, String> errors;
    private LocalDateTime timestamp;

    public ErrorDto(String message) {
        this.message = message;
        this.errors = new HashMap<>();
        this.timestamp = LocalDateTime.now();
    }
}
