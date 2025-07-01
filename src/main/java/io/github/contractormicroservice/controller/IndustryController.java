package io.github.contractormicroservice.controller;

import io.github.contractormicroservice.exception.EntityNotFoundException;
import io.github.contractormicroservice.model.dto.IndustryDTO;
import io.github.contractormicroservice.service.IndustryService;
import io.github.contractormicroservice.validator.IndustryValidator;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для работы с api индустриальных кодов
 */
@Slf4j
@RestController
@RequestMapping("api/v1/industries")
public class IndustryController {

    private final IndustryService industryService;
    private final IndustryValidator industryValidator;

    public IndustryController(IndustryService industryService, IndustryValidator industryValidator) {
        this.industryService = industryService;
        this.industryValidator = industryValidator;
    }

    @GetMapping("/all")
    public List<IndustryDTO> getAll() {
        log.info("Request to get all industries");
        return industryService.getAllActive();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        log.info("Request to get industry by id: {}", id);
        try {
            IndustryDTO industryDTO = industryService.getOne(id);
            return ResponseEntity.ok(industryDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Ошибка поиска индустриального кода",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 404
                    ));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("Request to delete industry with id: {}", id);
        try {
            IndustryDTO deletedIndustry = industryService.deleteOne(id);
            log.info("Industry deleted: {}", deletedIndustry);
            return ResponseEntity.ok(deletedIndustry);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Ошибка удаления индустриального кода",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 404
                    ));
        }
    }

    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody IndustryDTO industryDTO) {
        log.info("Validate industry: {}", industryDTO);
        String errorMessage = industryValidator.validate(industryDTO);
        if (errorMessage != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Ошибка при валидации индустриального кода",
                            "message", errorMessage,
                            "timestamp", LocalDateTime.now(),
                            "status", 400
                    ));
        }

        log.info("Request to save industry: {}", industryDTO);
        try {
            IndustryDTO savedIndustry = industryService.save(industryDTO);
            log.info("Industry saved: {}", savedIndustry);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedIndustry);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Ошибка сохранения индустриального кода",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 400
                    ));
        }
    }

}
