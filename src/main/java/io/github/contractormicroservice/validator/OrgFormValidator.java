package io.github.contractormicroservice.validator;

import io.github.contractormicroservice.model.dto.OrgFormDTO;
import org.springframework.stereotype.Component;

/**
 * Валидация входных данных организационных форм (обязательные поля)
 */
@Component
public class OrgFormValidator {

    public String validate(OrgFormDTO orgFormDTO) {
        if (orgFormDTO == null) {
            return "Request body cannot be null";
        }

        if (orgFormDTO.getName() == null || orgFormDTO.getName().trim().isEmpty()) {
            return "Industry name is required";
        }

        return null;
    }

}
