package com.konecta.internship.Restaurant_POS_System.Inventory;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/Inventory")
public class InventoryController 
{
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/all")
    public List<InventoryEntity> getInventory()
    {
        return inventoryService.getInventory();
    }

    @PutMapping("/add")
    public InventoryEntity addInventoryItem(@RequestBody InventoryEntity item)
    {
        return inventoryService.addInventoryItem(item);
    }


}
