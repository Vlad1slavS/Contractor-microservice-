package io.github.contractormicroservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contractor {

    private String id;
    private String parentId;
    private String name;
    private String nameFull;
    private String inn;
    private String ogrn;
    private String country;
    private Integer industry;
    private Integer orgForm;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String createUserId;
    private String modifyUserId;

    @Builder.Default
    private Boolean isActive = true;

}
