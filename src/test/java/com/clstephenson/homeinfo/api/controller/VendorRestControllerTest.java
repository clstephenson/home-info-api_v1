package com.clstephenson.homeinfo.api.controller;

import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.model.Vendor;
import com.clstephenson.homeinfo.api.service.UserService;
import com.clstephenson.homeinfo.api.service.VendorService;
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
@WebMvcTest(VendorRestController.class)
public class VendorRestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendorService vendorService;

    @MockBean
    private UserService userService;

    private User user;
    private Vendor vendor;
    private List<Vendor> vendorList;
    private long validId = 1;
    private long invalidId = 100;

    @Before
    public void setup() {
        user = TestDataHelper.getTestUser();
        user.setId(validId);
        vendor = TestDataHelper.getTestVendor(user);
        vendor.setId(validId);
        vendorList = Collections.singletonList(vendor);

        given(userService.existsById(validId)).willReturn(true);
        given(userService.existsById(invalidId)).willReturn(false);
        given(userService.findById(validId)).willReturn(Optional.of(user));
        given(vendorService.getAll()).willReturn(vendorList);
        given(vendorService.findByUserId(validId)).willReturn(vendorList);
        given(vendorService.findById(validId)).willReturn(Optional.of(vendor));
        given(vendorService.save(vendor)).willReturn(vendor);
    }

    @Test
    public void whenGetByValidUserId_thenReturn200AndJsonArray() throws Exception {
        mockMvc.perform(get("/vendor/user/" + validId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(vendor.getName())));
    }

    @Test
    public void whenGetByInvalidUserId_thenReturn404() throws Exception {
        mockMvc.perform(get("/vendor/user/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindByValidId_thenReturn200AndJson() throws Exception {
        mockMvc.perform(get("/vendor/" + validId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(vendor.getName())));
    }

    @Test
    public void whenFindByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(get("/vendor/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenCreateForValidUser_theReturn201() throws Exception {
        mockMvc.perform(post("/vendor/user/" + validId)
                .content(objectMapper.writeValueAsString(vendor))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreateForInvalidUser_theReturn404() throws Exception {
        mockMvc.perform(post("/vendor/user/" + invalidId)
                .content(objectMapper.writeValueAsString(vendor))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateWithValidId_thenReturn200() throws Exception {
        mockMvc.perform(put("/vendor/" + validId)
                .content(objectMapper.writeValueAsString(vendor))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateWithInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(put("/vendor/" + invalidId)
                .content(objectMapper.writeValueAsString(vendor))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteByValidId_thenReturn200() throws Exception {
        mockMvc.perform(delete("/vendor/" + validId))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(delete("/vendor/" + invalidId))
                .andExpect(status().isNotFound());
    }

}