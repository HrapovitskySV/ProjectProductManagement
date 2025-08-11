package ru.otus.hw.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.dto.InfoSystemDto;
import ru.otus.hw.services.InfoSystemService;

import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = InfoSystemRestController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)// отключаю  Security в функциональных тестах
class InfoSystemRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private InfoSystemService infoSystemService;


    private final List<InfoSystem> infoSystems = List.of(new InfoSystem(1L, "InfoSystem_1",false,"",null),
            new InfoSystem(2L, "InfoSystem2",false,"",null));



    @Test
    void listAllInfoSystems() throws Exception {
        when(infoSystemService.findAll()).thenReturn(infoSystems);
        mvc.perform(get("/api/infosystems"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(infoSystems)));
    }

    @Test
    void getInfoSystemTest() throws Exception {
        var infoSystem = infoSystems.get(0);
        when(infoSystemService.findById(1L)).thenReturn(Optional.of(infoSystem));
        mvc.perform(get("/api/infosystems/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(infoSystem)));
    }


    @Test
    void shouldCorrectSaveNewInfoSystem() throws Exception {
        InfoSystemDto infoSystemDto = new InfoSystemDto(2L, "InfoSystem_3",false,"",0);
        InfoSystem infoSystem = new InfoSystem(2L, "InfoSystem_3",false,"",null);
        given(infoSystemService.insert(any())).willReturn(infoSystem);
        String expectedResult = mapper.writeValueAsString(infoSystem);

        mvc.perform(post("/api/infosystems").contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResult));

    }


}