package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import static java.util.Objects.isNull;

@Data
@AllArgsConstructor
public class UserToListDto {
    private long id;

    private String username;

    private String email;

    private boolean informAboutTasks;

    public boolean eg(UserToListDto inUser, int fielad) {
        if (isNull(inUser)) {
            return false;
        }
            return (this.id == inUser.getId());

    }
}
