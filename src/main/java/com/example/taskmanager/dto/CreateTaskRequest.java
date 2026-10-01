package com.example.taskmanager.dto;

import com.example.taskmanager.entity.Priority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
    @NotBlank(message="Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 character")
    String title,

    @Size(max=500, message = "Description cannot exceed 500 characters")
    String description,

    @NotNull(message = "Priority is required")
    Priority priority
) {

}
