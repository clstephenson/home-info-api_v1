package com.clstephenson.homeinfo.api.controller;

import com.clstephenson.homeinfo.api.service.LocationService;
import com.clstephenson.homeinfo.api.service.PropertyService;
import com.clstephenson.homeinfo.api.service.StoredFileService;
import com.clstephenson.homeinfo.api.testutils.TestDataHelper;
import com.clstephenson.homeinfo.model.Location;
import com.clstephenson.homeinfo.model.Property;
import com.clstephenson.homeinfo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
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

@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(LocationRestController.class)
@ContextConfiguration(classes = {StoredFileService.class})
public class LocationRestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @MockBean
    private PropertyService propertyService;

    private Property property;
    private Location location;
    private List<Location> allLocations;
    private long validId = 1;
    private long invalidId = 100;

    @Before
    public void setup() {
        User user = TestDataHelper.getTestUser();
        user.setId(validId);
        property = TestDataHelper.getTestProperty(user);
        property.setId(validId);
        location = TestDataHelper.getTestLocation(property);
        location.setId(validId);
        allLocations = Collections.singletonList(location);

        given(propertyService.existsById(validId)).willReturn(true);
        given(propertyService.existsById(invalidId)).willReturn(false);
        given(propertyService.findById(validId)).willReturn(Optional.of(property));
        given(locationService.getAll()).willReturn(allLocations);
        given(locationService.findByPropertyId(validId)).willReturn(allLocations);
        given(locationService.findById(validId)).willReturn(Optional.of(location));
        given(locationService.save(location)).willReturn(location);
    }

    @Test
    public void whenGetByValidPropertyId_thenReturn200AndJsonArray() throws Exception {
        mockMvc.perform(get("/location/property/" + validId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(location.getName())));
    }

    @Test
    public void whenGetByInvalidPropertyId_thenReturn404() throws Exception {
        mockMvc.perform(get("/location/property/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindByValidId_thenReturn200AndJson() throws Exception {
        mockMvc.perform(get("/location/" + validId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(location.getName())));
    }

    @Test
    public void whenFindByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(get("/location/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateForValidProperty_theReturn201() throws Exception {
        mockMvc.perform(post("/location/property/" + validId)
                .content(objectMapper.writeValueAsString(location))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateForInvalidProperty_theReturn404() throws Exception {
        mockMvc.perform(post("/location/property/" + invalidId)
                .content(objectMapper.writeValueAsString(location))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateWithValidId_thenReturn200() throws Exception {
        mockMvc.perform(put("/location/" + validId)
                .content(objectMapper.writeValueAsString(location))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateWithInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(put("/location/" + invalidId)
                .content(objectMapper.writeValueAsString(location))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteByValidId_thenReturn200() throws Exception {
        mockMvc.perform(delete("/location/" + validId))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(delete("/location/" + invalidId))
                .andExpect(status().isNotFound());
    }

}