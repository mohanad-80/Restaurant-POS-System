package com.konecta.internship.Restaurant_POS_System.MenuItem;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/MenuItems")
public class MenuItemController 
{
     @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/all")
    public List<MenuItemEntity> getMenuItems() 
    {
        return menuItemService.getMenuItmes();   
    }

    @PostMapping("/add")
    public MenuItemEntity addMenuItem(@RequestBody MenuItemEntity menuItem)
    {
        return menuItemService.addMenuItem(menuItem);
    }

    @PutMapping("/update-{id}")
    public MenuItemEntity updateMenuItem (@PathVariable int id, @RequestParam Map<String, String> updates) 
    {    
        return menuItemService.updateMenuItem(id, updates);
    }
    
    @DeleteMapping("/delete-{id}")
    public void deleteMenuItem(@PathVariable int id)
    {
        menuItemService.deleteMenuItem(id);
    }

    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<String> uploadImage(@PathVariable int id, @RequestParam("image") MultipartFile image)
    {
        return menuItemService.uploadImage(id, image);
    }
}
