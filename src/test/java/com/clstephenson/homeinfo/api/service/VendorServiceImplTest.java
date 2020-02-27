package com.clstephenson.homeinfo.api.service;

import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.model.Vendor;
import com.clstephenson.homeinfo.api.repository.VendorRepository;
import com.clstephenson.homeinfo.api.testutils.TestDataHelper;
import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class VendorServiceImplTest {

    long userId = 1;
    long vendorId = 1;
    @Autowired
    private VendorService service;
    @MockBean
    private VendorRepository repository;
    private Vendor vendor;
    private List<Vendor> vendorList;

    @Before
    public void setUp() {
        User user = TestDataHelper.getTestUser();
        user.setId(userId);
        vendor = TestDataHelper.getTestVendor(user);
        vendor.setId(vendorId);
        vendorList = Collections.singletonList(vendor);

        when(repository.findByUserId(userId)).thenReturn(vendorList);
        when(repository.findAll()).thenReturn(vendorList);
        when(repository.findById(vendorId)).thenReturn(Optional.of(vendor));
        when(repository.save(vendor)).thenReturn(vendor);
        when(repository.existsById(vendorId)).thenReturn(true);
        doNothing().when(repository).deleteById(anyLong());
    }

    @Test
    public void whenGetAll_thenReturnListWithOneVendor() {
        List<Vendor> found = service.getAll();

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenGetByUserId_thenReturnListWithOneVendor() {
        List<Vendor> found = service.findByUserId(userId);

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenFindById_thenReturnVendor() {
        Vendor found = service.findById(vendorId).get();

        assertThat(found).isEqualTo(vendor);
    }

    @Test
    public void whenSaved_thenReturnSavedVendor() {
        Vendor saved = service.save(vendor);

        assertThat(saved).isEqualToIgnoringGivenFields(vendor, "id");
    }

    @Test
    public void whenDelete_thenVerifyRepositoryMethodCalled() {
        service.deleteById(userId);

        verify(repository).deleteById(anyLong());
    }

    @Test
    public void whenCheckIfExists_thenReturnTrue() {
        assertThat(service.existsById(vendorId)).isTrue();
    }

    @TestConfiguration
    static class VendorServiceImplTestContextConfiguration {
        @Bean
        public VendorService propertyService() {
            return new VendorServiceImpl();
        }
    }
}