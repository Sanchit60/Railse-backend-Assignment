package com.railse.workforcemgmt.controller;

import com.railse.workforcemgmt.common.model.response.Response;
import com.railse.workforcemgmt.dto.*;
import com.railse.workforcemgmt.service.TaskManagementService;
import org.springframework.web.bind.annotation.*;
import com.railse.workforcemgmt.dto.*;
import com.railse.workforcemgmt.model.enums.Priority;


import java.util.List;

@RestController
@RequestMapping("/task-mgmt")
public class TaskManagementController {

    private final TaskManagementService taskManagementService;

    public TaskManagementController(TaskManagementService taskManagementService) {
        this.taskManagementService = taskManagementService;
    }

    @GetMapping("/{id}")
    public Response<TaskManagementDto> getTaskById(@PathVariable Long id){
        return new Response<>(taskManagementService.findTaskById(id));
    }

    @PostMapping("/create")
    public Response<List<TaskManagementDto>> createTasks(@RequestBody TaskCreateRequest request){

        return new Response<>(taskManagementService.createTasks(request));
    }

    @PostMapping("/update")
    public Response<List<com.railse.workforcemgmt.dto.TaskManagementDto>> updateTasks(@RequestBody com.railse.workforcemgmt.dto.UpdateTaskRequest request) {
        return new Response<>(taskManagementService.updateTasks(request));
    }

    @PostMapping("/assign-by-ref")
    public Response<String> assignByReference(@RequestBody AssignByReferenceRequest request) {
        return new Response<>(taskManagementService.assignByReference(request));
    }

    @PostMapping("/fetch-by-date/v2")
    public Response<List<com.railse.workforcemgmt.dto.TaskManagementDto>> fetchByDate(@RequestBody com.railse.workforcemgmt.dto.TaskFetchByDateRequest request) {
        return new Response<>(taskManagementService.fetchTasksByDate(request));
    }
    @PostMapping("/change-priority")
    public Response<String> changePriority(
            @RequestParam Long taskId,
            @RequestParam Priority priority) {
        return new Response<>(taskManagementService.changeTaskPriority(taskId, priority));
    }

    @GetMapping("/priority/{priority}")
    public Response<List<TaskManagementDto>> getTasksByPriority(
            @PathVariable com.railse.workforcemgmt.model.enums.Priority priority) {
        return new Response<>(taskManagementService.getTasksByPriority(priority));
    }

}
