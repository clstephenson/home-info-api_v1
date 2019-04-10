package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.IntegrationTest;
import com.clstephenson.homeinfo.api_v1.model.Location;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.model.Window;
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
public class WindowRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WindowRespository windowRespository;

    @Test
    @Category(IntegrationTest.class)
    public void whenFindById_thenReturnWindow() {
        // given
        User user = entityManager.persist(TestDataHelper.getTestUser());
        Property property = entityManager.persist(TestDataHelper.getTestProperty(user));
        Location location = entityManager.persist(TestDataHelper.getTestLocation(property));
        Window window = TestDataHelper.getTestWindow(location);
        Long id = (Long) entityManager.persistAndGetId(window);
        entityManager.flush();

        // when
        boolean found = windowRespository.findById(id).isPresent();

        // then
        assertThat(found).isTrue();
    }
}
