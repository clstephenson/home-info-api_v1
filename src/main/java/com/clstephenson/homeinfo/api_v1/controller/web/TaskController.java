package com.clstephenson.homeinfo.api_v1.controller.web;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import com.clstephenson.homeinfo.api_v1.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
public class TaskController {

    private final MyLogger LOGGER = new MyLogger(TaskController.class);

    @Autowired
    RestTemplateHelper restTemplateHelper;

    @GetMapping("/tasks")
    public String tasks(Model model, HttpServletRequest request,
                        @RequestParam(value = "user_id") long userId,
                        @RequestParam(value = "property_id") long propertyId) {
        String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/tasks";
        TaskList taskList = restTemplateHelper.getForEntity(TaskList.class, endpoint, propertyId);
        List<Task> tasks = taskList == null ? Collections.emptyList() : taskList.getTasks();

        model.addAttribute("userId", userId);
        model.addAttribute("propertyId", propertyId);
        model.addAttribute("tasks", tasks);
        return "/tasks";
    }

    @GetMapping("/task")
    public String task(Model model, HttpServletRequest request,
                       @RequestParam(value = "task_id", required = false) Long taskId,
                       @RequestParam(value = "property_id") Long propertyId,
                       @RequestParam(value = "user_id") Long userId) {
        Task task;
        List<StoredFile> files = Collections.emptyList();
        String action;
        if (taskId != null && taskId > 0) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/task/{taskId}";
            task = restTemplateHelper.getForEntity(Task.class, endpoint, propertyId, taskId);
            userId = task != null ? task.getProperty().getUser().getId() : 0;

            // get any files associated with the feature
            endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/storedFiles/{category}/{categoryItemId}";
            StoredFileList fileList = restTemplateHelper.getForEntity(
                    StoredFileList.class, endpoint, "TASK", task.getId());
            if (fileList != null) {
                files = fileList.getStoredFiles();
            }

            action = String.format("/task?user_id=%d", userId);
        } else {
            task = new Task();
            task.setProperty(new Property());
            task.getProperty().setId(propertyId);
            task.setVendor(new Vendor());
            action = String.format("/task?user_id=%d", userId);
        }

        // get the list of vendors
        String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/user/{userId}/vendors";
        VendorList vendorList = restTemplateHelper.getForEntity(VendorList.class, endpoint, userId);
        List<Vendor> vendors = vendorList == null ? Collections.emptyList() : vendorList.getVendors();

        model.addAttribute("files", files);
        model.addAttribute("category", StoredFile.FileCategory.TASK.toString());
        model.addAttribute("vendorList", vendors);
        model.addAttribute("action", action);
        model.addAttribute("userId", userId);
        model.addAttribute("task", task);
        return "/task";
    }

    @PostMapping(value = "/task", params = "action=save")
    public RedirectView saveTask(Model model, HttpServletRequest request,
                                 @ModelAttribute("task") Task task,
                                 @RequestParam(value = "user_id") Long userId) {
        String message = "";
        if (task != null) {
            Task savedTask;
            String endpoint;

            if (task.getVendor().getId() != null && task.getVendor().getId() < 1) {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/vendor/{vendorId}";
                Vendor vendor = restTemplateHelper.getForEntity(Vendor.class, endpoint, task.getVendor().getId());
                task.setVendor(vendor);
            }

            if (task.getId() == null || task.getId() < 1) {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/task";
                savedTask = restTemplateHelper.postForEntity(Task.class, endpoint, task,
                        task.getProperty().getId());
            } else {
                endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/task/{taskId}";
                savedTask = restTemplateHelper.putForEntity(Task.class, endpoint, task,
                        task.getProperty().getId(),
                        task.getId());
            }

            message = "The task was saved.";
            model.addAttribute("task", savedTask);
        } else {
            message = "The task could not be saved.  Please try again.";
        }
        model.addAttribute("message", message);
        final String destination = String.format("/tasks?property_id=%d&user_id=%d", task.getProperty().getId(), userId);
        return ControllerHelper.redirectTo(request.getRequestURL().toString(), destination, LOGGER);
    }

    @PostMapping(value = "/task", params = "action=delete")
    public RedirectView deleteTask(Model model, HttpServletRequest request,
                                   @ModelAttribute("task") Task task,
                                   @RequestParam(value = "user_id") Long userId) {
        String message = "";
        if (task != null) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/task/{taskId}";
            restTemplateHelper.delete(endpoint, task.getProperty().getId(), task.getId());

            message = "The task was deleted.";
        } else {
            message = "The task could not be deleted.  Please try again.";
        }
        model.addAttribute("message", message);
        final String destination = String.format("/tasks?property_id=%d&user_id=%d", task.getProperty().getId(), userId);
        return ControllerHelper.redirectTo(request.getRequestURL().toString(), destination, LOGGER);
    }
}
