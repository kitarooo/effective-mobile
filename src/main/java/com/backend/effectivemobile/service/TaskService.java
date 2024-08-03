package com.backend.effectivemobile.service;

import com.backend.effectivemobile.dto.request.StatusRequest;
import com.backend.effectivemobile.dto.request.TaskRequest;
import com.backend.effectivemobile.dto.response.TasksResponse;
import com.backend.effectivemobile.entity.Task;

import java.util.List;

public interface TaskService {

    String createTask(TaskRequest request);
    String updateTask(Long id, TaskRequest request);
    List<TasksResponse> getMyTasks();
    String deleteTask(Long id);
    String changeStatus(Long id, StatusRequest request);
    List<TasksResponse> getAllTasks();
    List<Task> getByPerformers(String performer);
    Task getById(Long id);

}
