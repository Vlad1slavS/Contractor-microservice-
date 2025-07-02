package io.github.contractormicroservice.service;

import io.github.contractormicroservice.exception.EntityNotFoundException;
import io.github.contractormicroservice.model.dto.OrgFormDTO;
import io.github.contractormicroservice.model.entity.OrgForm;
import io.github.contractormicroservice.repository.orgForm.OrgFormRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с организационными формами (бизнес-логика)
 */
@Service
public class OrgFormService {

    private final OrgFormRepository orgFormRepository;

    public OrgFormService(OrgFormRepository orgFormRepository) {
        this.orgFormRepository = orgFormRepository;
    }

    public List<OrgFormDTO> getAllActive() {
        List<OrgForm> orgForms = orgFormRepository.findAllActive();
        return OrgFormDTO.fromEntityList(orgForms);
    }

    public OrgFormDTO getOne(Long id) {
        OrgForm orgForm = orgFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrgForm not found with id: " + id));
        return OrgFormDTO.fromEntity(orgForm);
    }

    public OrgFormDTO deleteOne(Long id) {
        OrgForm orgForm = orgFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrgForm not found with id: " + id));
        orgForm.setActive(false);
        orgFormRepository.save(orgForm);
        return OrgFormDTO.fromEntity(orgForm);
    }

    public OrgFormDTO save(OrgFormDTO orgFormDTO) {

        OrgForm newOrgForm;

        if (orgFormDTO.getId() != null) {
            Optional<OrgForm> existingOrgForm = orgFormRepository.findById(orgFormDTO.getId());

            if (existingOrgForm.isPresent()) {
                newOrgForm = existingOrgForm.get();
                newOrgForm.setName(orgFormDTO.getName());
                newOrgForm.markAsExisting();
            } else {
                throw new EntityNotFoundException("Org form not found with id: " + orgFormDTO.getId());
            }
        } else {
            newOrgForm = OrgForm.builder()
                    .name(orgFormDTO.getName())
                    .build();

            newOrgForm.markAsNew();
        }

        OrgForm savedOrgForm = orgFormRepository.save(newOrgForm);
        return OrgFormDTO.fromEntity(savedOrgForm);

    }

}

