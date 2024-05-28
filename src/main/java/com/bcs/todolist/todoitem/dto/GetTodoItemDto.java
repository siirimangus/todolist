package com.bcs.todolist.todoitem.dto;

public record GetTodoItemDto(
        Integer id,
        String item,
        Boolean completed,
        Integer personId
) {}
