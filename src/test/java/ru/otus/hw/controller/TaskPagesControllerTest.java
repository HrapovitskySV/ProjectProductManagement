package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.exceptions.TaskNotFoundException;
import ru.otus.hw.models.*;
import ru.otus.hw.models.dto.TaskDto;
import ru.otus.hw.models.dto.UserToListDto;
import ru.otus.hw.services.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = TaskPagesController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)// отключаю  Security в функциональных тестах
//@Import({BookConverter.class, AuthorConverter.class, GenreConverter.class, BookConverter.class})//, AuthorService.class, GenreService.class
class TaskPagesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private ProductService productService;

    @MockBean
    private InfoSystemService infoSystemService;

    @MockBean
    private TaskTypeService taskTypeService;

    @MockBean
    private UserService userService;

    @MockBean
    private LoadTaskFomExcelService loadTaskFomExcelService;


    private final List<TaskDto> tasksDto = new ArrayList<>();

    @BeforeEach
    void initData(){
        TaskDto task1= new TaskDto();
        task1.setId(1L);
        task1.setAuthor(new UserToListDto(1, "USER","",false));
        tasksDto.add(task1);

        TaskDto task2= new TaskDto();
        task2.setId(2L);
        task2.setAuthor(new UserToListDto(1, "USER","",false));
        tasksDto.add(task2);
    }



    @Test
    void listAllTask() throws Exception {
        mvc.perform(get("/tasks"))
                .andExpect(view().name("taskList"));
    }

    @Test
    void editPage() throws Exception {
        TaskDto taskDto = tasksDto.get(0);
        when(productService.findAll()).thenReturn(new ArrayList<>());
        when(infoSystemService.findAll()).thenReturn(new ArrayList<>());
        when(taskTypeService.findAll()).thenReturn(new ArrayList<>());
        when(userService.findAlltoList()).thenReturn(new ArrayList<>());

        when(taskService.findByIdToDto(1L)).thenReturn(Optional.of(taskDto));
        mvc.perform(get("/tasks/edit/1"))
            .andExpect(view().name("taskEdit"))
            .andExpect(model().attribute("task", taskDto));

    }

    @Test
    void shouldRenderErrorPageWhenTaskNotFound() throws Exception {
        when(productService.findAll()).thenReturn(new ArrayList<Product>());
        when(infoSystemService.findAll()).thenReturn(new ArrayList<InfoSystem>());

        when(taskTypeService.findAll()).thenReturn(new ArrayList<TaskType>());
        when(userService.findAlltoList()).thenReturn(new ArrayList<UserToListDto>());


        //when(taskService.findByIdToDto(1L)).thenThrow(new TaskNotFoundException());
        when(taskService.findByIdToDto(1L)).thenReturn(Optional.empty());
        mvc.perform(get("/tasks/edit/1"))
                .andExpect(view().name("customError"));
    }


}