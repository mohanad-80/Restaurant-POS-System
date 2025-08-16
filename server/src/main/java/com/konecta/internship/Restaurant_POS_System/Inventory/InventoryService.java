package com.konecta.internship.Restaurant_POS_System.Inventory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService 
{
    @Autowired
    private InventoryRepository inventoryRepository;

    public List<InventoryEntity> getInventory()
    {
        return inventoryRepository.findAll();
    }

    public InventoryEntity addInventoryItem(InventoryEntity item)
    {
        return inventoryRepository.save(item);
    }

    
}
