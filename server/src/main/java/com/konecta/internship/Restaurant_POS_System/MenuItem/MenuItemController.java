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

@RestController
@RequestMapping("/api/v1/menu")
public class MenuItemController 
{
    @Autowired
    private MenuItemService menuItemService;

    @GetMapping
    public List<MenuItemEntity> getMenuItems(@RequestParam(required = false) Long categoryId,@RequestParam(required = false) String status) 
    {
        return menuItemService.getMenuItems(categoryId,status);   
    }

    @PostMapping
    public MenuItemEntity addMenuItem(@RequestBody MenuItemEntity menuItem)
    {
        return menuItemService.addMenuItem(menuItem);
    }

    @PutMapping("/{id}")
    public MenuItemEntity updateMenuItem (@PathVariable Long id, @RequestParam Map<String, String> updates) 
    {    
        return menuItemService.updateMenuItem(id, updates);
    }
    
    @DeleteMapping("/{id}")
    public void deleteMenuItem(@PathVariable Long id)
    {
        menuItemService.deleteMenuItem(id);
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
