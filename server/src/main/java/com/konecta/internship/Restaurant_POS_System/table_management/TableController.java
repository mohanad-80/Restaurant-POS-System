package com.konecta.internship.Restaurant_POS_System.table_management;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/tables")
public class TableController {
    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    public ResponseEntity<List<TableResponseDto>> getTables() {
        List<TableResponseDto> dtos = tableService.fetchAllTables();
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @PostMapping
    public ResponseEntity<TableResponseDto> addTable(@Valid @RequestBody TableRequestDto requestDto) {
        TableResponseDto response = tableService.createNewTable(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Entity already exists exception handler
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorDto> entityAlreadyExistsHandler(EntityExistsException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("tableNumber", "table (" + e.getMessage() + ") already exists");
        ErrorDto error = new ErrorDto("Entity already exists");
        error.setErrors(errors);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // Request validation exceptions handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );
        ErrorDto error = new ErrorDto("Invalid request content");
        error.setErrors(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
