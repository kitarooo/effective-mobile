package com.backend.effectivemobile.service;

import com.backend.effectivemobile.dto.request.CommentRequest;
import com.backend.effectivemobile.dto.response.CommentResponse;
import com.backend.effectivemobile.dto.response.TaskCommentResponse;
import com.backend.effectivemobile.entity.Comment;
import com.backend.effectivemobile.entity.Task;

import java.util.List;

public interface CommentService {

    TaskCommentResponse createComment(Long taskId, String token, CommentRequest request);
    TaskCommentResponse updateComment(Long taskId, String token, CommentRequest request);
    TaskCommentResponse deleteComment(Long commentId, Long taskId, String token);
    List<CommentResponse> getAllComments(Long taskId);

}
