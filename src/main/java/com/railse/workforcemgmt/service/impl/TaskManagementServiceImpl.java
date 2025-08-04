package com.railse.workforcemgmt.service.impl;

import com.railse.workforcemgmt.common.exception.ResourceNotFoundException;
import com.railse.workforcemgmt.dto.*;
import com.railse.workforcemgmt.mapper.ITaskManagementMapper;
import com.railse.workforcemgmt.model.TaskActivity;
import com.railse.workforcemgmt.model.TaskComment;
import com.railse.workforcemgmt.model.TaskManagement;
import com.railse.workforcemgmt.model.enums.Task;
import com.railse.workforcemgmt.model.enums.TaskStatus;
import com.railse.workforcemgmt.repository.TaskRepository;
import com.railse.workforcemgmt.service.TaskManagementService;
import com.railse.workforcemgmt.model.enums.Priority;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {

    private final TaskRepository taskRepository;
    private final ITaskManagementMapper taskMapper;

    public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    private final Map<Long, List<TaskComment>> taskComments = new ConcurrentHashMap<>();
    private final Map<Long, List<TaskActivity>> taskActivities = new ConcurrentHashMap<>();

    @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        TaskManagementDto dto = taskMapper.modelToDto(task);

        List<TaskComment> comments = taskComments.getOrDefault(id, new ArrayList<>());
        List<TaskActivity> activities = taskActivities.getOrDefault(id, new ArrayList<>());

        dto.setComments(comments);
        dto.setActivityHistory(activities);

        return dto;
    }

    @Override
    public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
        List<TaskManagement> createdTasks = new ArrayList<>();
        for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(item.getReferenceId());
            newTask.setReferenceType(item.getReferenceType());
            newTask.setTask(item.getTask());
            newTask.setAssigneeId(item.getAssigneeId());
            newTask.setPriority(item.getPriority());
            newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setDescription("New task created.");
            createdTasks.add(taskRepository.save(newTask));
        }
        return taskMapper.modelListToDtoList(createdTasks);
    }

    @Override
    public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
        List<TaskManagement> updatedTasks = new ArrayList<>();
        for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));
            if (item.getTaskStatus() != null) {
                task.setStatus(item.getTaskStatus());
            }
            if (item.getDescription() != null) {
                task.setDescription(item.getDescription());
            }
            updatedTasks.add(taskRepository.save(task));
        }
        return taskMapper.modelListToDtoList(updatedTasks);
    }

    @Override
    public String assignByReference(AssignByReferenceRequest request) {
        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(
                request.getReferenceId(), request.getReferenceType());

        for (Task taskType : applicableTasks) {
            List<TaskManagement> tasksOfType = existingTasks.stream()
                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
                    .collect(Collectors.toList());

            if (!tasksOfType.isEmpty()) {
                TaskManagement taskToReassign = tasksOfType.get(0);
                taskToReassign.setAssigneeId(request.getAssigneeId());
                taskRepository.save(taskToReassign);

                for (int i = 1; i < tasksOfType.size(); i++) {
                    TaskManagement taskToCancel = tasksOfType.get(i);
                    taskToCancel.setStatus(TaskStatus.CANCELLED);
                    taskRepository.save(taskToCancel);
                }
            } else {
                TaskManagement newTask = new TaskManagement();
                newTask.setReferenceId(request.getReferenceId());
                newTask.setReferenceType(request.getReferenceType());
                newTask.setTask(taskType);
                newTask.setAssigneeId(request.getAssigneeId());
                newTask.setStatus(TaskStatus.ASSIGNED);
                taskRepository.save(newTask);
            }
        }

        return "Tasks assigned successfully for reference " + request.getReferenceId();
    }

    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());

        long startDate = request.getStartDate();
        long endDate = request.getEndDate();

        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(task -> task.getStatus() != TaskStatus.CANCELLED)
                .filter(task -> {
                    Long deadline = task.getTaskDeadlineTime();
                    boolean isInRange = deadline >= startDate && deadline <= endDate;
                    boolean isBeforeRangeAndActive = deadline < startDate && task.getStatus() != TaskStatus.COMPLETED;
                    return isInRange || isBeforeRangeAndActive;
                })
                .collect(Collectors.toList());

        return taskMapper.modelListToDtoList(filteredTasks);
    }

    @Override
    public String changeTaskPriority(Long taskId, Priority newPriority) {
        TaskManagement task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        task.setPriority(newPriority);
        taskRepository.save(task);
        return "Priority updated to " + newPriority + " for Task ID: " + taskId;
    }

    @Override
    public List<TaskManagementDto> getTasksByPriority(Priority priority) {
        List<TaskManagement> filteredTasks = taskRepository.getTasksByPriority(priority);
        return taskMapper.modelListToDtoList(filteredTasks);
    }

    @Override
    public void addCommentToTask(Long taskId, String author, String commentText) {
        TaskManagement task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        TaskComment comment = new TaskComment(taskId, author, commentText, LocalDateTime.now());
        taskComments.computeIfAbsent(taskId, k -> new ArrayList<>()).add(comment);

        TaskActivity activity = new TaskActivity(taskId, author + " added a comment.", LocalDateTime.now());
        taskActivities.computeIfAbsent(taskId, k -> new ArrayList<>()).add(activity);
    }
}
