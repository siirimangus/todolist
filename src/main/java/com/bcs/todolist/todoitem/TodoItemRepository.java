package com.bcs.todolist.todoitem;

import com.bcs.todolist.person.Person;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE todoitem SET item = ?2, completed = ?3, person = ?4 WHERE id = ?1")
    void update(Integer id, String item, Boolean completed, Person person);
}
