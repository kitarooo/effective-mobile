package com.backend.effectivemobile.dto.request;

import com.backend.effectivemobile.entity.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatusRequest {
    Status status;
}
