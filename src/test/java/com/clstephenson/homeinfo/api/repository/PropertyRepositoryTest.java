package com.clstephenson.homeinfo.api.repository;

import com.clstephenson.homeinfo.api.IntegrationTest;
import com.clstephenson.homeinfo.api.JpaDataTest;
import com.clstephenson.homeinfo.model.Property;
import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.api.testutils.TestDataHelper;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyRepositoryTest extends JpaDataTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PropertyRepository propertyRepository;

    @Test
    @Category(IntegrationTest.class)
    public void whenFindByUserId_thenReturnProperties() {
        // given
        User user = entityManager.persist(TestDataHelper.getTestUser());
        Property property = TestDataHelper.getTestProperty(user);
        entityManager.persistAndFlush(property);

        // when
        List<Property> found = (List<Property>) propertyRepository.findByUserId(user.getId());

        // then
        assertThat(found.size()).isGreaterThan(0);
    }

}
