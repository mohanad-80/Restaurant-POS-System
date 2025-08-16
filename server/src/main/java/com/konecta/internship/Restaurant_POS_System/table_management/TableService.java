package com.konecta.internship.Restaurant_POS_System.table_management;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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

    private TableResponseDto saveTable(TableRequestDto dto, DiningTable table) {
        table.setTableNumber(dto.getTableNumber());
        table.setSeats(dto.getSeats());
        table.setSection(dto.getSection());
        String status = dto.getStatus();
        if(status != null)
            table.setStatus(TableStatus.valueOf(status));
        tableRepository.save(table);
        return new TableResponseDto(table);
    }

    public TableResponseDto createNewTable(TableRequestDto dto) {
        if(tableRepository.existsByTableNumber(dto.getTableNumber())){
            throw new EntityExistsException(dto.getTableNumber());
        }
        DiningTable table = new DiningTable();
        return saveTable(dto, table);
    }

    public TableResponseDto updateExistingTable(Long id, TableRequestDto dto) {
        if(tableRepository.existsByTableNumber(dto.getTableNumber())){
            throw new EntityExistsException(dto.getTableNumber());
        }
        DiningTable table = tableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        return saveTable(dto, table);
    }

    public void deleteTable(Long id) {
        DiningTable table = tableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        tableRepository.delete(table);
    }
}
