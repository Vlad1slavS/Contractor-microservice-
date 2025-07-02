package io.github.contractormicroservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object для контрагента (для ответа пользователю)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ContractorDTO {

    private String id;

    @JsonProperty("parent_id")
    private String parentId;

    private String name;

    @JsonProperty("name_full")
    private String nameFull;

    private String inn;
    private String ogrn;
    private String country;
    private Long industry;

    @JsonProperty("org_form")
    private Long orgForm;


}
