package io.github.contractormicroservice.model.dto;

import io.github.contractormicroservice.model.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object для страны (для ответа пользователю)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryDTO {

    private String id;
    private String name;

    /**
     * Преобразование сущности в DTO
     * @param country
     * @return CountryDTO
     */
    public static CountryDTO fromEntity(Country country) {
        if (country == null) {
            return null;
        }

        return CountryDTO.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }

    /**
     * Преобразование списка сущностей в список DTO
     * @param countries
     * @return список DTO
     */
    public static List<CountryDTO> fromEntityList(List<Country> countries) {
        if (countries == null) {
            return null;
        }

        return countries.stream()
                .map(CountryDTO::fromEntity)
                .collect(Collectors.toList());
    }

}


