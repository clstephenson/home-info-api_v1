package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.IntegrationTest;
import com.clstephenson.homeinfo.api_v1.model.Location;
import com.clstephenson.homeinfo.api_v1.model.Plant;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.testutils.TestDataHelper;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class PlantRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlantRepository plantRepository;

    @Test
    @Category(IntegrationTest.class)
    public void whenFindById_thenReturnPlant() {
        // given
        User user = entityManager.persist(TestDataHelper.getTestUser());
        Property property = entityManager.persist(TestDataHelper.getTestProperty(user));
        Location location = entityManager.persist(TestDataHelper.getTestLocation(property));
        Plant plant = TestDataHelper.getTestPlant(property, location);
        Long id = (Long) entityManager.persistAndGetId(plant);
        entityManager.flush();

        // when
        boolean found = plantRepository.findById(id).isPresent();

        // then
        assertThat(found).isTrue();
    }
}
