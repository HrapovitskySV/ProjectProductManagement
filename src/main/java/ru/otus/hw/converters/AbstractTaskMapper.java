package ru.otus.hw.converters;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Task;
import ru.otus.hw.models.TaskType;
import ru.otus.hw.models.dto.ProductDto;
import ru.otus.hw.models.dto.TaskDto;
import ru.otus.hw.models.dto.TaskDtoInputWeb;
import ru.otus.hw.models.dto.UserToListDto;
import ru.otus.hw.services.UserService;
import ru.otus.hw.services.ProductService;
import ru.otus.hw.services.InfoSystemService;
import ru.otus.hw.services.TaskTypeService;

@Mapper(componentModel = "spring")
public abstract class AbstractTaskMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InfoSystemService infoSystemService;

    @Autowired
    private TaskTypeService taskTypeService;

    public abstract TaskDto toDto(Task task);

    @Mapping(target = "product", source = "productId")
    @Mapping(target = "taskType", source = "taskTypeId")
    @Mapping(target = "responsible", source = "responsibleId")
    @Mapping(target = "author", source = "authorId")
    @Mapping(target = "programmer", source = "programmerId")
    @Mapping(target = "analyst", source = "analystId")
    @Mapping(target = "infoSystem", source = "infoSystemId")
    public abstract Task toModel(TaskDtoInputWeb taskDto);

    public UserToListDto userToUserToListDto(CustomUser user) {
        return Mappers.getMapper(AbstractUserConverter.class).toListDto(user);
    }

    public CustomUser userIdToUser(long userId) {
        if (userId == 0) {
            return null;
        }

        var oUser = userService.findById(userId);
        if (oUser.isEmpty()) {
            throw new EntityNotFoundException("User with id %s not found".formatted(userId));
        }
        return oUser.get();
    }

    public Product productIdToProduct(long productId) {
        if (productId == 0) {
            return null;
        }
        var oProduct = productService.findById(productId);
        if (oProduct.isEmpty()) {
            throw new EntityNotFoundException("Product with id %s not found".formatted(productId));
        }
        return oProduct.get();
    }

    public InfoSystem infoSystemIdToInfoSystem(long infoSystemId) {
        if (infoSystemId == 0) {
            return null;
        }
        var oInfoSystem = infoSystemService.findById(infoSystemId);
        if (oInfoSystem.isEmpty()) {
            throw new EntityNotFoundException("InfoSystem with id %s not found".formatted(infoSystemId));
        }
        return oInfoSystem.get();
    }

    public TaskType taskTypeIdToTaskType(long taskTypeId) {
        if (taskTypeId == 0) {
            return null;
        }

        var oTaskType = taskTypeService.findById(taskTypeId);
        if (oTaskType.isEmpty()) {
            throw new EntityNotFoundException("TaskType with id %s not found".formatted(taskTypeId));
        }
        return oTaskType.get();
    }



    public String getViewTask(TaskDto taskDto) {
        String strView = ("Task: %d from: %s, " +
                "name: %s, " +
                "type: %s, " +
                "description: %s, " +
                "author: %s" +
                "priority: %s " +
                "product: %s " +
                "infoSystem: %s" +
                "responsible: %s" +
                "analyst: %s" +
                "responsible: %s")
                .formatted(
                        taskDto.getId(),
                        taskDto.getCreated(),
                        taskDto.getName(),
                        taskDto.getTaskType(),
                        taskDto.getDescription(),
                        taskDto.getAuthor(),
                        taskDto.getPriority(),
                        taskDto.getProduct(),
                        taskDto.getInfoSystem(),
                        taskDto.getResponsible(),
                        taskDto.getProgrammer(),
                        taskDto.getAnalyst()
                );
        return strView;
    }

    public ProductDto productToProductDto(Product product) {
        return Mappers.getMapper(ProductMapper.class).toDto(product);
    }

}
