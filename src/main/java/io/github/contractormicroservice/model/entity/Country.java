package io.github.contractormicroservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Country {

    private String id;

    private String name;

    @Builder.Default
    private boolean isActive = true;

}


