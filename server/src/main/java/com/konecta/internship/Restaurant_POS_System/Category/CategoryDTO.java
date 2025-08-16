package com.konecta.internship.Restaurant_POS_System.Category;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CategoryDTO 
{
    @NotBlank(message = "Category name is required")
    private String name;

}
