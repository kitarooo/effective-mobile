package com.backend.effectivemobile.entity;

import com.backend.effectivemobile.entity.base_entity.BaseEntity;
import com.backend.effectivemobile.entity.enums.Priority;
import com.backend.effectivemobile.entity.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task extends BaseEntity {
    String title;
    String description;
    @Enumerated(EnumType.STRING)
    Status status;
    @Enumerated(EnumType.STRING)
    Priority priority;
    String author;
    String performer;
}
