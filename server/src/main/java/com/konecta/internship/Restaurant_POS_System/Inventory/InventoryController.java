package com.konecta.internship.Restaurant_POS_System.Inventory;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController 
{
    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public List<InventoryEntity> getInventory()
    {
        return inventoryService.getInventory();
    }

    @PostMapping
    public InventoryEntity addInventoryItem(@RequestBody InventoryEntity item)
    {
        return inventoryService.addInventoryItem(item);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile file) 
    {
        try 
        {
            inventoryService.importCSV(file);
            return ResponseEntity.ok("CSV imported successfully");
        } 
        catch (Exception e) 
        {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCSV() 
    {
        try 
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            inventoryService.exportCSV(out);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inventory.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(out.toByteArray());
        } 
        catch (Exception e) 
        {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
