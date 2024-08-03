package com.backend.effectivemobile.controller;

import com.backend.effectivemobile.dto.request.CommentRequest;
import com.backend.effectivemobile.dto.response.CommentResponse;
import com.backend.effectivemobile.dto.response.TaskCommentResponse;
import com.backend.effectivemobile.entity.Comment;
import com.backend.effectivemobile.entity.Task;
import com.backend.effectivemobile.service.impl.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping("/create/{taskId}")
    public TaskCommentResponse create(@RequestBody CommentRequest request,
                                      @RequestHeader("Authorization") String token,
                                      @PathVariable Long taskId) {
        return commentService.createComment(taskId,token,request);
    }

    @PatchMapping("/update/{commentId}")
    public TaskCommentResponse update(@PathVariable Long commentId,
                       @RequestBody CommentRequest request,
                       @RequestHeader("Authorization") String token) {
        return commentService.updateComment(commentId,token,request);
    }

    @DeleteMapping("/{taskId}/{commentId}")
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
