package ru.otus.hw.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.dto.InfoSystemDto;
import ru.otus.hw.services.InfoSystemService;
import ru.otus.hw.services.ProductService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(value = InfosystemPagesController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)// отключаю  Security в функциональных тестах
@Import({GlobalExceptionHandler.class})//, AuthorService.class, GenreService.class
class InfosystemPagesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private InfoSystemService infoSystemService;

    @MockBean
    private ProductService productService;

    private final List<InfoSystemDto> infoSystems = List.of(new InfoSystemDto(1L, "1C-Бухалтерия",false,"",0),
             new InfoSystemDto(1L, "Документооборот",false,"",0));


    @Test
    void listAllPage() throws Exception {
        mvc.perform(get("/infosystems"))
                .andExpect(view().name("infoSystemList"));

    }

    @Test
    void editPage() throws Exception {
        InfoSystemDto infoSystem = infoSystems.get(0);
        when(infoSystemService.findByIdDto(1L)).thenReturn(Optional.of(infoSystem));
        mvc.perform(get("/infosystems/edit/1"))
            .andExpect(view().name("infoSystemEdit"))
            .andExpect(model().attribute("infoSystem", infoSystem));

    }

    @Test
    void shouldRenderErrorPageWhenAuthorNotFound() throws Exception {
        when(infoSystemService.findByIdDto(1L)).thenReturn(Optional.empty());
        //when(infoSystemService.findByIdDto(1L)).thenThrow(new InfoSystemNotFoundException());
        mvc.perform(get("/infosystems/edit/1"))
                .andExpect(view().name("customError"));
    }



}