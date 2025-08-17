package com.konecta.internship.Restaurant_POS_System.MenuItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity,Long> 
{
    List<MenuItemEntity> findByCategoryIdAndStatus(Long categoryId, MenuItemEntity.Status status);
    List<MenuItemEntity> findByCategoryId(Long categoryId);
    List<MenuItemEntity> findByStatus(MenuItemEntity.Status status);
}
