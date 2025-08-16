package com.konecta.internship.Restaurant_POS_System.Category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Categories")
public class CategoryController 
{
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public List<CategoryEntity> getCategories()
    {
        return categoryService.getCategories();
    }

    @PostMapping("/add")
    public CategoryEntity addCategory(@RequestBody CategoryEntity category) 
    {    
        return categoryService.addCategory(category);
    }

    @PutMapping("/update-{id}")
    public CategoryEntity updateCategory(@PathVariable int id, @RequestBody CategoryEntity category) 
    {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/delete-{id}")
    public void deleteCategory(@PathVariable int id)
    {
        categoryService.deleteCategory(id);
    }

}
