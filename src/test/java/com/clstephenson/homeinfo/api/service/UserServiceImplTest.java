package com.clstephenson.homeinfo.api.service;

import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.api.repository.UserRepository;
import com.clstephenson.homeinfo.api.testutils.TestDataHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
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

    private User user;
    private List<User> userList;
    private long id = 1;

    @Before
    public void setUp() {
        user = TestDataHelper.getTestUser();
        userList = Collections.singletonList(user);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findAll()).thenReturn(userList);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.existsById(id)).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyLong());
    }

    @Test
    public void whenGetAllUsers_thenReturnListWithOneUser() {
        List<User> found = userService.getAll();

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenGetById_thenReturnUser() {
        User found = userService.findById(id).orElse(null);

        assertThat(found.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void whenValidEmail_thenUserShouldBeFound() {
        User found = userService.findByEmail(user.getEmail()).orElse(null);

        assertThat(found.getEmail())
                .isEqualTo(user.getEmail());
    }

    @Test
    public void whenUserSaved_thenReturnSavedUser() {
        User saved = userService.save(user);

        assertThat(saved).isEqualToIgnoringGivenFields(user, "id");
    }

    @Test
    public void whenDeleteUser_thenVerifyRepositoryMethodCalled() {
        userService.deleteById(id);

        verify(userRepository).deleteById(anyLong());
    }

    @Test
    public void whenCheckIfValidUserExists_thenReturnTrue() {
        assertThat(userService.existsById(id)).isTrue();
    }

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }
}