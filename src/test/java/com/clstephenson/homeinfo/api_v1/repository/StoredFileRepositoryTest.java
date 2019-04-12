package com.clstephenson.homeinfo.api_v1.repository;

import com.clstephenson.homeinfo.api_v1.IntegrationTest;
import com.clstephenson.homeinfo.api_v1.JpaDataTest;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.StoredFile;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.testutils.TestDataHelper;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StoredFileRepositoryTest extends JpaDataTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StoredFileRepository storedFileRepository;

    @Test
    @Category(IntegrationTest.class)
    public void whenFindByPropertyId_thenReturnStoredFile() {
        // given
        User user = entityManager.persist(TestDataHelper.getTestUser());
        Property property = entityManager.persist(TestDataHelper.getTestProperty(user));
        StoredFile storedFile = TestDataHelper.getTestFile(property);
        Long id = (Long) entityManager.persistAndGetId(storedFile);
        entityManager.flush();

        // when
        List<StoredFile> found = (List<StoredFile>) storedFileRepository.findAllByPropertyId(property.getId());

        // then
        assertThat(found).size().isGreaterThan(0);
    }
}
