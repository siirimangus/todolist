package com.bcs.todolist.todoitem;

import com.bcs.todolist.todoitem.dto.GetTodoItemDto;
import com.bcs.todolist.todoitem.dto.SaveOrUpdateTodoItemDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/todoitem")
public class TodoItemController {
    private final TodoItemService todoItemService;

    @Autowired
    public TodoItemController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @GetMapping
    public List<GetTodoItemDto> getAllTodoItems() {
        return todoItemService.getAllTodoItems();
    }

    @GetMapping("/{id}")
    public GetTodoItemDto getTodoItemById(@PathVariable("id") Integer id) {
        return todoItemService.getTodoItemById(id);
    }

    @PostMapping
    public void saveTodoItem(@Valid @RequestBody SaveOrUpdateTodoItemDto todoItem) {
        todoItemService.saveTodoItem(todoItem);
    }

    @PatchMapping("/{id}")
    public void updateTodoItem(
            @PathVariable("id") Integer id,
            @RequestBody SaveOrUpdateTodoItemDto todoItemDto
    ) {
        todoItemService.updateTodoItem(todoItemDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteTodoItem(@PathVariable("id") Integer id) {
        todoItemService.deleteTodoItem(id);
    }
}
