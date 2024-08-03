package com.backend.effectivemobile;

import com.backend.effectivemobile.dto.request.TaskRequest;
import com.backend.effectivemobile.entity.Task;
import com.backend.effectivemobile.entity.User;
import com.backend.effectivemobile.entity.enums.Priority;
import com.backend.effectivemobile.entity.enums.Role;
import com.backend.effectivemobile.entity.enums.Status;
import com.backend.effectivemobile.exception.NotFoundException;
import com.backend.effectivemobile.exception.TaskAlreadyExistException;
import com.backend.effectivemobile.repository.TaskRepository;
import com.backend.effectivemobile.repository.UserRepository;
import com.backend.effectivemobile.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateTask_TaskAlreadyExists() {
        TaskRequest request = new TaskRequest("title", "description", "username1", Status.IN_PROCESS,Priority.HIGH);
        Task existingTask = Task.builder()
                .title("title")
                .description("description")
                .priority(Priority.HIGH)
                .status(Status.IN_PROCESS)
                .performerUsername("username1")
                .build();

        when(taskRepository.findByTitle(request.getTitle())).thenReturn(Optional.of(existingTask));

        TaskAlreadyExistException thrown = assertThrows(TaskAlreadyExistException.class, () -> taskService.createTask(request));
        assertEquals("Такой таск уже существует!", thrown.getMessage());
    }

    @Test
    void testCreateTask_PerformerNotFound() {
        TaskRequest request = new TaskRequest("title", "description", "username1", Status.IN_PROCESS,Priority.HIGH);

        when(taskRepository.findByTitle(request.getTitle())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(request.getPerformerUsername())).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> taskService.createTask(request));
        assertEquals("Такого исполнителя нет!", thrown.getMessage());
    }

    @Test
    void testUpdateTask_TaskNotFound() {
        TaskRequest request =  new TaskRequest("update title", "update description", "username1", Status.IN_PROCESS,Priority.HIGH);

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> taskService.updateTask(1L, request));
        assertEquals("Таск не найден!", thrown.getMessage());
    }
}

