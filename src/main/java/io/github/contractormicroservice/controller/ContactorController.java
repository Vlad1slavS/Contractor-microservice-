package io.github.contractormicroservice.controller;

import io.github.contractormicroservice.exception.EntityNotFoundException;
import io.github.contractormicroservice.model.dto.ContractorDTO;
import io.github.contractormicroservice.model.entity.Contractor;
import io.github.contractormicroservice.model.entity.ContractorSearch;
import io.github.contractormicroservice.model.entity.Pagination;
import io.github.contractormicroservice.service.ContractorService;
import io.github.contractormicroservice.validator.ContractorValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Контроллер для работы с api контрагентов
 */
@RestController
@RequestMapping("/api/v1/contractor")
@Slf4j
public class ContactorController {

    private final ContractorService contractorService;

    private final ContractorValidator contractorValidator;

    public ContactorController(ContractorService contractorService, ContractorValidator contractorValidator) {
        this.contractorService = contractorService;
        this.contractorValidator = contractorValidator;
    }


    /**
     * Получение контрагента по id
     * @param id
     * @return статус и DTO страны
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        log.info("Request to get contractor by id: {}", id);
        try {
            Contractor contractor = contractorService.getOne(id);
            return ResponseEntity.ok(contractor);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Ошибка поиска страны",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 404
                    ));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        log.info("Request to delete contractor with id: {}", id);
        try {
            Contractor deletedCountry = contractorService.deleteOne(id);
            log.info("Contractor deleted: {}", deletedCountry);
            return ResponseEntity.ok(deletedCountry);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Ошибка удаления контрагента",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 404
                    ));
        }
    }

    @PutMapping("/save")
    public ResponseEntity<?> saveContractor(@RequestBody ContractorDTO contractorDto) {
        log.info("Validate contractor: {}", contractorDto);

        String errorMessage = contractorValidator.validate(contractorDto);
        if (errorMessage != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Ошибка при валидации контрагента",
                            "message", errorMessage,
                            "timestamp", LocalDateTime.now(),
                            "status", 400
                    ));
        }

        log.info("Request to save contractor: {}", contractorDto);

        try {
            Contractor savedContractor = contractorService.save(contractorDto);
            log.info("Contractor saved: {}", savedContractor);
            return ResponseEntity.ok(savedContractor);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Ошибка сохранения контрагента",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 400
                    ));
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchContractors(@RequestBody(required = false) ContractorSearch searchRequest,
                                               @RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer limit) {
        try {

            log.info("Request to search contractors with search request: {}", searchRequest);
            Pagination pagination = contractorService.searchContractors(searchRequest, page, limit);
            log.info("Contractors found: {}", pagination.getContractors().size());
            return ResponseEntity.ok(pagination);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Ошибка поиска контрагентов",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 400
                    ));
        }
    }

}
