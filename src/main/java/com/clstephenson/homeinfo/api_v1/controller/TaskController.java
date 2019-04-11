package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.exception.PropertyNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.TaskNotFoundException;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.Task;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import com.clstephenson.homeinfo.api_v1.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    PropertyService propertyService;

    @GetMapping("/task/property/{propertyId}")
    List<Task> getAllTasksByPropertyId(@PathVariable long propertyId) {
        if (propertyService.existsById(propertyId)) {
            return taskService.findByPropertyId(propertyId);
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/task/property/{propertyId}")
    Task createTaskWithPropertyId(@Valid @RequestBody Task newTask, @PathVariable long propertyId) {
        Property property = propertyService.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        newTask.setProperty(property);
        return taskService.save(newTask);
    }

    @GetMapping("/task/{taskId}")
    Task getTaskById(@PathVariable long taskId) {
        return taskService.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @PutMapping("/task/{taskId}")
    Task updateTask(@PathVariable long taskId, @Valid Task taskRequest) {
        return taskService.findById(taskId).map(task -> {
            // do not allow updates to property field of task after created
            task.setTask(taskRequest.getTask());
            task.setLastCompletionDate(taskRequest.getLastCompletionDate());
            task.setRecurring(taskRequest.getRecurring());
            task.setFrequencyInDays(taskRequest.getFrequencyInDays());
            task.setVendor(taskRequest.getVendor());
            return taskService.save(task);
        }).orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @DeleteMapping("/task/{taskId}")
    ResponseEntity<?> deleteTask(@PathVariable long taskId) {
        return taskService.findById(taskId).map(task -> {
            taskService.deleteById(taskId);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new TaskNotFoundException(taskId));
    }
}
