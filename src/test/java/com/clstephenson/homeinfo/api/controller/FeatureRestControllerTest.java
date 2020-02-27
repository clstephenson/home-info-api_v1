package com.clstephenson.homeinfo.api.controller;

import com.clstephenson.homeinfo.model.Feature;
import com.clstephenson.homeinfo.model.Location;
import com.clstephenson.homeinfo.model.Property;
import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.security.SpringSecurityConfig;
import com.clstephenson.homeinfo.api.service.FeatureService;
import com.clstephenson.homeinfo.api.service.LocationService;
import com.clstephenson.homeinfo.api.service.PropertyService;
import com.clstephenson.homeinfo.api.testutils.TestDataHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

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
@WebMvcTest(FeatureRestController.class)
public class FeatureRestControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeatureService featureService;

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private LocationService locationService;

    @MockBean
    private SpringSecurityConfig springSecurityConfig;

    private Property property;
    private Feature feature;
    private List<Feature> allFeatures;
    private long validId = 1;
    private long invalidId = 100;

    @Before
    public void setup() {

        User user = TestDataHelper.getTestUser();
        user.setId(validId);
        property = TestDataHelper.getTestProperty(user);
        property.setId(validId);
        Location location = TestDataHelper.getTestLocation(property);
        location.setId(validId);
        feature = TestDataHelper.getTestFeature(property, location);
        feature.setId(validId);
        allFeatures = Collections.singletonList(feature);

        given(propertyService.existsById(validId)).willReturn(true);
        given(propertyService.existsById(invalidId)).willReturn(false);
        given(propertyService.findById(validId)).willReturn(Optional.of(property));
        given(locationService.existsById(validId)).willReturn(true);
        given(locationService.existsById(invalidId)).willReturn(false);
        given(featureService.getAll()).willReturn(allFeatures);
        given(featureService.findByPropertyId(validId)).willReturn(allFeatures);
        given(featureService.findByPropertyIdAndLocationId(validId, validId)).willReturn(allFeatures);
        given(featureService.findById(validId)).willReturn(Optional.of(feature));
        given(featureService.save(feature)).willReturn(feature);
    }

    @Test
    public void whenGetByValidPropertyId_thenReturn200AndJsonArray() throws Exception {
        mockMvc.perform(get(String.format("/property/%d/feature", validId)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(feature.getName())));
    }

    @Test
    public void whenGetByInvalidPropertyId_thenReturn404() throws Exception {
        mockMvc.perform(get(String.format("/property/%d/feature", invalidId)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetByValidPropertyIdAndLocationId_thenReturn200AndJsonArray() throws Exception {
        mockMvc.perform(get(String.format("/property/%d/location/%d/feature", validId, validId)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(feature.getName())));
    }

    @Test
    public void whenGetByInvalidPropertyIdAndValidLocationId_thenReturn404() throws Exception {
        mockMvc.perform(get(String.format("/property/%d/location/%d/feature", invalidId, validId)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetByValidPropertyIdAndInvalidLocationId_thenReturn404() throws Exception {
        mockMvc.perform(get(String.format("/property/%d/location/%d/feature", validId, invalidId)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindByValidPropertyIdAndValidFeatureId_thenReturn200AndJson() throws Exception {
        mockMvc.perform(get(String.format("/property/%d/feature/%d", validId, validId)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(feature.getName())));
    }

    @Test
    public void whenFindByInvalidPropertyId_thenReturn404() throws Exception {
        mockMvc.perform(get(String.format("/property/%d/feature/%d", invalidId, validId)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindByInvalidFeatureId_thenReturn404() throws Exception {
        mockMvc.perform(get(String.format("/property/%d/feature/%d", validId, invalidId)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateForValidProperty_theReturn201() throws Exception {
        mockMvc.perform(post(String.format("/property/%d/feature", validId))
                .content(objectMapper.writeValueAsString(feature))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateForInvalidProperty_theReturn404() throws Exception {
        mockMvc.perform(post(String.format("/property/%d/feature", invalidId))
                .content(objectMapper.writeValueAsString(feature))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateWithValidPropertyIdAndValidFeatureId_thenReturn200() throws Exception {
        mockMvc.perform(put(String.format("/property/%d/feature/%d", validId, validId))
                .content(objectMapper.writeValueAsString(feature))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateWithInvalidPropertyId_thenReturn404() throws Exception {
        mockMvc.perform(put(String.format("/property/%d/feature/%d", invalidId, validId))
                .content(objectMapper.writeValueAsString(feature))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateWithInvalidFeatureId_thenReturn404() throws Exception {
        mockMvc.perform(put(String.format("/property/%d/feature/%d", validId, invalidId))
                .content(objectMapper.writeValueAsString(feature))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteByValidId_thenReturn200() throws Exception {
        mockMvc.perform(delete(String.format("/property/%d/feature/%d", validId, validId)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteByInvalidPropertyId_thenReturn404() throws Exception {
        mockMvc.perform(delete(String.format("/property/%d/feature/%d", invalidId, validId)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteByInvalidFeatureId_thenReturn404() throws Exception {
        mockMvc.perform(delete(String.format("/property/%d/feature/%d", validId, invalidId)))
                .andExpect(status().isNotFound());
    }

}