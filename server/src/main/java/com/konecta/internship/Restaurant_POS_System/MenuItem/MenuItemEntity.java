package com.konecta.internship.Restaurant_POS_System.MenuItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.konecta.internship.Restaurant_POS_System.Category.CategoryEntity;
import com.konecta.internship.Restaurant_POS_System.Inventory.InventoryEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "menu_items")
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

    @ManyToMany
    @JoinTable(
        name = "menuitem_inventory",
        joinColumns = @JoinColumn(name = "menuitem_id"),
        inverseJoinColumns = @JoinColumn(name = "inventory_id")
    )
    private List<InventoryEntity> ingredients;

    public enum Status {
        AVAILABLE,
        OUT_OF_STOCK
    }
}
