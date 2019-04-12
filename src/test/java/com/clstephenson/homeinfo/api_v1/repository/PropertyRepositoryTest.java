package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.IntegrationTest;
import com.clstephenson.homeinfo.api_v1.JpaDataTest;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.testutils.TestDataHelper;
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
