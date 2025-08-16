package com.konecta.internship.Restaurant_POS_System.MenuItem;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.konecta.internship.Restaurant_POS_System.Category.CategoryEntity;
import com.konecta.internship.Restaurant_POS_System.Category.CategoryRepository;

@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public List<MenuItemEntity> getMenuItems(Long categoryId, String status) 
    {
        MenuItemEntity.Status statusEnum = null;
        if (status != null) 
        {
            statusEnum = MenuItemEntity.Status.valueOf(status.toUpperCase());
        }
        if (categoryId != null && statusEnum != null) 
        {
            return repository.findByCategoryIdAndStatus(categoryId, statusEnum);
        } 
        else if (categoryId != null) 
        {
            return repository.findByCategoryId(categoryId);
        } 
        else if (statusEnum != null) 
        {
            return repository.findByStatus(statusEnum);
        } 
        else 
        {
            return repository.findAll();
        }
    }

    public MenuItemEntity getMenuItemById(Long id)
    {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Menu item with id " + id + " not found"));
    }

    public MenuItemEntity addMenuItem(MenuItemDTO dto) 
    {
        MenuItemEntity entity = new MenuItemEntity();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setPreparation_time(dto.getPreparation_time());
        entity.setImage_path(dto.getImage_path());
        entity.setStatus(dto.getStatus());  
        entity.setCreated_at(LocalDateTime.now());

        if (dto.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new NoSuchElementException("Category not found"));
            entity.setCategory(category);
        }

        return repository.save(entity); 
    }       

    public MenuItemEntity updateMenuItem(Long id, Map<String, String> updates) 
    {
        MenuItemEntity item = repository.findById(id).orElseThrow(()-> new NoSuchElementException("Menu item with id " + id + " not found"));
        updates.forEach((field, value) -> {
            switch (field) {
                case "name":
                    item.setName(value);
                    break;
                case "price":
                    item.setPrice(new BigDecimal(value));
                    break;
                case "preparation_time":
                    item.setPreparation_time(Integer.parseInt(value));
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
                    CategoryEntity category = categoryRepository.findById(Long.parseLong(value)).orElseThrow(() -> new NoSuchElementException("Category with id " + value + " not found"));
                    item.setCategory(category);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + field);
            }
        });

        return repository.save(item);
    }

    public void deleteMenuItem(Long id) 
    {
        boolean isExist = repository.findById(id).isPresent();
        if (!isExist) 
        {
            throw new NoSuchElementException("OOPS...This item not found");
        } 
        else 
        {
            repository.deleteById(id);
        }
    }

    public ResponseEntity<String> uploadImage(Long id, MultipartFile image) 
    {
        try 
        {
            MenuItemEntity item = repository.findById(id).orElseThrow(()-> new NoSuchElementException("Menu item with id " + id + " not found"));

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
    
    public BigDecimal getMenuItemPrice(Long id)
    {
        MenuItemEntity item= repository.findById(id).orElseThrow(()-> new NoSuchElementException("Menu item with id " + id + " not found"));
        return item.getPrice();

    }
}
