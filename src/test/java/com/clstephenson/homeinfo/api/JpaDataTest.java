package com.clstephenson.homeinfo.api;

import com.clstephenson.homeinfo.api.auditing.AuditorAwareImpl;
import com.clstephenson.homeinfo.api.auditing.JpaAuditingConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = ASSIGNABLE_TYPE,
        classes = {AuditorAwareImpl.class, JpaAuditingConfig.class}
))
@ActiveProfiles({"test"})
public class JpaDataTest implements IntegrationTest {
    @Test
    public void dummyTest() {

    }
}
