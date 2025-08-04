package com.railse.workforcemgmt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.railse.workforcemgmt.common.model.enums.ReferenceType;
import com.railse.workforcemgmt.model.enums.Priority;
import com.railse.workforcemgmt.model.enums.Task;
import com.railse.workforcemgmt.model.enums.TaskStatus;
import com.railse.workforcemgmt.model.TaskActivity;
import com.railse.workforcemgmt.model.TaskComment;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskManagementDto {
    private Long id;
    private Long referenceId;
    private ReferenceType referenceType;
    private Task task;
    private String description;
    private TaskStatus status;
    private Long assigneeId;
    private Long taskDeadlineTime;
    private Priority priority;

    private List<TaskComment> comments;
    private List<TaskActivity> activityHistory;
}
