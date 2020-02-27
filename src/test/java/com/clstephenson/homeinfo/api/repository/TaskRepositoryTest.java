package com.clstephenson.homeinfo.api.repository;

import com.clstephenson.homeinfo.api.IntegrationTest;
import com.clstephenson.homeinfo.api.JpaDataTest;
import com.clstephenson.homeinfo.model.Property;
import com.clstephenson.homeinfo.model.Task;
import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.model.Vendor;
import com.clstephenson.homeinfo.api.testutils.TestDataHelper;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskRepositoryTest extends JpaDataTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @Category(IntegrationTest.class)
    public void whenFindByPropertyId_thenReturnTasks() {
        // given
        User user = entityManager.persist(TestDataHelper.getTestUser());
        Property property = entityManager.persist(TestDataHelper.getTestProperty(user));
        Vendor vendor = entityManager.persist(TestDataHelper.getTestVendor(user));
        Task task = TestDataHelper.getTestTask(property, vendor);
        Long id = (Long) entityManager.persistAndGetId(task);
        entityManager.flush();

        // when
        List<Task> found = (List<Task>) taskRepository.findAllByPropertyId(property.getId());

        // then
        assertThat(found.size()).isGreaterThan(0);
    }
}
