package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.Task;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.model.Vendor;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import com.clstephenson.homeinfo.api_v1.service.TaskService;
import com.clstephenson.homeinfo.api_v1.testutils.TestDataHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private PropertyService propertyService;

    private Property property;
    private Task task;
    private List<Task> allTasks;
    private long validId = 1;
    private long invalidId = 100;

    @Before
    public void setup() {
        User user = TestDataHelper.getTestUser();
        user.setId(validId);
        property = TestDataHelper.getTestProperty(user);
        property.setId(validId);
        Vendor vendor = TestDataHelper.getTestVendor(user);
        vendor.setId(validId);
        task = TestDataHelper.getTestTask(property, vendor);
        task.setId(validId);
        allTasks = Collections.singletonList(task);

        given(propertyService.existsById(validId)).willReturn(true);
        given(propertyService.existsById(invalidId)).willReturn(false);
        given(propertyService.findById(validId)).willReturn(Optional.of(property));
        given(taskService.getAll()).willReturn(allTasks);
        given(taskService.findByPropertyId(validId)).willReturn(allTasks);
        given(taskService.findById(validId)).willReturn(Optional.of(task));
        given(taskService.save(task)).willReturn(task);
    }

    @Test
    public void whenGetByValidPropertyId_thenReturn200AndJsonArray() throws Exception {
        mockMvc.perform(get("/task/property/" + validId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].task", is(task.getTask())));
    }

    @Test
    public void whenGetByInvalidPropertyId_thenReturn404() throws Exception {
        mockMvc.perform(get("/task/property/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindByValidId_thenReturn200AndJson() throws Exception {
        mockMvc.perform(get("/task/" + validId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.task", is(task.getTask())));
    }

    @Test
    public void whenFindByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(get("/task/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateForValidProperty_theReturn201() throws Exception {
        mockMvc.perform(post("/task/property/" + validId)
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateForInvalidProperty_theReturn404() throws Exception {
        mockMvc.perform(post("/task/property/" + invalidId)
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateWithValidId_thenReturn200() throws Exception {
        mockMvc.perform(put("/task/" + validId)
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateWithInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(put("/task/" + invalidId)
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteByValidId_thenReturn200() throws Exception {
        mockMvc.perform(delete("/task/" + validId))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(delete("/task/" + invalidId))
                .andExpect(status().isNotFound());
    }

}