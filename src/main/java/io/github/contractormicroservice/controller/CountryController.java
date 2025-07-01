package io.github.contractormicroservice.controller;

import io.github.contractormicroservice.exception.EntityNotFoundException;
import io.github.contractormicroservice.model.dto.CountryDTO;
import io.github.contractormicroservice.service.CountryService;
import io.github.contractormicroservice.validator.CountryValidator;
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
 * Контроллер для работы с api стран
 */
@RestController
@RequestMapping("/api/v1/country")
@Slf4j
public class CountryController {

    private final CountryService countryService;
    private final CountryValidator countryValidator;

    public CountryController(CountryService countryService, CountryValidator countryValidator) {
        this.countryService = countryService;
        this.countryValidator = countryValidator;
    }

    /**
     * Получение всех активных стран
     * @return список активных стран
     */
    @GetMapping("/all")
    public List<CountryDTO> getAll() {
        log.info("Request to get all countries");
        return countryService.getAllActive();
    }

    /**
     * Получение страны по id
     * @param id
     * @return статус и DTO страны
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        log.info("Request to get country by id: {}", id);
        try {
            CountryDTO country = countryService.getOne(id);
            return ResponseEntity.ok(country);
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

    /**
     * Логическое удаление страны по id
     * @param id
     * @return статус и DTO страны
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        log.info("Request to delete country with id: {}", id);
        try {
            CountryDTO deletedCountry = countryService.deleteOne(id);
            log.info("Country deleted: {}", deletedCountry);
            return ResponseEntity.ok(deletedCountry);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Ошибка удаления страны",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 404
                    ));
        }
    }

    /**
     * Сохранение страны при существующем id
     * Создание новой страны при отсутствии
     * @param countryDTO
     * @return
     */
    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody CountryDTO countryDTO) {
        log.info("Validate country: {}", countryDTO);
        String errorMessage = countryValidator.validate(countryDTO);
        if (errorMessage != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Ошибка при валидации страны",
                            "message", errorMessage,
                            "timestamp", LocalDateTime.now(),
                            "status", 400
                    ));
        }

        log.info("Request to save country: {}", countryDTO);

        CountryDTO savedCountry = countryService.save(countryDTO);
        log.info("Country saved: {}", savedCountry);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCountry);

    }

}

