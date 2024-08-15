package com.example.todolist.controller;

import com.example.todolist.model.Todo;
import com.example.todolist.security.CustomUserDetails;
import com.example.todolist.service.ReportService;
import com.example.todolist.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;
    private final TodoService todoService;

    @Autowired
    public ReportController(ReportService reportService, TodoService todoService) {
        this.reportService = reportService;
        this.todoService = todoService;
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadCsvReport(Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            List<Todo> todos = todoService.getTodosByUserId(userDetails.getUser().getId());
            byte[] csvReport = reportService.generateTodoCsvReport(todos);

            ByteArrayInputStream bis = new ByteArrayInputStream(csvReport);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=todo-report.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(new InputStreamResource(bis));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
