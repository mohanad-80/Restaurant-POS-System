package com.konecta.internship.Restaurant_POS_System.MenuItem;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/menu")
public class MenuItemController 
{
     @Autowired
    private MenuItemService menuItemService;

    @GetMapping
    public ResponseEntity<List<MenuItemEntity>> getMenuItems(@RequestParam(required = false) Long categoryId,@RequestParam(required = false) String status) 
    {
        List<MenuItemEntity> items = menuItemService.getMenuItems(categoryId, status);
        return ResponseEntity.ok(items); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemEntity> getMenuItemById(@PathVariable Long id) 
    {
    MenuItemEntity item = menuItemService.getMenuItemById(id);
    return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<MenuItemEntity> addMenuItem( @Valid @RequestBody MenuItemDTO dto)
    {
        MenuItemEntity savedItem = menuItemService.addMenuItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemEntity> updateMenuItem(@PathVariable Long id, @RequestParam Map<String, String> updates) 
    {    
        MenuItemEntity updatedItem = menuItemService.updateMenuItem(id, updates);
        return ResponseEntity.ok(updatedItem); 
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id)
    {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build(); 
    }

    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile image)
    {
        return menuItemService.uploadImage(id, image);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) 
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElement(NoSuchElementException ex) 
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception ex) 
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}
