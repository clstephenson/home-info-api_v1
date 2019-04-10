package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.IntegrationTest;
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
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Category(IntegrationTest.class)
    public void whenFindById_thenReturnUser() {
        // given
        User chris = TestDataHelper.getTestUser();
        Long id = (Long) entityManager.persistAndGetId(chris);
        entityManager.flush();

        // when
        boolean found = userRepository.findById(id).isPresent();

        // then
        assertThat(found).isTrue();
    }

    @Test
    @Category(IntegrationTest.class)
    public void whenFindByEmail_thenReturnUser() {
        // given
        User chris = TestDataHelper.getTestUser();
        entityManager.persistAndFlush(chris);

        // when
        User found = userRepository.findByEmail(chris.getEmail());

        // then
        assertThat(found.getEmail())
                .isEqualTo(chris.getEmail());
    }

}
