package com.example.taskmanager.dto;

import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.entity.TaskStatus;

import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(

   @Size(max = 100, message = "Title cannot exceed 100 character")
    String title,

    @Size(max=500, message = "Description cannot exceed 500 characters")
    String description,

    Priority priority,
    TaskStatus status

) {
}
    

