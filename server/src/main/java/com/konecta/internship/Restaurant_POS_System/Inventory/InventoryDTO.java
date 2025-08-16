package com.konecta.internship.Restaurant_POS_System.Inventory;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO 
{
    @NotBlank(message = "Inventory name is required")
    private String name;

    @NotNull(message = "Available units are required")
    private BigDecimal available_units;

    @NotBlank(message = "Unit is required")
    private String unit;

    private List<Long> menuItemIds;

}
