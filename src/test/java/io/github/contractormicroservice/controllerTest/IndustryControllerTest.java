package io.github.contractormicroservice.controllerTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.contractormicroservice.controller.IndustryController;
import io.github.contractormicroservice.exception.EntityNotFoundException;
import io.github.contractormicroservice.model.dto.IndustryDTO;
import io.github.contractormicroservice.service.IndustryService;
import io.github.contractormicroservice.validator.IndustryValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class IndustryControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private IndustryService industryService;

    @Mock
    private IndustryValidator industryValidator;

    /**
     * Экземпляр контроллера (с инжектом сервиса и валидатора (@Mock))
     */
    @InjectMocks
    private IndustryController industryController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(industryController).build();
    }

    /**
     * Тест для получения списка всех активных индустриальных кодов
     * @throws Exception
     */
    @Test
    public void getAllIndustries_ShouldReturnAllActiveIndustries() throws Exception {

        List<IndustryDTO> industries = Arrays.asList(
                IndustryDTO.builder().id(1L).name("Энергетика").build(),
                IndustryDTO.builder().id(2L).name("Авиастроение").build(),
                IndustryDTO.builder().id(3L).name("Судостроение").build()
        );

        when(industryService.getAllActive()).thenReturn(industries);

        mockMvc.perform(get("/api/v1/industries/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Энергетика")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Авиастроение")))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].name", is("Судостроение")));

        verify(industryService, times(1)).getAllActive();
    }

    /**
     * Тест поиска существующего индустриального кода по id
     * @throws Exception
     */
    @Test
    public void getIndustryById_ShouldReturnIndustryById() throws Exception {

        IndustryDTO industryDTO = IndustryDTO.builder()
                .id(1L)
                .name("Авиастроение")
                .build();

        when(industryService.getOne(1L)).thenReturn(industryDTO);

        mockMvc.perform(get("/api/v1/industries/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Авиастроение")));

        verify(industryService, times(1)).getOne(1L);
    }

    /**
     * Тест поиска несуществующего индустриального кода по id
     * @throws Exception
     */
    @Test
    void getIndustryById_ShouldReturn404_WhenNotFound() throws Exception {

        when(industryService.getOne(1000L))
                .thenThrow(new EntityNotFoundException("Industry not found with id: " + 1000));

        mockMvc.perform(get("/api/v1/industries/{id}", 1000))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("Ошибка поиска индустриального кода")))
                .andExpect(jsonPath("$.message", is("Industry not found with id: 1000")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.timestamp", notNullValue()));

        verify(industryService, times(1)).getOne(1000L);
    }

    /**
     * Тест создания нового индустриального кода
     * @throws Exception
     */
    @Test
    void saveIndustry_ShouldCreateNewIndustry_WhenValid() throws Exception {

        IndustryDTO inputIndustry = IndustryDTO.builder()
                .id(1L)
                .name("Авиастроение")
                .build();

        when(industryValidator.validate(any(IndustryDTO.class))).thenReturn(null);
        when(industryService.save(any(IndustryDTO.class))).thenReturn(inputIndustry);

        mockMvc.perform(put("/api/v1/industries/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputIndustry)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Авиастроение")));

        verify(industryValidator, times(1))
                .validate(any(IndustryDTO.class));
        verify(industryService, times(1))
                .save(any(IndustryDTO.class));
    }

    /**
     * Тест логического удаления индустриального кода по id
     * @throws Exception
     */
    @Test
    public void deleteIndustryById_ShouldDeleteIndustryById() throws Exception {

        IndustryDTO deletedIndustry = IndustryDTO.builder()
                .id(1L)
                .name("Авиастроение")
                .build();

        when(industryService.deleteOne(1L)).thenReturn(deletedIndustry);

        mockMvc.perform(delete("/api/v1/industries/delete/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Авиастроение")));

            verify(industryService, times(1)).deleteOne(1L);
    }

}
