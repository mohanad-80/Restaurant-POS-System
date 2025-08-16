package com.konecta.internship.Restaurant_POS_System.table_management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableRequestDto {
    @NotBlank
    private String tableNumber;
    @NotNull
    @Min(value = 1)
    private Integer seats;
    private String section;
    @Pattern(
            regexp = "AVAILABLE|OCCUPIED|RESERVED|OUT_OF_SERVICE",
            message = "must be in (AVAILABLE, OCCUPIED, RESERVED or OUT_OF_SERVICE)"
    )
    private String status;
}
