package io.github.contractormicroservice.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.contractormicroservice.controller.CountryController;
import io.github.contractormicroservice.exception.EntityNotFoundException;
import io.github.contractormicroservice.model.dto.CountryDTO;
import io.github.contractormicroservice.service.CountryService;
import io.github.contractormicroservice.validator.CountryValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CountryControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CountryService countryService;

    @Mock
    private CountryValidator countryValidator;

    /**
     * Экземпляр контроллера (с инжектом сервиса и валидатора (@Mock))
     */
    @InjectMocks
    private CountryController countryController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
    }

    /**
     * Тест для получения списка всех активных стран
     * @throws Exception
     */
    @Test
    public void getAllCountries_ShouldReturnAllActiveCountries() throws Exception {

        List<CountryDTO> countries = Arrays.asList(
                CountryDTO.builder().id("RU").name("Россия").build(),
                CountryDTO.builder().id("CN").name("Китай").build(),
                CountryDTO.builder().id("DE").name("Германия").build()
        );

        when(countryService.getAllActive()).thenReturn(countries);

        mockMvc.perform(get("/api/v1/country/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is("RU")))
                .andExpect(jsonPath("$[0].name", is("Россия")))
                .andExpect(jsonPath("$[1].id", is("CN")))
                .andExpect(jsonPath("$[1].name", is("Китай")))
                .andExpect(jsonPath("$[2].id", is("DE")))
                .andExpect(jsonPath("$[2].name", is("Германия")));

        verify(countryService, times(1)).getAllActive();
    }

    /**
     * Тест поиска существующей страны по id
     * @throws Exception
     */
    @Test
    public void getCountryById_ShouldReturnCountryById() throws Exception {

        CountryDTO countryDTO = CountryDTO.builder()
                .id("RU")
                .name("Россия")
                .build();

        when(countryService.getOne("RU")).thenReturn(countryDTO);

        mockMvc.perform(get("/api/v1/country/{id}", "RU"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("RU")))
                .andExpect(jsonPath("$.name", is("Россия")));

        verify(countryService, times(1)).getOne("RU");
    }

    /**
     * Тест поиска несуществующей страны по id
     * @throws Exception
     */
    @Test
    void getCountryById_ShouldReturn404_WhenNotFound() throws Exception {

        when(countryService.getOne("XX"))
                .thenThrow(new EntityNotFoundException("Country not found with id: " + "XX"));

        mockMvc.perform(get("/api/v1/country/{id}", "XX"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Ошибка поиска страны")))
                .andExpect(jsonPath("$.message", is("Country not found with id: XX")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        verify(countryService, times(1)).getOne("XX");
    }

    /**
     * Тест создания новой страны
     * @throws Exception
     */
    @Test
    void saveCountry_ShouldCreateNewCountry_WhenValid() throws Exception {

        CountryDTO inputCountry = CountryDTO.builder()
                .id("FR")
                .name("Франция")
                .build();

        when(countryValidator.validate(any(CountryDTO.class))).thenReturn(null);
        when(countryService.save(any(CountryDTO.class))).thenReturn(inputCountry);

        mockMvc.perform(put("/api/v1/country/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputCountry)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("FR")))
                .andExpect(jsonPath("$.name", is("Франция")));

        verify(countryValidator, times(1))
                .validate(any(CountryDTO.class));
        verify(countryService, times(1))
                .save(any(CountryDTO.class));
    }

    /**
     * Тест обновления страны по null id
     * @throws Exception
     */
    @Test
    void saveCountry_ShouldReturn400_WhenValidationFails() throws Exception {

        CountryDTO invalidCountry = CountryDTO.builder()
                .id("")
                .name("")
                .build();

        when(countryValidator.validate(any(CountryDTO.class)))
                .thenReturn("Country ID and name are required");

        mockMvc.perform(put("/api/v1/country/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCountry)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Ошибка при валидации страны")))
                .andExpect(jsonPath("$.message", is("Country ID and name are required")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        verify(countryValidator, times(1))
                .validate(any(CountryDTO.class));
        verify(countryService, never())
                .save(any(CountryDTO.class));
    }

    /**
     * Тест логического удаления страны по id
     * @throws Exception
     */
    @Test
    public void deleteCountryById_ShouldDeleteCountryById() throws Exception {

        CountryDTO deletedCountry = CountryDTO.builder()
                .id("RU")
                .name("Россия")
                .build();

        when(countryService.deleteOne("RU")).thenReturn(deletedCountry);

        mockMvc.perform(delete("/api/v1/country/delete/{id}", "RU"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("RU")))
                .andExpect(jsonPath("$.name", is("Россия")));

        verify(countryService, times(1)).deleteOne("RU");
    }

}


