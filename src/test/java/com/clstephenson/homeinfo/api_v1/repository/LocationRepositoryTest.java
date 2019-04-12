package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.IntegrationTest;
import com.clstephenson.homeinfo.api_v1.JpaDataTest;
import com.clstephenson.homeinfo.api_v1.model.Location;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.testutils.TestDataHelper;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LocationRepositoryTest extends JpaDataTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    @Category(IntegrationTest.class)
    public void whenFindByPropertyId_thenReturnLocation() {
        // given
        User user = entityManager.persist(TestDataHelper.getTestUser());
        Property property = entityManager.persist(TestDataHelper.getTestProperty(user));
        Location location = TestDataHelper.getTestLocation(property);
        Long id = (Long) entityManager.persistAndGetId(location);
        entityManager.flush();

        // when
        List<Location> found = (List<Location>) locationRepository.findAllByPropertyId(property.getId());

        // then
        assertThat(found).size().isGreaterThan(0);
    }
}
