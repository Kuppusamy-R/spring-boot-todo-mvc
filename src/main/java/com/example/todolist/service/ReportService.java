package com.example.todolist.service;

import com.example.todolist.model.Todo;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

@Service
public class ReportService {

    public byte[] generateTodoCsvReport(List<Todo> todos) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (Writer writer = new OutputStreamWriter(baos)) {
            writer.write("ID,Task\n");

            for (Todo todo : todos) {
                writer.write(String.format("%d,%s\n",
                        todo.getId(),
                        todo.getTask()));
            }
        }
        return baos.toByteArray();
    }
}

