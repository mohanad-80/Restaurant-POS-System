package com.konecta.internship.Restaurant_POS_System.MenuItem;

import java.time.LocalDateTime;

import com.konecta.internship.Restaurant_POS_System.Category.CategoryEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
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
@Entity
@Table
public class MenuItemEntity 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name="category_id")
    private CategoryEntity category;
    
    @NotNull
    @DecimalMin("0.01")
    private double price;
    
    private int preperation_time;
    private String image_path;
    
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime  created_at;

    public enum Status 
    {
        AVAILABLE,
        OUT_OF_STOCK
    }
}
