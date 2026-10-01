package com.example.taskmanager.acceptanceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.testConfig.TestContainersConfig;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

@AutoConfigureTestRestTemplate 
@Import(TestContainersConfig.class)
public class TaskApiAcceptanceTest {
    
    @Autowired 
    private TestRestTemplate client;
    
    @Autowired 
    private TaskRepository taskRepo;

    @BeforeEach 
    void resetDatabase(){
        taskRepo.deleteAll();
    }
    @Test
    void createTask_WithValidRequest_ReturnsCreated() {
        //Arrange
        CreateTaskRequest request=new CreateTaskRequest(
            "Learn acceptance testing", 
            "test my api", 
            Priority.HIGH
        );

        // Act
        ResponseEntity<TaskResponse> response=
            client.postForEntity("/api/tasks", request, TaskResponse.class);
        
        //Assert
        assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.CREATED);

        assertThat(response.getBody())
            .isNotNull();
        
        assertThat(response.getBody().id())
            .isNotNull();

        assertThat(response.getBody().title())
            .isEqualTo(request.title());

        assertThat(response.getBody().priority())
            .isEqualTo(request.priority());

    }

    @Test
    void userCanCreateAndRetrieveTask() {

        // Arrange
        CreateTaskRequest request = new CreateTaskRequest(
            "Learn Spring Boot",
            "Acceptance testing",
            Priority.HIGH
        );

        // Act: create task
        ResponseEntity<TaskResponse> createResponse =
            client.postForEntity(
                "/api/tasks",
                request,
                TaskResponse.class
            );

        assertThat(createResponse.getStatusCode())
            .isEqualTo(HttpStatus.CREATED);

        TaskResponse createdTask =
            createResponse.getBody();

        assertThat(createdTask).isNotNull();

        Long taskId = createdTask.id();

        // Act: retrieve task
        ResponseEntity<TaskResponse> getResponse =
            client.getForEntity(
                "/api/tasks/" + taskId,
                TaskResponse.class
            );

        // Assert
        assertThat(getResponse.getStatusCode())
            .isEqualTo(HttpStatus.OK);

        assertThat(getResponse.getBody())
            .isNotNull();

        assertThat(getResponse.getBody().id())
            .isEqualTo(taskId);

        assertThat(getResponse.getBody().title())
            .isEqualTo(request.title());

        assertThat(getResponse.getBody().priority())
            .isEqualTo(request.priority());
    }

    @Test
    void getTask_WhenTaskDoesNotExist_ReturnsNotFound() {

        // Act
        ResponseEntity<String> response =
            client.getForEntity(
                "/api/tasks/999",
                String.class
            );

        // Assert
        assertThat(response.getStatusCode())
            .isEqualTo(HttpStatus.NOT_FOUND);
    }

    


    // POST /api/tasks
    // verify 201 Created

    // GET /api/tasks/{id}
    // verify task exists

    // PUT /api/tasks/{id}
    // verify update

    // DELETE /api/tasks/{id}
    // verify 204

    //invalid request → 400
    // missing task → 404
}
