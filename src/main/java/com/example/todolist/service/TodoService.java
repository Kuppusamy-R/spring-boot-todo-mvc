package com.example.todolist.service;

import com.example.todolist.model.Todo;
import com.example.todolist.model.User;
import com.example.todolist.repository.TodoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Todo> getTodosByUserId(Long userId) {
        return todoRepository.findByUserId(userId);
    }

    public void addTodo(String task, Long userId) {

        User managedUser = entityManager.getReference(User.class, userId);

        Todo todo = new Todo();
        todo.setTask(task);
        todo.setUser(managedUser);
        todoRepository.save(todo);
    }

    public boolean deleteTodoById(Long id, Long userId) {
        Optional<Todo> todo = todoRepository.findByIdAndUserId(id, userId);
        if(todo.isPresent()){
            todoRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }
}
