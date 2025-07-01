package io.github.contractormicroservice.model.dto;

import io.github.contractormicroservice.model.entity.OrgForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object для организационной формы (для ответа пользователю)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrgFormDTO {

    private Long id;
    private String name;

    public static OrgFormDTO fromEntity(OrgForm orgForm) {
        if (orgForm == null) {
            return null;
        }

        return OrgFormDTO.builder()
                .id(orgForm.getId())
                .name(orgForm.getName())
                .build();
    }

    public static List<OrgFormDTO> fromEntityList(List<OrgForm> orgForms) {
        if (orgForms == null) {
            return null;
        }

        return orgForms.stream()
                .map(OrgFormDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
