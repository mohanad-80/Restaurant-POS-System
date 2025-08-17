package com.konecta.internship.Restaurant_POS_System.table_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableStatusUpdateDto {
    @NotBlank
    @Pattern(
            regexp = "AVAILABLE|OCCUPIED|RESERVED|OUT_OF_SERVICE",
            message = "must be one of (AVAILABLE, OCCUPIED, RESERVED, OUT_OF_SERVICE)"
    )
    private String status;
}
