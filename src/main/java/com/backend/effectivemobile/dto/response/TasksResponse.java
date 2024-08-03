package com.backend.effectivemobile.dto.response;

import com.backend.effectivemobile.entity.enums.Priority;
import com.backend.effectivemobile.entity.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TasksResponse {
    Long id;
    String title;
    String performerUsername;
    Status status;
    Priority priority;
}
