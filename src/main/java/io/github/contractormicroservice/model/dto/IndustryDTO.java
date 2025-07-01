package io.github.contractormicroservice.model.dto;

import io.github.contractormicroservice.model.entity.Industry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object для индустриального кода (для ответа пользователю)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndustryDTO {

    private Long id;
    private String name;

    public static IndustryDTO fromEntity(Industry industry) {
        if (industry == null) {
            return null;
        }

        return IndustryDTO.builder()
                .id(industry.getId())
                .name(industry.getName())
                .build();
    }

    public static List<IndustryDTO> fromEntityList(List<Industry> industries) {
        if (industries == null) {
            return null;
        }

        return industries.stream()
                .map(IndustryDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
