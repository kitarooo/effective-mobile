package com.backend.effectivemobile.dto.response;

import com.backend.effectivemobile.entity.enums.Priority;
import com.backend.effectivemobile.entity.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskCommentResponse {
    Long id;
    String title;
    String performerUsername;
    String description;
    Status status;
    Priority priority;
    List<CommentResponse> comments;
}
