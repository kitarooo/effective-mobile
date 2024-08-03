package com.backend.effectivemobile.service.impl;

import com.backend.effectivemobile.dto.request.CommentRequest;
import com.backend.effectivemobile.dto.response.CommentResponse;
import com.backend.effectivemobile.dto.response.TaskCommentResponse;
import com.backend.effectivemobile.entity.Comment;
import com.backend.effectivemobile.entity.Task;
import com.backend.effectivemobile.exception.NotFoundException;
import com.backend.effectivemobile.exception.UserException;
import com.backend.effectivemobile.repository.CommentRepository;
import com.backend.effectivemobile.repository.TaskRepository;
import com.backend.effectivemobile.security.jwt.JwtService;
import com.backend.effectivemobile.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final JwtService jwtService;
    private final TaskRepository taskRepository;


    @Override
    public TaskCommentResponse createComment(Long taskId, String token, CommentRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Таск не найден!"));

        Long userId = jwtService.extractUserId(token);
        Comment comment = new Comment();
        comment.setDescription(request.getDescription());
        comment.setUserId(userId);
        comment.setTask(task);
        commentRepository.save(comment);

        return TaskCommentResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .performerUsername(task.getPerformerUsername())
                .comments(getAllComments(task.getId()))
                .build();
    }

    @Override
    public TaskCommentResponse updateComment(Long commentId, String token, CommentRequest request) {
        Long userId = jwtService.extractUserId(token);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Коммент не найден!"));

        if (userId.equals(comment.getUserId())) {
            comment.setDescription(request.getDescription());
            commentRepository.save(comment);
            return TaskCommentResponse.builder()
                    .id(comment.getTask().getId())
                    .title(comment.getTask().getTitle())
                    .description(comment.getTask().getDescription())
                    .status(comment.getTask().getStatus())
                    .priority(comment.getTask().getPriority())
                    .performerUsername(comment.getTask().getPerformerUsername())
                    .comments(getAllComments(comment.getTask().getId()))
                    .build();
        } else {
            throw new NotFoundException("Ваш комментарий не найден!");
        }
    }

    @Override
    public TaskCommentResponse deleteComment(Long commentId, Long taskId, String token) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Коммент не найден!"));
        Long userId = jwtService.extractUserId(token);
        if (comment.getUserId().equals(userId)) {
            commentRepository.delete(taskId, commentId);
            commentRepository.delete(comment);

            return TaskCommentResponse.builder()
                    .id(comment.getTask().getId())
                    .title(comment.getTask().getTitle())
                    .description(comment.getTask().getDescription())
                    .status(comment.getTask().getStatus())
                    .priority(comment.getTask().getPriority())
                    .performerUsername(comment.getTask().getPerformerUsername())
                    .comments(getAllComments(comment.getTask().getId()))
                    .build();
        } else {
            throw new UserException("Это не ваш коммент)");
        }
    }

    @Override
    public List<CommentResponse> getAllComments(Long taskId) {
        List<Comment> commentList =  commentRepository.findAll().stream()
                .filter(task -> task.getTask().getId().equals(taskId))
                .toList();
        return commentList.stream()
                .filter(task -> task.getTask().getId().equals(taskId))
                .map(this::mapToResponse)
                .toList();
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .description(comment.getDescription())
                .userId(comment.getUserId())
                .build();
    }
}
