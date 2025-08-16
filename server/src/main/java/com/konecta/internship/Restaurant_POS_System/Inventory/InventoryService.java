package com.konecta.internship.Restaurant_POS_System.Inventory;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

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

    public void importCSV(MultipartFile file) throws Exception 
    {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream())))
        {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) 
            {
                InventoryEntity item = new InventoryEntity();
                item.setName(line[0]);
                item.setAvailable_units(new BigDecimal(line[1]));
                item.setUnit(line[2]);
                inventoryRepository.save(item);
            }
        }
    }

    public void exportCSV(OutputStream outputStream) throws Exception 
    {
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) 
        {
            writer.writeNext(new String[]{"name","available_units","unit"});
            for (InventoryEntity item : inventoryRepository.findAll()) {
                writer.writeNext(new String[]{
                    item.getName(),
                    item.getAvailable_units().toString(),
                    item.getUnit()
                });
            }
        }
    }

    public void deductStock(Long inventoryId, int quantity) 
    {
        InventoryEntity inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new RuntimeException("Inventory not found: " + inventoryId));

        BigDecimal newStock = inventory.getAvailable_units().subtract(BigDecimal.valueOf(quantity));

        if (newStock.compareTo(BigDecimal.ZERO) < 0) 
        {
            throw new RuntimeException("Not enough stock for " + inventory.getName());
        }

        inventory.setAvailable_units(newStock);
        inventoryRepository.save(inventory);
    }
}
