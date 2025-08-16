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
            throw new EntityExistsException("table (" + dto.getTableNumber() + ") already exists");
        }
        DiningTable table = new DiningTable();
        return saveTable(dto, table);
    }

    public TableResponseDto updateExistingTable(Long id, TableRequestDto dto) {
        DiningTable table = tableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No table is found with id: " + id));
        if(!table.getTableNumber().equals(dto.getTableNumber()) && tableRepository.existsByTableNumber(dto.getTableNumber())){
            throw new EntityExistsException("table (" + dto.getTableNumber() + ") already exists");
        }
        return saveTable(dto, table);
    }

    public void deleteTable(Long id) {
        DiningTable table = tableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No table is found with id: " + id));
        tableRepository.delete(table);
    }

    public List<TableResponseDto> filterTablesByStatus(String statusParam) {
        TableStatus status;
        try {
            status = TableStatus.valueOf(statusParam.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusException("must be in (AVAILABLE, OCCUPIED, RESERVED or OUT_OF_SERVICE)");
        }
        return tableRepository.findByStatus(status)
                .stream()
                .map(TableResponseDto::new)
                .toList();
    }
}
