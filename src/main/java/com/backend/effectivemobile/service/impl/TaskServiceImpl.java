package com.backend.effectivemobile.service.impl;

import com.backend.effectivemobile.dto.request.StatusRequest;
import com.backend.effectivemobile.dto.request.TaskRequest;
import com.backend.effectivemobile.dto.response.TasksResponse;
import com.backend.effectivemobile.entity.Task;
import com.backend.effectivemobile.entity.User;
import com.backend.effectivemobile.exception.NotFoundException;
import com.backend.effectivemobile.exception.TaskAlreadyExistException;
import com.backend.effectivemobile.repository.TaskRepository;
import com.backend.effectivemobile.repository.UserRepository;
import com.backend.effectivemobile.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private List<TasksResponse> getTasksResponse(User user) {
        List<TasksResponse> tasksResponses = new ArrayList<>();
        user = getAuthUser();
        for (Task task1 : user.getTasks()) {
            tasksResponses.add(toResponse(task1));
        }

        return tasksResponses;
    }

    @Override
    public List<TasksResponse> createTask(TaskRequest request) {
        if (taskRepository.findByTitle(request.getTitle()).isPresent()) {
            throw new TaskAlreadyExistException("Такой таск уже существует!");
        }

        Task task = new Task();

        User pefrormerUser = userRepository.findByUsername(request.getPerformerUsername())
                .orElseThrow(() -> new NotFoundException("Такого исполнителя нет!"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus());
        task.setPerformerUsername(pefrormerUser.getUsername());
        taskRepository.save(task);

        User user = getAuthUser();
        user.getTasks().add(task);
        userRepository.save(user);

        return getTasksResponse(user);
    }

    @Override
    public List<TasksResponse> updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Таск не найден!"));

        User user = getAuthUser();
        User pefrormerUser = userRepository.findByUsername(request.getPerformerUsername())
                        .orElseThrow(() -> new NotFoundException("Такого исполнителя нет!"));

        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setPerformerUsername(pefrormerUser.getUsername());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        taskRepository.save(task);

        return getTasksResponse(user);
    }

    @Override
    public List<TasksResponse> getMyTasks() {
        User user = getAuthUser();
        return getTasksResponse(user);
    }

    @Override
    public List<TasksResponse> deleteTask(Long id) {
        User user = getAuthUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Таск не найден!"));
        taskRepository.delete(task.getId(), user.getId());
        taskRepository.delete(task);

        return getTasksResponse(user);
    }

    @Override
    public List<TasksResponse> changeStatus(Long id, StatusRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Таск не найден!"));
        User user = getAuthUser();
        if (task.getPerformerUsername().equals(user.getUsername())) {
            task.setStatus(request.getStatus());
            taskRepository.save(task);
        } else {
            throw new NotFoundException("У вас такого таска нет!");
        }
        List<Task> taskList = getByPerformers(task.getPerformerUsername());
        return taskList.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<TasksResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<Task> getByPerformers(String performerUsername) {
        return taskRepository.findAll().stream()
                .filter(task -> task.getPerformerUsername().equals(performerUsername))
                .toList();
    }

    @Override
    public Task getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Таск не найден!"));
        return task;
    }

    private User getAuthUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public TasksResponse toResponse(Task task) {
        return TasksResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .performerUsername(task.getPerformerUsername())
                .priority(task.getPriority())
                .status(task.getStatus())
                .build();
    }
}
