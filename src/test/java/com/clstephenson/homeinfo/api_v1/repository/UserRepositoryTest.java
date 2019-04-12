package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.IntegrationTest;
import com.clstephenson.homeinfo.api_v1.JpaDataTest;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.testutils.TestDataHelper;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest extends JpaDataTest {

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
        User found = userRepository.findByEmail(chris.getEmail()).get();

        // then
        assertThat(found.getEmail())
                .isEqualTo(chris.getEmail());
    }

}
