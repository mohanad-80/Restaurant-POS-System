package com.konecta.internship.Restaurant_POS_System.MenuItem;

import java.math.BigDecimal;
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
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name="category_id")
    private CategoryEntity category;
    
    private BigDecimal price;
    
    private int preparation_time;
    private String image_path;
    
    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime created_at;

    public enum Status {
        AVAILABLE,
        OUT_OF_STOCK
    }
}
