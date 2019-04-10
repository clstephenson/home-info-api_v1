package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import com.clstephenson.homeinfo.api_v1.service.UserService;
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
@WebMvcTest(PropertyController.class)
public class PropertyControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private UserService userService;

    private User user;
    private Property property;
    private List<Property> allProperties;
    private long validId = 1;
    private long invalidId = 100;

    @Before
    public void setup() {
        user = TestDataHelper.getTestUser();
        user.setId(validId);
        property = TestDataHelper.getTestProperty(user);
        property.setId(validId);
        allProperties = Collections.singletonList(property);

        given(userService.existsById(validId)).willReturn(true);
        given(userService.existsById(invalidId)).willReturn(false);
        given(userService.findById(validId)).willReturn(Optional.of(user));
        given(propertyService.getAll()).willReturn(allProperties);
        given(propertyService.findByUserId(validId)).willReturn(allProperties);
        given(propertyService.findById(validId)).willReturn(Optional.of(property));
        given(propertyService.save(property)).willReturn(property);
    }

    @Test
    public void givenProperties_whenGetProperties_thenReturn200AndJsonArrayContainingOneProperty() throws Exception {
        mockMvc.perform(get("/property"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(property.getName())));
    }

    @Test
    public void givenProperties_whenGetPropertiesByValidUserId_thenReturn200AndJsonArrayContainingOneProperty() throws Exception {
        mockMvc.perform(get("/property/user/" + validId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(property.getName())));
    }

    @Test
    public void givenProperties_whenGetPropertiesByInvalidUserId_thenReturn404() throws Exception {
        mockMvc.perform(get("/property/user/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenProperty_whenFindByValidId_thenReturn200AndJsonContainingProperty() throws Exception {
        mockMvc.perform(get("/property/" + validId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(property.getName())));
    }

    @Test
    public void givenProperty_whenFindByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(get("/property/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNewProperty_whenCreateForValidUser_theReturn201() throws Exception {
        mockMvc.perform(post("/property/user/" + validId)
                .content(objectMapper.writeValueAsString(property))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenNewProperty_whenCreateForInvalidUser_theReturn404() throws Exception {
        mockMvc.perform(post("/property/user/" + invalidId)
                .content(objectMapper.writeValueAsString(property))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenProperty_whenUpdateWithValidId_thenReturn200() throws Exception {
        mockMvc.perform(put("/property/" + validId)
                .content(objectMapper.writeValueAsString(property))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void givenProperty_whenUpdateWithInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(put("/property/" + invalidId)
                .content(objectMapper.writeValueAsString(property))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenProperty_whenDeleteByValidId_thenReturn200() throws Exception {
        mockMvc.perform(delete("/property/" + validId))
                .andExpect(status().isOk());
    }

    @Test
    public void givenProperty_whenDeleteByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(delete("/property/" + invalidId))
                .andExpect(status().isNotFound());
    }

}