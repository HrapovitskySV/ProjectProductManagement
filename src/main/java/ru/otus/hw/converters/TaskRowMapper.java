package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;
import org.springframework.stereotype.Component;
import ru.otus.hw.exceptions.UserNotFoundException;
import ru.otus.hw.exceptions.InfoSystemNotFoundException;
import ru.otus.hw.exceptions.ProductNotFoundException;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Task;
import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.TaskType;

import ru.otus.hw.services.InfoSystemService;
import ru.otus.hw.services.ProductService;
import ru.otus.hw.services.TaskTypeService;
import ru.otus.hw.services.UserService;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TaskRowMapper  implements RowMapper<Task> {

    private final ProductService productService;

    private final InfoSystemService infoSystemService;

    private final UserService userService;

    private final TaskTypeService taskTypeService;

    @Override
    public Task mapRow(RowSet rowSet) throws Exception {

        Task task = new Task();
        var prop = rowSet.getProperties();
        task.setName(prop.getProperty("name"));
        task.setDescription(prop.getProperty("description"));
        task.setLaborcosts(new BigDecimal(prop.getProperty("laborcosts")));
        task.setPriority(new BigDecimal(prop.getProperty("priority")));

        task.setProduct(productNameToProduct(prop.getProperty("product")));
        task.setInfoSystem(infoSystemNameToInfoSystem(prop.getProperty("infoSystem")));
        task.setResponsible(userNameToUser(prop.getProperty("responsible")));
        task.setAuthor(userService.getCurrentUser());
        task.setProgrammer(userNameToUser(prop.getProperty("programmer")));
        task.setAnalyst(userNameToUser(prop.getProperty("analyst")));
        task.setTaskType(taskTypeNameToTaskType(prop.getProperty("taskType")));


        return task;
    }

    private TaskType taskTypeNameToTaskType(String taskTypeName) {
        if (taskTypeName.isEmpty()) {
            return null;
        }
        var taskTypes = taskTypeService.findByName(taskTypeName);
        if (taskTypes.isEmpty()) {
            throw new UserNotFoundException("InfoSystem with name %s not found".formatted(taskTypeName));
        }
        return taskTypes.get();
    }

    private CustomUser userNameToUser(String userName) {
        if (userName.isEmpty()) {
            return null;
        }

        var oUser = userService.findByUsername(userName);
        if (oUser.isEmpty()) {
            throw new UserNotFoundException("User with name %s not found".formatted(userName));
        }

        return oUser.get();
    }

    private InfoSystem infoSystemNameToInfoSystem(String infoSystemName) {
        if (infoSystemName.isEmpty()) {
            return null;
        }
        var infoSystems = infoSystemService.findByName(infoSystemName);
        if (infoSystems.isEmpty()) {
            throw new InfoSystemNotFoundException("InfoSystem with name %s not found".formatted(infoSystemName));
        }
        return infoSystems.get();

    }

    public Product productNameToProduct(String productName) {
        if (productName.isEmpty()) {
            return null;
        }
        var products = productService.findByName(productName);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("Product with name %s not found".formatted(productName));
        }

        return products.get();
    }
}
