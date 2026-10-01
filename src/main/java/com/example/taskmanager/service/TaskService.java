package com.example.taskmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.UpdateTaskRequest;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.repository.TaskRepository;

@Service 
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository)
    {
        this.taskRepository=taskRepository;
    }


    public TaskResponse createTask(CreateTaskRequest request){
        Task task= new Task(
            request.title(),
            request.description(),
            request.priority(),
            TaskStatus.TODO
        );
        
        Task savedTask=taskRepository.save(task);
        return toResponse(savedTask);
    }

    public List<TaskResponse> getAllTasks(){
        return taskRepository
        .findAll()
        .stream()
        .map(this::toResponse)
        .toList();
    }

    public TaskResponse getTask(Long id){

        Task task=taskRepository
            .findById(id)
            .orElseThrow(()->
                new TaskNotFoundException(id)
            );

        return toResponse(task);
    }

    public void deleteTask(Long id)
    {
        Task task = taskRepository
        .findById(id)
        .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.delete(task);
    }

    public TaskResponse updateTask(Long id, UpdateTaskRequest request)
    {
       Task task= taskRepository.findById(id).orElseThrow(()->new TaskNotFoundException(id));

       task.setTitle(request.title());
       task.setDescription(request.description());
       task.setPriority(request.priority());
       task.setStatus(request.status());

       Task updatedTask=taskRepository.save(task);

       return toResponse(updatedTask);
    
    }

    private TaskResponse toResponse(Task task) {

        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getPriority(),
            task.getStatus()
        );
    }


}
