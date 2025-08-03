package com.railse.workforcemgmt.service;

import com.railse.workforcemgmt.dto.*;
import com.railse.workforcemgmt.dto.TaskCreateRequest;
import com.railse.workforcemgmt.dto.TaskManagementDto;

import java.util.List;

public interface
TaskManagementService {
    List<TaskManagementDto> createTasks(TaskCreateRequest request);
    List<com.railse.workforcemgmt.dto.TaskManagementDto> updateTasks(com.railse.workforcemgmt.dto.UpdateTaskRequest request);
    String assignByReference(com.railse.workforcemgmt.dto.AssignByReferenceRequest request);
    List<com.railse.workforcemgmt.dto.TaskManagementDto> fetchTasksByDate(com.railse.workforcemgmt.dto.TaskFetchByDateRequest request);
    com.railse.workforcemgmt.dto.TaskManagementDto findTaskById(Long id);
}
