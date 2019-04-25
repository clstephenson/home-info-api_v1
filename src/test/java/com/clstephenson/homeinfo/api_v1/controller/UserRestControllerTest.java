package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.controller.rest.UserRestController;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.service.UserService;
import com.clstephenson.homeinfo.api_v1.testutils.TestDataHelper;
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
@WebMvcTest(UserRestController.class)
public class UserRestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User chris;
    private List<User> allUsers;
    private long validId = 1;
    private long invalidId = 100;

    @Before
    public void setup() {
        chris = TestDataHelper.getTestUser();
        chris.setId(validId);
        allUsers = Collections.singletonList(chris);

        given(userService.getAll()).willReturn(allUsers);
        given(userService.findById(validId)).willReturn(Optional.of(chris));
        given(userService.save(chris)).willReturn(chris);
    }

    @Test
    public void givenUsers_whenGetUsers_thenReturn200AndJsonArrayContainingOneUser() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", is(chris.getEmail())));
    }

    @Test
    public void givenUser_whenFindByValidId_thenReturn200AndJsonContainingUser() throws Exception {
        mockMvc.perform(get("/users/" + validId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is(chris.getEmail())));
    }

    @Test
    public void givenUser_whenFindByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(get("/users/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNewUser_whenCreateUser_theReturn201() throws Exception {
        mockMvc.perform(post("/users")
                .content(objectMapper.writeValueAsString(chris))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenUser_whenUpdateUser_thenReturn200() throws Exception {
        mockMvc.perform(put("/users/" + validId)
                .content(objectMapper.writeValueAsString(chris))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUser_whenUpdateUserWithInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(put("/users/" + invalidId)
                .content(objectMapper.writeValueAsString(chris))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUser_whenDeleteByValidId_thenReturn200() throws Exception {
        mockMvc.perform(delete("/users/" + validId))
                .andExpect(status().isOk());
    }

    @Test
    public void givenUser_whenDeleteByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(delete("/users/" + invalidId))
                .andExpect(status().isNotFound());
    }

}