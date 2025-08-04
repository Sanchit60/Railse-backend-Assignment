package com.railse.workforcemgmt.repository;

import com.railse.workforcemgmt.model.TaskActivity;
import com.railse.workforcemgmt.model.TaskComment;
import com.railse.workforcemgmt.model.TaskManagement;
import com.railse.workforcemgmt.common.model.enums.ReferenceType;
import com.railse.workforcemgmt.model.enums.Priority;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Optional<TaskManagement> findById(Long id);
    TaskManagement save(TaskManagement task);
    List<TaskManagement> findAll();
    List<TaskManagement> findByReferenceIdAndReferenceType(Long referenceId, ReferenceType referenceType);
    List<TaskManagement> findByAssigneeIdIn(List<Long> assigneeIds);
    List<TaskManagement> getTasksByPriority(Priority priority);
    List<TaskComment> findCommentsByTaskId(Long taskId);
    List<TaskActivity> findActivitiesByTaskId(Long taskId);



}
