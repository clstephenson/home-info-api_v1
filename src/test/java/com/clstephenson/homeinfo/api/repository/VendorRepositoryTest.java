package com.clstephenson.homeinfo.api.repository;

import com.clstephenson.homeinfo.api.IntegrationTest;
import com.clstephenson.homeinfo.api.JpaDataTest;
import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.model.Vendor;
import com.clstephenson.homeinfo.api.testutils.TestDataHelper;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VendorRepositoryTest extends JpaDataTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VendorRepository vendorRepository;

    @Test
    @Category(IntegrationTest.class)
    public void whenFindByUserId_thenReturnVendors() {
        // given
        User user = entityManager.persist(TestDataHelper.getTestUser());
        Vendor vendor = TestDataHelper.getTestVendor(user);
        entityManager.persistAndFlush(vendor);

        // when
        List<Vendor> found = (List<Vendor>) vendorRepository.findByUserId(user.getId());

        // then
        assertThat(found.size()).isGreaterThan(0);
    }

}
