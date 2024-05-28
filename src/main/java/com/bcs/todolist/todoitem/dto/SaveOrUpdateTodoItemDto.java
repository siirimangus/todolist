package com.bcs.todolist.todoitem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveOrUpdateTodoItemDto(
        @NotBlank
        String item,

        Boolean completed,

        @NotNull
        Integer personId
) {
}
