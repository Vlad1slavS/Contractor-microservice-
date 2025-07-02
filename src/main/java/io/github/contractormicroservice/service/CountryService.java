package io.github.contractormicroservice.service;

import io.github.contractormicroservice.exception.EntityNotFoundException;
import io.github.contractormicroservice.model.dto.CountryDTO;
import io.github.contractormicroservice.model.entity.Country;
import io.github.contractormicroservice.repository.country.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы со странами (бизнес-логика)
 */
@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryDTO> getAllActive() {
        List<Country> countries = countryRepository.findAllActive();
        return CountryDTO.fromEntityList(countries);
    }

    public CountryDTO getOne(String id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with id: " + id));
        return CountryDTO.fromEntity(country);
    }

    public CountryDTO deleteOne(String id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with id: " + id));
        country.setActive(false);
        countryRepository.save(country);
        return CountryDTO.fromEntity(country);
    }

    public CountryDTO save(CountryDTO countryDTO) {

        Optional<Country> country = countryRepository.findById(countryDTO.getId());

        Country newCountry;

        if (country.isPresent()) {
            newCountry = country.get();
            newCountry.setName(countryDTO.getName());
            newCountry.markAsExisting();
        } else {
            newCountry = Country.builder()
                    .id(countryDTO.getId())
                    .name(countryDTO.getName())
                    .build();

            newCountry.markAsNew();
        }

        Country savedCountry = countryRepository.save(newCountry);
        return CountryDTO.fromEntity(savedCountry);

    }

}


