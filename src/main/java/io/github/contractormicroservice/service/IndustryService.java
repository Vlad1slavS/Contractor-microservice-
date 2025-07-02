package io.github.contractormicroservice.service;

import io.github.contractormicroservice.exception.EntityNotFoundException;
import io.github.contractormicroservice.model.dto.IndustryDTO;
import io.github.contractormicroservice.model.entity.Industry;
import io.github.contractormicroservice.repository.industry.IndustryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с индустриальными кодами (бизнес-логика)
 */
@Service
public class IndustryService {

    private final IndustryRepository industryRepository;

    public IndustryService(IndustryRepository industryRepository) {
        this.industryRepository = industryRepository;
    }

    public List<IndustryDTO> getAllActive() {
        List<Industry> industries = industryRepository.findAllActive();
        return IndustryDTO.fromEntityList(industries);
    }

    public IndustryDTO getOne(Long id) {
        Industry industry = industryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Industry not found with id: " + id));
        return IndustryDTO.fromEntity(industry);
    }

    public IndustryDTO deleteOne(Long id) {
        Industry industry = industryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Industry not found with id: " + id));
        industry.setActive(false);
        industryRepository.save(industry);
        return IndustryDTO.fromEntity(industry);
    }

    public IndustryDTO save(IndustryDTO industryDTO) {

        Industry newIndustry;

        if (industryDTO.getId() != null) {
            Optional<Industry> existingIndustry = industryRepository.findById(industryDTO.getId());

            if (existingIndustry.isPresent()) {
                newIndustry = existingIndustry.get();
                newIndustry.setName(industryDTO.getName());
                newIndustry.markAsExisting();
            } else {
                throw new EntityNotFoundException("Industry not found with id: " + industryDTO.getId());
            }
        } else {
            newIndustry = Industry.builder()
                    .name(industryDTO.getName())
                    .build();

            newIndustry.markAsNew();
        }

        Industry savedIndustry = industryRepository.save(newIndustry);
        return IndustryDTO.fromEntity(savedIndustry);

    }

}
