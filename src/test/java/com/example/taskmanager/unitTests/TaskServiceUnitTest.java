package com.example.taskmanager.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.UpdateTaskRequest;
import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.service.TaskService;

@ExtendWith(MockitoExtension.class)
public class TaskServiceUnitTest {
    @Mock 
    private TaskRepository taskRepoMock;

    @InjectMocks
    private TaskService taskService;
   

    @Test 
    void shouldCreateTask(){
        //Arrange

        CreateTaskRequest request=new CreateTaskRequest(
            "Learn unit testing",
            "Practice Mockito",
            Priority.HIGH
        );

        Task savedTask=new Task("Learn unit testing",
            "Practice Mockito",
            Priority.HIGH,
            TaskStatus.TODO
        );

        when(taskRepoMock.save(any(Task.class)))
            .thenReturn(savedTask);
        
        //Act
        TaskResponse result=taskService.createTask(request);

        //Assert
        assertEquals("Learn unit testing", result.title());
        assertEquals(Priority.HIGH,result.priority());
        assertEquals(TaskStatus.TODO, result.status());
        
        verify(taskRepoMock).save(any(Task.class));
        

    }

    @Test 
    void shouldReturnTaskWhenTaskExists()
    {
        //Arrange
        Long id=1l;

        Task savedTask=new Task("Learn unit testing",
            "Practice Mockito",
            Priority.HIGH,
            TaskStatus.TODO
        );
        when(taskRepoMock.findById(id)).thenReturn(Optional.of(savedTask));
        //Act
        TaskResponse taskResponse=taskService.getTask(id);

        //Assert
        assertEquals(savedTask.getTitle(), taskResponse.title());
        assertEquals(savedTask.getDescription(), taskResponse.description());
        assertEquals(Priority.HIGH,taskResponse.priority());
        assertEquals(TaskStatus.TODO, taskResponse.status());
        
        
        verify(taskRepoMock).findById(id);
        
    }

    @Test
    void shouldThrowExceptionWhenTaskDoesNotExist(){
        //Arrange
        Long notValidId=99l;

        when(taskRepoMock.findById(notValidId)).thenReturn(Optional.empty());

        //Act + Assert

       assertThrows(TaskNotFoundException.class, ()->taskService.getTask(notValidId)); 

       verify(taskRepoMock).findById(notValidId);
    }

    @Test 
    void shouldUpdateTask(){
        //Arrange
        Long id=1l;

        Task savedTask=new Task("Learn unit testing",
            "Practice Mockito",
            Priority.HIGH,
            TaskStatus.TODO
        );

        UpdateTaskRequest request= new UpdateTaskRequest("new Title","new desciption",Priority.MEDIUM,TaskStatus.IN_PROGRESS);
        
        when(taskRepoMock.findById(id)).thenReturn(Optional.of(savedTask));
        when(taskRepoMock.save(savedTask)).thenReturn(savedTask);

        //Act
        TaskResponse taskResponse=taskService.updateTask(id,request);

        //Assert
        verify(taskRepoMock).findById(id);

        assertEquals(request.title(), taskResponse.title());
        assertEquals(request.description(), taskResponse.description());
        assertEquals(request.priority(),taskResponse.priority());
        assertEquals(request.status(), taskResponse.status());
       

    }

    @Test 
    void shouldDeleteTask()
    {
        //Arrange
        Long id=1l;

        Task savedTask=new Task("Learn unit testing",
            "Practice Mockito",
            Priority.HIGH,
            TaskStatus.TODO
        );
        when(taskRepoMock.findById(id)).thenReturn(Optional.of(savedTask));

        //Act
        taskService.deleteTask(id);

        //Assert
        verify(taskRepoMock).findById(id);
        verify(taskRepoMock).delete(savedTask);

    }
    
    @Test
    void shouldThrowExceptionWhenDeletingMissingTask() {

        //Arrange
        when(taskRepoMock.findById(99L))
            .thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(
            TaskNotFoundException.class,
            () -> taskService.deleteTask(99L)
        );

        verify(taskRepoMock).findById(99L);
        verify(taskRepoMock, never()).delete(any());
    }
}
