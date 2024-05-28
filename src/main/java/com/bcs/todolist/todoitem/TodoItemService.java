package com.bcs.todolist.todoitem;

import com.bcs.todolist.person.Person;
import com.bcs.todolist.person.PersonRepository;
import com.bcs.todolist.todoitem.dto.GetTodoItemDto;
import com.bcs.todolist.todoitem.dto.SaveOrUpdateTodoItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TodoItemService {
    private final TodoItemRepository todoItemRepository;
    private final PersonRepository personRepository;

    @Autowired
    public TodoItemService(
            TodoItemRepository todoItemRepository,
            PersonRepository personRepository
    ) {
        this.todoItemRepository = todoItemRepository;
        this.personRepository = personRepository;
    }

    public List<GetTodoItemDto> getAllTodoItems() {
        List<TodoItem> todoItems = todoItemRepository.findAll();

        return todoItems.stream()
                .map(todoItem -> new GetTodoItemDto(todoItem.getId(), todoItem.getItem(), todoItem.getCompleted(), todoItem.getPerson().getId()))
                .toList();
    }

    public GetTodoItemDto getTodoItemById(Integer id) {
        Optional<TodoItem> todoItem = todoItemRepository.findById(id);

        if (todoItem.isPresent()) {
            TodoItem existingTodoItem = todoItem.get();
            return new GetTodoItemDto(existingTodoItem.getId(), existingTodoItem.getItem(), existingTodoItem.getCompleted(), existingTodoItem.getPerson().getId());
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void saveTodoItem(SaveOrUpdateTodoItemDto dto) {
        TodoItem todoItem = new TodoItem();
        todoItem.setItem(dto.item());
        todoItem.setCompleted(dto.completed());

        try {
            Person person = personRepository.getReferenceById(dto.personId());
            todoItem.setPerson(person);
            todoItemRepository.save(todoItem);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void updateTodoItem(SaveOrUpdateTodoItemDto dto, Integer id) {
        Optional<TodoItem> existingTodoItem = todoItemRepository.findById(id);

        if (existingTodoItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Integer personId =  dto.personId() == null ? existingTodoItem.get().getPerson().getId() : dto.personId();
        Optional<Person> person = personRepository.findById(personId);

        if (person.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        String item = (dto.item() == null || dto.item().isEmpty()) ? existingTodoItem.get().getItem() : dto.item();
        Boolean completed = dto.completed() == null ? existingTodoItem.get().getCompleted() : dto.completed();

        try {
            todoItemRepository.update(id, item, completed, person.get());
        }  catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteTodoItem(Integer id) {
        todoItemRepository.deleteById(id);
    }
}
