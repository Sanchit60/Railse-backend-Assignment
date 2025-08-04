package com.railse.workforcemgmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskComment {
    private Long taskId;
    private String author;
    private String comment;
    private LocalDateTime timestamp;
}
