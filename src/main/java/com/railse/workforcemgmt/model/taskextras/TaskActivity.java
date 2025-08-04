package com.railse.workforcemgmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskActivity {
    private Long taskId;
    private String message;
    private LocalDateTime timestamp;
}
