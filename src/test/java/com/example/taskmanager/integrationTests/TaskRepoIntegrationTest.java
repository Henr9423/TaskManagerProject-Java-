package com.example.taskmanager.integrationTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskStatus;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.testConfig.TestContainersConfig;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest 
@Import(TestContainersConfig.class)
@Transactional 
public class TaskRepoIntegrationTest {
    
    @Autowired private TaskRepository taskRepo;
    @Autowired private EntityManager entityManager;

    @Test 
    void shouldSaveTask(){
        //Arrange
        Task task=new Task("Learn unit testing",
            "Practice Mockito",
            Priority.HIGH,
            TaskStatus.TODO
        );
         
        //Act
        Task savedTask=taskRepo.saveAndFlush(task);
        Long id=savedTask.getId();
        entityManager.clear();
        Task loadedTask=taskRepo.findById(id).orElseThrow();

        //Assert
        assertEquals(task.getTitle(), loadedTask.getTitle());
        assertEquals(task.getDescription(), loadedTask.getDescription());
        assertEquals(task.getPriority(), loadedTask.getPriority());
        assertEquals(task.getStatus(), loadedTask.getStatus());

    }

   
  

    @Test 
    void shouldDeleteTask()
    {
        // Arrange
        Task task = new Task(
            "Test task",
            "Delete me",
            Priority.HIGH,
            TaskStatus.TODO
        );

        Task savedTask = taskRepo.saveAndFlush(task);

        Long id = savedTask.getId();

        // Act
        taskRepo.deleteById(id);
        taskRepo.flush();

        entityManager.clear();

        // Assert
        assertFalse(taskRepo.existsById(id));
    }


    @Test
    void saveTask_WithNullTitle_ShouldFail() {

        Task task = new Task(
            null,
            "Description",
            Priority.HIGH,
            TaskStatus.TODO
        );

        assertThrows(
            DataIntegrityViolationException.class,
            () -> taskRepo.saveAndFlush(task)
        );
    }

  


}
