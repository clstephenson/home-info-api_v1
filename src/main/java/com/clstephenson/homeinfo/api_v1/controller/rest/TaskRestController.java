package com.clstephenson.homeinfo.api_v1.controller.rest;

import com.clstephenson.homeinfo.api_v1.exception.PropertyNotFoundException;
import com.clstephenson.homeinfo.api_v1.exception.TaskNotFoundException;
import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.StoredFile;
import com.clstephenson.homeinfo.api_v1.model.Task;
import com.clstephenson.homeinfo.api_v1.model.TaskList;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import com.clstephenson.homeinfo.api_v1.service.StoredFileService;
import com.clstephenson.homeinfo.api_v1.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class TaskRestController {

    private final MyLogger LOGGER = new MyLogger(TaskRestController.class);

    @Autowired
    TaskService taskService;

    @Autowired
    StoredFileService storedFileService;

    @Autowired
    PropertyService propertyService;

    @GetMapping("/apiv1/property/{propertyId}/tasks")
    ResponseEntity<TaskList> getAllTasksByPropertyId(@PathVariable long propertyId) {
        if (propertyService.existsById(propertyId)) {
            TaskList taskList = new TaskList();
            taskService.findByPropertyId(propertyId).forEach(task -> taskList.getTasks().add(task));
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(taskList);
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @GetMapping("/apiv1/property/{propertyId}/task/{taskId}")
    Task getTaskByIdAndPropertyId(@PathVariable long propertyId, @PathVariable long taskId) {
        if (propertyService.existsById(propertyId)) {
            return taskService.findById(taskId)
                    .orElseThrow(() -> new TaskNotFoundException(taskId));
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/apiv1/property/{propertyId}/task")
    Task createTaskWithPropertyId(@Valid @RequestBody Task newTask, @PathVariable long propertyId) {
        Property property = propertyService.findById(propertyId)
                .orElseThrow(() -> new PropertyNotFoundException(propertyId));
        newTask.setProperty(property);
        try {
            return taskService.save(newTask);
        } catch (Exception e) {
            LOGGER.warn("Could not save task. Message from server: " + e.getLocalizedMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    @PutMapping("/apiv1/property/{propertyId}/task/{taskId}")
    Task updateTask(@PathVariable long propertyId, @PathVariable long taskId, @Valid @RequestBody Task taskRequest) {
        if (propertyService.existsById(propertyId)) {
            return taskService.findById(taskId).map(task -> {
                // do not allow updates to property field of task after created
                task.setDescription(taskRequest.getDescription());
                task.setLastCompletionDate(taskRequest.getLastCompletionDate());
                task.setRecurring(taskRequest.getRecurring());
                task.setFrequencyInDays(taskRequest.getFrequencyInDays());
                task.setVendor(taskRequest.getVendor());
                return taskService.save(task);
            }).orElseThrow(() -> new TaskNotFoundException(taskId));
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }

    @DeleteMapping("/apiv1/property/{propertyId}/task/{taskId}")
    ResponseEntity<?> deleteTask(@PathVariable long propertyId, @PathVariable long taskId) {
        if (propertyService.existsById(propertyId)) {
            return taskService.findById(taskId).map(task -> {
                taskService.deleteById(taskId);
                storedFileService.deleteAllByCategoryAndCategoryItemId(StoredFile.FileCategory.TASK, taskId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }).orElseThrow(() -> new TaskNotFoundException(taskId));
        } else {
            throw new PropertyNotFoundException(propertyId);
        }
    }
}
