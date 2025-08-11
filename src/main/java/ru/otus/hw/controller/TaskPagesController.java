package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.hw.exceptions.TaskNotFoundException;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.TaskType;
import ru.otus.hw.models.dto.TaskDto;
import ru.otus.hw.models.dto.UserToListDto;
import ru.otus.hw.services.InfoSystemService;
import ru.otus.hw.services.TaskService;
import ru.otus.hw.services.ProductService;
import ru.otus.hw.services.TaskTypeService;
import ru.otus.hw.services.UserService;
import ru.otus.hw.services.LoadTaskFomExcelService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller()
@RequiredArgsConstructor
public class TaskPagesController {

    private String uploadDir = "";

    private final InfoSystemService infoSystemService;

    private final TaskService taskService;

    private final ProductService productService;

    private final TaskTypeService taskTypeService;

    private final UserService userService;


    private final LoadTaskFomExcelService loadTaskFomExcelService;

    @InitBinder
    public void initBinder(WebDataBinder binder) throws IOException {
        this.uploadDir = Files.createTempDirectory("tmpDirPrefix").toFile().getAbsolutePath();
    }

    @GetMapping("/tasks")
    public String listAllTasks(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUserDto());

        return "taskList";
    }

    @GetMapping("/")
    public String listAllTasks2(Model model) {
        return "taskList";
    }

    @GetMapping("/tasks/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        TaskDto task = taskService.findByIdToDto(id).orElseThrow(TaskNotFoundException::new);
        model.addAttribute("task", task);
        model.addAttribute("method", "PUT");
        return openEditPage(model);
    }


    @GetMapping("/tasks/add")
    public String addPage(Model model) {
        TaskDto task = new TaskDto();
        task.setAuthor(userService.getCurrentUserDto());
        task.setResponsible(task.getAuthor());
        model.addAttribute("task", task);
        model.addAttribute("method", "POST");
        return openEditPage(model);
    }

    private String openEditPage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        List<InfoSystem> infoSystems = infoSystemService.findAll();
        model.addAttribute("infoSystems", infoSystems);

        List<TaskType> taskTypes = taskTypeService.findAll();
        model.addAttribute("taskTypes", taskTypes);

        List<UserToListDto> users = userService.findAlltoList();
        model.addAttribute("users", users);
        model.addAttribute("redirectUrl", "/tasks");

        return "taskEdit";
    }

    @PostMapping("/tasks/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) throws Exception {
        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "taskList";
        }

        // normalize the file path
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // save the file on the local file system
        Path path = Paths.get(uploadDir + "/" + UUID.randomUUID() + "_" + fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');
        loadTaskFomExcelService.starLoadTaskJobWithJob(path.toString());
        Files.delete(path);
        return "redirect:/tasks";
    }
}
