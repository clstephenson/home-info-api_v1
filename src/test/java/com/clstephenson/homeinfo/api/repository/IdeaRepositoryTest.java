package com.clstephenson.homeinfo.api.repository;

import com.clstephenson.homeinfo.api.IntegrationTest;
import com.clstephenson.homeinfo.api.JpaDataTest;
import com.clstephenson.homeinfo.model.Idea;
import com.clstephenson.homeinfo.model.Property;
import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.api.testutils.TestDataHelper;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class IdeaRepositoryTest extends JpaDataTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IdeaRepository ideaRepository;

    @Test
    @Category(IntegrationTest.class)
    public void whenFindByPropertyId_thenReturnIdeas() {
        // given
        User user = entityManager.persist(TestDataHelper.getTestUser());
        Property property = entityManager.persist(TestDataHelper.getTestProperty(user));
        Idea idea = TestDataHelper.getTestIdea(property);
        Long id = (Long) entityManager.persistAndGetId(idea);
        entityManager.flush();

        // when
        List<Idea> found = (List<Idea>) ideaRepository.findAllByPropertyId(property.getId());

        // then
        assertThat(found).size().isGreaterThan(0);
    }
}
