package com.konecta.internship.Restaurant_POS_System.Inventory;

import java.math.BigDecimal;
import java.util.List;

import com.konecta.internship.Restaurant_POS_System.MenuItem.MenuItemEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Inventory")
public class InventoryEntity 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private BigDecimal  available_units;

    private String unit;

    @ManyToMany(mappedBy = "ingredients")
    private List<MenuItemEntity> menuItems;

}
