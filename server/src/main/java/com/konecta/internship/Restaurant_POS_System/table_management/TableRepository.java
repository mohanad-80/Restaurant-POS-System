package com.konecta.internship.Restaurant_POS_System.table_management;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<DiningTable, Long> {
    boolean existsByTableNumber(@NotBlank String tableNumber);

    List<DiningTable> findByStatus(TableStatus status);
}
