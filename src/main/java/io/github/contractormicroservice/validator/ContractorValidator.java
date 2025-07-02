package io.github.contractormicroservice.validator;

import io.github.contractormicroservice.model.dto.ContractorDTO;
import org.springframework.stereotype.Component;

@Component
public class ContractorValidator {

    public String validate(ContractorDTO contractor) {
        if (contractor == null) {
            return "Request body cannot be null";
        }

        if (contractor.getId() == null || contractor.getId().trim().isEmpty()) {
            return "Contractor ID is required";
        }

        if (contractor.getName() == null || contractor.getName().trim().isEmpty()) {
            return "Contractor name is required";
        }

        if (contractor.getCountry() == null || contractor.getCountry().trim().isEmpty()) {
            return "Contractor country is required";
        }

        if (contractor.getIndustry() == null) {
            return "Contractor industry is required";
        }

        if (contractor.getOrgForm() == null) {
            return "Contractor org form is required";
        }

        return null;
    }

}
