package com.konecta.internship.Restaurant_POS_System.table_management.entity;

import com.konecta.internship.Restaurant_POS_System.table_management.enums.TableStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "dining_tables")
public class DiningTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "table_number", nullable = false, unique = true)
    private String tableNumber;
    @Column(name = "seats", nullable = false)
    private int seats;
    @Column(name = "section")
    private String section;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TableStatus status = TableStatus.AVAILABLE;
}
