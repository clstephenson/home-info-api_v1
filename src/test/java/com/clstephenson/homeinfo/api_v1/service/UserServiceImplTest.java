package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.repository.UserRepository;
import com.clstephenson.homeinfo.api_v1.testutils.TestDataHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private User user1;
    private List<User> userList;

    @Before
    public void setUp() {
        user1 = TestDataHelper.getTestUser();
        User user2 = TestDataHelper.getTestUser();
        user2.setEmail("email@example.com");
        userList = Arrays.asList(user1, user2);

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(userRepository.findAll()).thenReturn(userList);
        when(userRepository.save(user1)).thenReturn(user1);
        doNothing().when(userRepository).deleteById(anyLong());
    }

    @Test
    public void whenGetAllUsers_thenReturnListWithTwoUsers() {
        List<User> found = userService.getAll();

        assertThat(found).size().isEqualTo(2);
    }

    @Test
    public void whenValidEmail_thenUserShouldBeFound() {
        User found = userService.findByEmail(user1.getEmail()).orElse(null);

        assertThat(found.getEmail())
                .isEqualTo(user1.getEmail());
    }

    @Test
    public void whenUserSaved_thenReturnSavedUser() {
        User saved = userService.save(user1);

        assertThat(saved).isEqualToIgnoringGivenFields(user1, "id");
    }

    @Test
    public void whenDeleteUser_thenVerifyRepositoryMethodCalled() {
        userService.deleteById(1L);

        verify(userRepository).deleteById(anyLong());
    }

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }
}