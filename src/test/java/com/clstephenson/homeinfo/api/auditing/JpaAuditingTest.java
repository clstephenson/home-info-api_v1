package com.clstephenson.homeinfo.api.auditing;

import com.clstephenson.homeinfo.api.IntegrationTest;
import com.clstephenson.homeinfo.api.JpaDataTest;
import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.api.repository.UserRepository;
import com.clstephenson.homeinfo.api.testutils.TestDataHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class JpaAuditingTest extends JpaDataTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setup() {
        user = userRepository.save(TestDataHelper.getTestUser());
    }

    @Test
    @Category(IntegrationTest.class)
    public void whenNewUserCreated_thenAuditDataIsGenerated() {
        assertThat(user.getCreatedBy()).isEqualTo("Chris");
        assertThat(user.getLastModifiedBy()).isEqualTo("Chris");
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getLastModifiedAt()).isNotNull();
    }

//    @Test
//    @Category(IntegrationTest.class)
//    public void whenUserUpdated_thenAuditDataIsUpdated() {
//        Date created = user.getCreatedAt();
//        Date lastModified = user.getLastModifiedAt();
//
//        user.setLastName("Smith");
//        userRepository.save(user);
//
//        userRepository.findById(user.getId())
//                .ifPresent(updatedUser -> {
//                    assertThat(updatedUser.getLastName()).isEqualTo("Smith");
//                    assertThat(updatedUser.getCreatedAt()).isEqualTo(created);
//                    assertThat(updatedUser.getLastModifiedAt()).isAfter(lastModified);
//                });
//    }
}
