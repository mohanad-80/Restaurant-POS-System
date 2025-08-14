package com.konecta.internship.Restaurant_POS_System.MenuItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.konecta.internship.Restaurant_POS_System.Category.CategoryEntity;
import com.konecta.internship.Restaurant_POS_System.Category.CategoryRepository;

@Service
public class MenuItemService 
{
    @Autowired
    private MenuItemRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public List<MenuItemEntity> getMenuItmes() {
        return repository.findAll();
    }

    public MenuItemEntity addMenuItem(MenuItemEntity menuItem) {
        return repository.save(menuItem);
    }

    public MenuItemEntity updateMenuItem(int id, Map<String, String> updates) {
        MenuItemEntity item = repository.findById(id).orElseThrow();
        updates.forEach((field, value) -> {
            switch (field) {
                case "name":
                    item.setName(value);
                    break;
                case "price":
                    item.setPrice(Double.parseDouble(value));
                    break;
                case "preperation_time":
                    item.setPreperation_time(Integer.parseInt(value));
                    break;
                case "status":
                    item.setStatus(MenuItemEntity.Status.valueOf(value.toUpperCase()));
                    break;
                case "image_path":
                    item.setImage_path(value);
                    break;
                case "created_at":
                    if (value.contains("T")) {
                        item.setCreated_at(LocalDateTime.parse(value));
                    } else {
                        item.setCreated_at(LocalDate.parse(value).atStartOfDay());
                    }
                    break;
                case "category_id":
                    CategoryEntity category = categoryRepository.findById(Integer.parseInt(value)).orElseThrow();
                    item.setCategory(category);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + field);
            }
        });

        return repository.save(item);
    }

    public void deleteMenuItem(int id) 
    {
        boolean isExcist = repository.findById(id).isPresent();
        if (!isExcist) {
            throw new IllegalAccessError("OOPS...This item not found");
        } else {
            repository.deleteById(id);
        }
    }

    public ResponseEntity<String> uploadImage(int id, MultipartFile image)
    {
        try 
        {
            MenuItemEntity item = repository.findById(id).orElseThrow(); 

            if (item.getImage_path() != null) {
                Path oldFilePath = Paths.get(item.getImage_path().replaceFirst("^/", ""));
                try 
                {
                    Files.deleteIfExists(oldFilePath);
                } 
                catch (IOException e) 
                {
                    System.err.println("Could not delete old image: " + oldFilePath);
                }
        }

        String imagePath = fileStorageService.storeFile(image, id);
        item.setImage_path(imagePath);
        repository.save(item);

        return ResponseEntity.ok("Image uploaded successfully: " + imagePath);
    } 
    catch (Exception e) 
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
    }

    }
}
