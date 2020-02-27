package com.clstephenson.homeinfo.api.service;

import com.clstephenson.homeinfo.model.Property;
import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.api.repository.PropertyRepository;
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
public class PropertyServiceImplTest {

    long userId = 1;
    long propertyId = 1;
    @Autowired
    private PropertyService service;
    @MockBean
    private PropertyRepository repository;
    private Property property;
    private List<Property> propertyList;

    @Before
    public void setUp() {
        User user = TestDataHelper.getTestUser();
        user.setId(userId);
        property = TestDataHelper.getTestProperty(user);
        property.setId(propertyId);
        propertyList = Collections.singletonList(property);

        when(repository.findByUserId(userId)).thenReturn(propertyList);
        when(repository.findAll()).thenReturn(propertyList);
        when(repository.findById(propertyId)).thenReturn(Optional.of(property));
        when(repository.save(property)).thenReturn(property);
        when(repository.existsById(propertyId)).thenReturn(true);
        doNothing().when(repository).deleteById(anyLong());
    }

    @Test
    public void whenGetAll_thenReturnListWithOneProperty() {
        List<Property> found = service.getAll();

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenGetByUserId_thenReturnListWithOneProperty() {
        List<Property> found = service.findByUserId(userId);

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenFindById_thenReturnProperty() {
        Property found = service.findById(propertyId).get();

        assertThat(found).isEqualTo(property);
    }

    @Test
    public void whenSaved_thenReturnSavedProperty() {
        Property saved = service.save(property);

        assertThat(saved).isEqualToIgnoringGivenFields(property, "id");
    }

    @Test
    public void whenDelete_thenVerifyRepositoryMethodCalled() {
        service.deleteById(userId);

        verify(repository).deleteById(anyLong());
    }

    @Test
    public void whenCheckIfExists_thenReturnTrue() {
        assertThat(service.existsById(propertyId)).isTrue();
    }

    @TestConfiguration
    static class PropertyServiceImplTestContextConfiguration {
        @Bean
        public PropertyService propertyService() {
            return new PropertyServiceImpl();
        }
    }
}