package com.example.taskmanager.dto;

import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.entity.TaskStatus;

public record TaskResponse(
    Long id,
    String title,
    String description,
    Priority priority,
    TaskStatus status
) {
    
}
