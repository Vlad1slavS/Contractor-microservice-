package io.github.contractormicroservice.validator;

import io.github.contractormicroservice.model.dto.IndustryDTO;
import org.springframework.stereotype.Component;

/**
 * Валидация входных данных индустриальных кодов (обязательные поля)
 */
@Component
public class IndustryValidator {

    public String validate(IndustryDTO industryDTO) {
        if (industryDTO == null) {
            return "Request body cannot be null";
        }

        if (industryDTO.getName() == null || industryDTO.getName().trim().isEmpty()) {
            return "Industry name is required";
        }

        return null;
    }

}
