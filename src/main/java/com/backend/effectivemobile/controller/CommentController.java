package com.backend.effectivemobile.controller;

import com.backend.effectivemobile.dto.request.CommentRequest;
import com.backend.effectivemobile.dto.response.CommentResponse;
import com.backend.effectivemobile.dto.response.TaskCommentResponse;
import com.backend.effectivemobile.exception.handler.ExceptionResponse;
import com.backend.effectivemobile.service.impl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping("/create/{taskId}")
    @GetMapping("/get/{id}")
    @Operation(summary = "Добавить комментарий", description = "Ендпоинт для добавления комментария!",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "Task not found!")
            })
    public TaskCommentResponse create(@RequestBody CommentRequest request,
                                      @RequestHeader("Authorization") String token,
                                      @PathVariable Long taskId) {
        return commentService.createComment(taskId,token,request);
    }

    @PatchMapping("/update/{commentId}")
    @Operation(summary = "Обновить комментарий", description = "Ендпоинт для обновления комментария!",
            responses = {
                    @ApiResponse(
                            content = @Content(mediaType = "string"),
                            responseCode = "200", description = "Good"),
                    @ApiResponse(
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)),
                            responseCode = "404", description = "Task not found!")
            })
    public TaskCommentResponse update(@PathVariable Long commentId,
                       @RequestBody CommentRequest request,
                       @RequestHeader("Authorization") String token) {
        return commentService.updateComment(commentId,token,request);
    }

    @DeleteMapping("/{taskId}/{commentId}")
    @Operation(summary = "Удалить комментарий", description = "Ендпоинт для удаления комментария!",
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
                            responseCode = "405", description = "That's not your comment.")
            })
    public TaskCommentResponse delete(@PathVariable Long taskId,
                                      @PathVariable Long commentId,
                                      @RequestHeader("Authorization") String token) {
        return commentService.deleteComment(commentId,taskId,token);
    }

    @GetMapping("/{taskId}")
    public List<CommentResponse> getAllComments(@PathVariable Long taskId) {
        return commentService.getAllComments(taskId);
    }

}
