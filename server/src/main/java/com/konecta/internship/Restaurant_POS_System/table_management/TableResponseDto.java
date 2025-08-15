package com.konecta.internship.Restaurant_POS_System.table_management;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TableResponseDto {
    private Long id;
    private String tableNumber;
    private int seats;
    private String section;
    private TableStatus status;

    public TableResponseDto(DiningTable table) {
        this.id = table.getId();
        this.tableNumber = table.getTableNumber();
        this.seats = table.getSeats();
        this.section = table.getSection();
        this.status = table.getStatus();
    }
}
