package com.railse.workforcemgmt.controller;

import com.railse.workforcemgmt.common.model.response.Response;
import com.railse.workforcemgmt.dto.*;
import com.railse.workforcemgmt.model.enums.Priority;
import com.railse.workforcemgmt.service.TaskManagementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-mgmt")
public class TaskManagementController {

    private final TaskManagementService taskManagementService;

    public TaskManagementController(TaskManagementService taskManagementService) {
        this.taskManagementService = taskManagementService;
    }

    // Feature 2: Change task priority
    @PatchMapping("/change-priority")
    public Response<String> changeTaskPriority(
            @RequestParam Long taskId,
            @RequestParam Priority priority) {
        return new Response<>(taskManagementService.changeTaskPriority(taskId, priority));
    }

    //  Get a task by ID (includes comments & activity history from Feature 3)
    @GetMapping("/{id}")
    public Response<TaskManagementDto> getTaskById(@PathVariable Long id) {
        return new Response<>(taskManagementService.findTaskById(id));
    }

    //  Create new tasks
    @PostMapping("/create")
    public Response<List<TaskManagementDto>> createTasks(@RequestBody TaskCreateRequest request) {
        return new Response<>(taskManagementService.createTasks(request));
    }

    //  Get tasks by priority
    @GetMapping("/priority/{priority}")
    public Response<List<TaskManagementDto>> getTasksByPriority(@PathVariable Priority priority) {
        return new Response<>(taskManagementService.getTasksByPriority(priority));
    }

    //  Update existing tasks
    @PostMapping("/update")
    public Response<List<TaskManagementDto>> updateTasks(@RequestBody UpdateTaskRequest request) {
        return new Response<>(taskManagementService.updateTasks(request));
    }

    // Assign tasks by reference ID and reference type
    @PostMapping("/assign-by-ref")
    public Response<String> assignByReference(@RequestBody AssignByReferenceRequest request) {
        return new Response<>(taskManagementService.assignByReference(request));
    }

    //  Feature 1: Smart fetch by date (includes pending tasks before start date)
    @PostMapping("/fetch-by-date/v2")
    public Response<List<TaskManagementDto>> fetchByDate(@RequestBody TaskFetchByDateRequest request) {
        return new Response<>(taskManagementService.fetchTasksByDate(request));
    }

    //  Feature 3: Add a comment to a task
    @PostMapping("/{taskId}/comments")
    public Response<String> addComment(
            @PathVariable Long taskId,
            @RequestBody TaskCommentDto commentDto) {
        taskManagementService.addCommentToTask(taskId, commentDto.getAuthor(), commentDto.getCommentText());
        return new Response<>("Comment added successfully.");
    }
}
