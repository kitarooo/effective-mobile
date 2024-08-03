package com.backend.effectivemobile.service.impl;

import com.backend.effectivemobile.dto.request.StatusRequest;
import com.backend.effectivemobile.dto.request.TaskRequest;
import com.backend.effectivemobile.dto.response.CommentResponse;
import com.backend.effectivemobile.dto.response.TasksResponse;
import com.backend.effectivemobile.entity.Comment;
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


    @Override
    public String createTask(TaskRequest request) {
        if (taskRepository.findByTitle(request.getTitle()).isPresent()) {
            throw new TaskAlreadyExistException("Такой таск уже существует!");
        }

        User pefrormerUser = userRepository.findByUsername(request.getPerformerUsername())
                .orElseThrow(() -> new NotFoundException("Такого исполнителя нет!"));

        Task task = Task.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .priority(request.getPriority())
                        .status(request.getStatus())
                        .performerUsername(pefrormerUser.getUsername())
                        .build();

        taskRepository.save(task);

        User user = getAuthUser();
        user.getTasks().add(task);
        userRepository.save(user);

        return "Таск успешно создан!";
    }

    @Override
    public String updateTask(Long id, TaskRequest request) {
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

        return "Таск успешно обновлен!";
    }

    @Override
    public List<TasksResponse> getMyTasks() {
        List<TasksResponse> tasksResponses = new ArrayList<>();
        User user = getAuthUser();
        for (Task task : user.getTasks()) {
            tasksResponses.add(toResponse(task));
        }
        return tasksResponses;
    }

    @Override
    public String deleteTask(Long id) {
        User user = getAuthUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Таск не найден!"));
        taskRepository.delete(task.getId(), user.getId());
        taskRepository.delete(task);

        return "Таск успешно удален!";
    }

    @Override
    public String changeStatus(Long id, StatusRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Таск не найден!"));
        User user = getAuthUser();
        if (task.getPerformerUsername().equals(user.getUsername())) {
            task.setStatus(request.getStatus());
            taskRepository.save(task);
            return "Статус таска успешно обновлен!";
        } else {
            throw new NotFoundException("Вы не являетесь исполнителем!");
        }
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


    public TasksResponse toResponse(Task task) {
        List<Comment> comments = (task.getComments() != null) ? task.getComments() : new ArrayList<>();

        List<CommentResponse> tasksResponses = comments.stream()
                .map(this::mapToResponse)
                .toList();

        return TasksResponse.builder()
                .id(task.getId())
                .comments(tasksResponses)
                .description(task.getDescription())
                .title(task.getTitle())
                .performerUsername(task.getPerformerUsername())
                .priority(task.getPriority())
                .status(task.getStatus())
                .build();
    }


    private User getAuthUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    protected CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .description(comment.getDescription())
                .userId(comment.getUserId())
                .build();
    }
}
