package com.konecta.internship.Restaurant_POS_System.table_management;

import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {
    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public List<TableResponseDto> fetchAllTables() {
        return tableRepository.findAll()
                .stream()
                .map(TableResponseDto::new)
                .toList();
    }

    public TableResponseDto createNewTable(TableRequestDto dto) {
        if(tableRepository.existsByTableNumber(dto.getTableNumber())){
            throw new EntityExistsException(dto.getTableNumber());
        }
        DiningTable table = new DiningTable();
        table.setTableNumber(dto.getTableNumber());
        table.setSeats(dto.getSeats());
        table.setSection(dto.getSection());
        TableStatus status = TableStatus.valueOf(dto.getStatus());
        table.setStatus(status);

        tableRepository.save(table);
        return new TableResponseDto(table);
    }
}
