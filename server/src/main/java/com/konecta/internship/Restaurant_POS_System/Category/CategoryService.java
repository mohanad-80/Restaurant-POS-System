package com.konecta.internship.Restaurant_POS_System.Category;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService 
{
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryEntity> getCategories()
    {
        return categoryRepository.findAll();
    }

    public CategoryEntity addCategory(CategoryDTO dto) 
    {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(dto.getName());
        return categoryRepository.save(entity);
    }

    public CategoryEntity updateCategory(Long id, CategoryDTO dto) 
    {
        CategoryEntity category = categoryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Category with id " + id + " not found"));

        category.setName(dto.getName());
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id)
    {
        boolean isExist=categoryRepository.findById(id).isPresent();
        if(!isExist)
        {
            throw new java.util.NoSuchElementException("Category with id " + id + " not found");
        }
        else
        {
            categoryRepository.deleteById(id);
        }
    }

}
