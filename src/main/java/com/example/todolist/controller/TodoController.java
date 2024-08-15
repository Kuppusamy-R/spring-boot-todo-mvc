package com.example.todolist.controller;

import com.example.todolist.model.Todo;
import com.example.todolist.security.CustomUserDetails;
import com.example.todolist.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public String todoList(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<Todo> todos = todoService.getTodosByUserId(userDetails.getUser().getId());
        model.addAttribute("todos", todos);
        return "todo";
    }

    @PostMapping
    public String addTodo(@RequestParam String task, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        todoService.addTodo(task, userDetails.getUser().getId());
        return "redirect:/todo";
    }

    @PostMapping("/delete")
    public String deleteTodo(@RequestParam Long id, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        boolean isDeleteSuccess = todoService.deleteTodoById(id, userDetails.getUser().getId());

        if(isDeleteSuccess){
            return "redirect:/todo";
        }else{
            return "redirect:/todo?deletionError=true";
        }
    }
}
