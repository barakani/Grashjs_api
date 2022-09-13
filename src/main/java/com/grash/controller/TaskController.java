package com.grash.controller;

import com.grash.dto.SuccessResponse;
import com.grash.dto.TaskPatchDTO;
import com.grash.exception.CustomException;
import com.grash.model.Task;
import com.grash.model.User;
import com.grash.model.WorkOrder;
import com.grash.service.TaskService;
import com.grash.service.UserService;
import com.grash.service.WorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@Api(tags = "task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final WorkOrderService workOrderService;

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 404, message = "Task not found")})
    public Optional<Task> getById(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Task> optionalTask = taskService.findById(id);
        if (optionalTask.isPresent()) {
            Task savedTask = optionalTask.get();
            if (hasAccess(user, savedTask)) {
                return optionalTask;
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else return null;
    }

    @PostMapping("")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied")})
    public Task create(@ApiParam("Task") @RequestBody Task taskReq, HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<WorkOrder> optionalWorkOrder = workOrderService.findById(taskReq.getWorkOrder().getId());
        if (optionalWorkOrder.isPresent()) {
            User workOrderCreator = userService.findById(optionalWorkOrder.get().getCreatedBy()).get();
            if (user.getCompany().getId().equals(workOrderCreator.getCompany().getId())) {
                return taskService.create(taskReq);
            } else throw new CustomException("Access denied", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Invalid Work Order", HttpStatus.NOT_ACCEPTABLE);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Task not found")})
    public Task patch(@ApiParam("Task") @RequestBody TaskPatchDTO task, @ApiParam("id") @PathVariable("id") Long id,
                      HttpServletRequest req) {
        User user = userService.whoami(req);
        Optional<Task> optionalTask = taskService.findById(id);

        if (optionalTask.isPresent()) {
            Task savedTask = optionalTask.get();
            if (hasAccess(user, savedTask)) {
                return taskService.update(id, task);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Task not found", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("permitAll()")
    @ApiResponses(value = {//
            @ApiResponse(code = 500, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "Task not found")})
    public ResponseEntity delete(@ApiParam("id") @PathVariable("id") Long id, HttpServletRequest req) {
        User user = userService.whoami(req);

        Optional<Task> optionalTask = taskService.findById(id);
        if (optionalTask.isPresent()) {
            Task savedTask = optionalTask.get();
            if (hasAccess(user, savedTask)) {
                taskService.delete(id);
                return new ResponseEntity(new SuccessResponse(true, "Deleted successfully"),
                        HttpStatus.OK);
            } else throw new CustomException("Forbidden", HttpStatus.FORBIDDEN);
        } else throw new CustomException("Task not found", HttpStatus.NOT_FOUND);
    }

    private boolean hasAccess(User user, Task task) {
        return user.getCompany().getId().equals(
                userService.findById(task.getWorkOrder().getCreatedBy()).get().getCompany().getId());
    }
}
