package com.railse.workforcemgmt.service;

import com.railse.workforcemgmt.dto.*;
import com.railse.workforcemgmt.model.enums.Priority;

import java.util.List;

public interface TaskManagementService {


    List<TaskManagementDto> createTasks(TaskCreateRequest request);
    List<TaskManagementDto> updateTasks(UpdateTaskRequest request);
    String assignByReference(AssignByReferenceRequest request);
    List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);
    TaskManagementDto findTaskById(Long id);


    void addCommentToTask(Long taskId, String author, String commentText);
    // ✅ Feature 2
    String changeTaskPriority(Long taskId, Priority newPriority);
    List<TaskManagementDto> getTasksByPriority(Priority priority);
}
