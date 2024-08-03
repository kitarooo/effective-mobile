package com.backend.effectivemobile.controller;

import com.backend.effectivemobile.dto.request.StatusRequest;
import com.backend.effectivemobile.dto.request.TaskRequest;
import com.backend.effectivemobile.dto.response.TasksResponse;
import com.backend.effectivemobile.entity.Task;
import com.backend.effectivemobile.exception.handler.ExceptionResponse;
import com.backend.effectivemobile.service.impl.TaskServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskService;

    @PostMapping("/create")
    @Operation(summary = "Создание таска!", description = "Ендпоинт для создания таска!",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "400", description = "Task already exist exception!"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "Performer not found!!"
                    )
            })
    public List<TasksResponse> createTask(@RequestBody TaskRequest taskRequest) {
        return taskService.createTask(taskRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление таска!", description = "Ендпоинт для обновления таска!",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "Task not found!"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "Performer not found!!"
                    )
            })
    public List<TasksResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        return taskService.updateTask(id, taskRequest);
    }

    public List<TasksResponse> getMyTasks() {
        return taskService.getMyTasks();
    }

    @DeleteMapping("/{id}")
    @PutMapping("/{id}")
    @Operation(summary = "Удаление таска", description = "Ендпоинт для удаления таска!",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "Task not found!")
            })
    public List<TasksResponse> deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }

    @PatchMapping("/{id}")
    @PutMapping("/{id}")
    @Operation(summary = "Исполнитель обновляет статус таска!", description = "Ендпоинт для обновления статуса таска!",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "Task not found!")
            })
    public List<TasksResponse> updateTaskStatus(@PathVariable Long id, @RequestBody StatusRequest request) {
        return taskService.changeStatus(id, request);
    }

    @GetMapping("/get_all")
    public List<TasksResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{performerUsername}")
    public List<Task> getTasksByPerformerUsername(@PathVariable String performerUsername) {
        return taskService.getByPerformers(performerUsername);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Получение таска по id", description = "Ендпоинт для получения таска!",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "Task not found!")
            })
    public Task getById(@PathVariable Long id) {
        return taskService.getById(id);
    }
}
