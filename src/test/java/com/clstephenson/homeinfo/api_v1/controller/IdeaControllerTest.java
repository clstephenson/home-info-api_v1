package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.model.Idea;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.model.Vendor;
import com.clstephenson.homeinfo.api_v1.service.IdeaService;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
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
@WebMvcTest(IdeaController.class)
public class IdeaControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdeaService ideaService;

    @MockBean
    private PropertyService propertyService;

    private Property property;
    private Idea idea;
    private List<Idea> allIdeas;
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
        idea = TestDataHelper.getTestIdea(property);
        idea.setId(validId);
        allIdeas = Collections.singletonList(idea);

        given(propertyService.existsById(validId)).willReturn(true);
        given(propertyService.existsById(invalidId)).willReturn(false);
        given(propertyService.findById(validId)).willReturn(Optional.of(property));
        given(ideaService.getAll()).willReturn(allIdeas);
        given(ideaService.findByPropertyId(validId)).willReturn(allIdeas);
        given(ideaService.findById(validId)).willReturn(Optional.of(idea));
        given(ideaService.save(idea)).willReturn(idea);
    }

    @Test
    public void whenGetByValidPropertyId_thenReturn200AndJsonArray() throws Exception {
        mockMvc.perform(get("/idea/property/" + validId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(idea.getDescription())));
    }

    @Test
    public void whenGetByInvalidPropertyId_thenReturn404() throws Exception {
        mockMvc.perform(get("/idea/property/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindByValidId_thenReturn200AndJson() throws Exception {
        mockMvc.perform(get("/idea/" + validId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", is(idea.getDescription())));
    }

    @Test
    public void whenFindByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(get("/idea/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateForValidProperty_theReturn201() throws Exception {
        mockMvc.perform(post("/idea/property/" + validId)
                .content(objectMapper.writeValueAsString(idea))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateForInvalidProperty_theReturn404() throws Exception {
        mockMvc.perform(post("/idea/property/" + invalidId)
                .content(objectMapper.writeValueAsString(idea))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateWithValidId_thenReturn200() throws Exception {
        mockMvc.perform(put("/idea/" + validId)
                .content(objectMapper.writeValueAsString(idea))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateWithInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(put("/idea/" + invalidId)
                .content(objectMapper.writeValueAsString(idea))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteByValidId_thenReturn200() throws Exception {
        mockMvc.perform(delete("/idea/" + validId))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(delete("/idea/" + invalidId))
                .andExpect(status().isNotFound());
    }

}