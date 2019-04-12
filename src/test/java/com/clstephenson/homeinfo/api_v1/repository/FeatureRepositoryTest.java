package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.IntegrationTest;
import com.clstephenson.homeinfo.api_v1.JpaDataTest;
import com.clstephenson.homeinfo.api_v1.model.Feature;
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


public class FeatureRepositoryTest extends JpaDataTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FeatureRepository featureRepository;

    @Test
    @Category(IntegrationTest.class)
    public void whenFindByPropertyId_thenReturnFeatures() {
        // given
        User user = entityManager.persist(TestDataHelper.getTestUser());
        Property property = entityManager.persist(TestDataHelper.getTestProperty(user));
        Location location = entityManager.persist(TestDataHelper.getTestLocation(property));
        Feature feature = TestDataHelper.getTestFeature(property, location);
        Long id = (Long) entityManager.persistAndGetId(feature);
        entityManager.flush();

        // when
        List<Feature> found = (List<Feature>) featureRepository.findAllByPropertyId(property.getId());

        // then
        assertThat(found).size().isGreaterThan(0);
    }

    @Test
    @Category(IntegrationTest.class)
    public void whenFindByPropertyIdAndLocationId_thenReturnFeatures() {
        // given
        User user = entityManager.persist(TestDataHelper.getTestUser());
        Property property = entityManager.persist(TestDataHelper.getTestProperty(user));
        Location location = entityManager.persist(TestDataHelper.getTestLocation(property));
        Feature feature = TestDataHelper.getTestFeature(property, location);
        Long id = (Long) entityManager.persistAndGetId(feature);
        entityManager.flush();

        // when
        List<Feature> found = (List<Feature>) featureRepository.findAllByPropertyIdAndLocationId(property.getId(), location.getId());

        // then
        assertThat(found).size().isGreaterThan(0);
    }
}
