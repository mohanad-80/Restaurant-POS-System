package com.konecta.internship.Restaurant_POS_System.Category;

import java.util.List;

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

    public CategoryEntity addCategory(CategoryEntity category)
    {
        return categoryRepository.save(category);
    }

    public CategoryEntity updateCategory(Long id,CategoryEntity category)
    {
        CategoryEntity categ= categoryRepository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Category with id " + id + " not found"));
        categ.setName(category.getName());
        categoryRepository.save(categ);
        return categ;
    }

    public void deleteCategory(Long id)
    {
        boolean isExcist=categoryRepository.findById(id).isPresent();
        if(!isExcist)
        {
            throw new java.util.NoSuchElementException("Category with id " + id + " not found");
        }
        else
        {
            categoryRepository.deleteById(id);
        }
    }

}
