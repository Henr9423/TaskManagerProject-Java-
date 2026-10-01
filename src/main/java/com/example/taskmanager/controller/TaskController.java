package com.example.taskmanager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.UpdateTaskRequest;
import com.example.taskmanager.service.TaskService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController 
@RequestMapping("/api/tasks")
public class TaskController {
    
    private final TaskService taskService;

    public TaskController(TaskService taskService)
    {
        this.taskService=taskService;
    }


    @PostMapping()
    public ResponseEntity<TaskResponse> CreateTask(
        @Valid @RequestBody CreateTaskRequest request) {
        
       TaskResponse task= taskService.createTask(request);
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(task);
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(@PathVariable Long id) {

        return taskService.getTask(id);
    }

    @GetMapping()
    public List<TaskResponse> getAllTasks(@RequestParam String status) {
        return taskService.getAllTasks();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,@RequestBody UpdateTaskRequest request) {
        TaskResponse response=taskService.updateTask(id,request);
     
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id)
    {
        taskService.deleteTask(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    
    


}
