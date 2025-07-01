package io.github.contractormicroservice.controller;

import io.github.contractormicroservice.exception.EntityNotFoundException;
import io.github.contractormicroservice.model.dto.OrgFormDTO;
import io.github.contractormicroservice.service.OrgFormService;
import io.github.contractormicroservice.validator.OrgFormValidator;
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
 * Класс для работы с api организационных форм
 */
@Slf4j
@RestController
@RequestMapping("api/v1/orgForm")
public class OrgFormController {

    private final OrgFormService orgFormService;
    private final OrgFormValidator orgFormValidator;

    public OrgFormController(OrgFormService orgFormService, OrgFormValidator orgFormValidator) {
        this.orgFormService = orgFormService;
        this.orgFormValidator = orgFormValidator;
    }

    @GetMapping("/all")
    public List<OrgFormDTO> getAll() {
        log.info("Request to get all org forms");
        return orgFormService.getAllActive();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        log.info("Request to get org form by id: {}", id);
        try {
            OrgFormDTO orgFormDTO = orgFormService.getOne(id);
            return ResponseEntity.ok(orgFormDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Ошибка поиска организационной формы",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 404
                    ));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("Request to delete org form with id: {}", id);
        try {
            OrgFormDTO deletedOrgForm = orgFormService.deleteOne(id);
            log.info("Org form deleted: {}", deletedOrgForm);
            return ResponseEntity.ok(deletedOrgForm);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Ошибка удаления организационной формы",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 404
                    ));
        }
    }

    @PutMapping("/save")
    public ResponseEntity<?> save(@RequestBody OrgFormDTO orgFormDTO) {
        log.info("Validate org form: {}", orgFormDTO);
        String errorMessage = orgFormValidator.validate(orgFormDTO);
        if (errorMessage != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Ошибка при валидации организационной формы",
                            "message", errorMessage,
                            "timestamp", LocalDateTime.now(),
                            "status", 400
                    ));
        }

        log.info("Request to save org form: {}", orgFormDTO);
        try {
            OrgFormDTO savedOrgForm = orgFormService.save(orgFormDTO);
            log.info("Org form saved: {}", savedOrgForm);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrgForm);
        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Ошибка сохранения организационной формы",
                            "message", e.getMessage(),
                            "timestamp", LocalDateTime.now(),
                            "status", 400
                    ));
        }
    }

}
