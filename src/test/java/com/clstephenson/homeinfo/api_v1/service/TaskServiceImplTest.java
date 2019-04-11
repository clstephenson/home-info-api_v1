package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.Task;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.model.Vendor;
import com.clstephenson.homeinfo.api_v1.repository.TaskRepository;
import com.clstephenson.homeinfo.api_v1.testutils.TestDataHelper;
import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class TaskServiceImplTest {

    final long id = 1;
    @Autowired
    private TaskService service;
    @MockBean
    private TaskRepository repository;
    private Task task;
    private List<Task> taskList;

    @Before
    public void setUp() {
        User user = TestDataHelper.getTestUser();
        user.setId(id);
        Property property = TestDataHelper.getTestProperty(user);
        property.setId(id);
        Vendor vendor = TestDataHelper.getTestVendor(user);
        task = TestDataHelper.getTestTask(property, vendor);

        taskList = Collections.singletonList(task);

        when(repository.findAllByPropertyId(id)).thenReturn(taskList);
        when(repository.findAll()).thenReturn(taskList);
        when(repository.findById(id)).thenReturn(Optional.of(task));
        when(repository.save(task)).thenReturn(task);
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(anyLong());
    }

    @Test
    public void whenGetAll_thenReturnListWithOneTask() {
        List<Task> found = service.getAll();

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenGetByPropertyId_thenReturnListWithOneTask() {
        List<Task> found = service.findByPropertyId(id);

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenFindById_thenReturnTask() {
        Task found = service.findById(id).get();

        assertThat(found).isEqualTo(task);
    }

    @Test
    public void whenSaved_thenReturnSavedTask() {
        Task saved = service.save(task);

        assertThat(saved).isEqualToIgnoringGivenFields(task, "id");
    }

    @Test
    public void whenDelete_thenVerifyRepositoryMethodCalled() {
        service.deleteById(id);

        verify(repository).deleteById(anyLong());
    }

    @Test
    public void whenCheckIfExists_thenReturnTrue() {
        assertThat(service.existsById(id)).isTrue();
    }

    @TestConfiguration
    static class TaskServiceImplTestContextConfiguration {
        @Bean
        public TaskService taskService() {
            return new TaskServiceImpl();
        }
    }
}