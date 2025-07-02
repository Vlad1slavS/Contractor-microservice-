package io.github.contractormicroservice.validator;

import io.github.contractormicroservice.model.dto.CountryDTO;
import org.springframework.stereotype.Component;

/**
 * Валидация входных данных страны (обязательные поля)
 */
@Component
public class CountryValidator {

    public String validate(CountryDTO country) {
        if (country == null) {
            return "Request body cannot be null";
        }

        if (country.getId() == null || country.getId().trim().isEmpty()) {
            return "Country ID is required";
        }

        if (country.getName() == null || country.getName().trim().isEmpty()) {
            return "Country name is required";
        }

        return null;
    }

}

