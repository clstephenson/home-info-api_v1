package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Location;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.repository.LocationRepository;
import com.clstephenson.homeinfo.api_v1.testutils.TestDataHelper;
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
public class LocationServiceImplTest {

    final long id = 1;
    @Autowired
    private LocationService service;
    @MockBean
    private LocationRepository repository;
    private Location location;
    private List<Location> locationList;

    @Before
    public void setUp() {
        User user = TestDataHelper.getTestUser();
        user.setId(id);
        Property property = TestDataHelper.getTestProperty(user);
        property.setId(id);
        location = TestDataHelper.getTestLocation(property);

        locationList = Collections.singletonList(location);

        when(repository.findAllByPropertyId(id)).thenReturn(locationList);
        when(repository.findAll()).thenReturn(locationList);
        when(repository.findById(id)).thenReturn(Optional.of(location));
        when(repository.save(location)).thenReturn(location);
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(anyLong());
    }

    @Test
    public void whenGetAll_thenReturnListWithOneLocation() {
        List<Location> found = service.getAll();

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenGetByUserId_thenReturnListWithOneLocation() {
        List<Location> found = service.findByPropertyId(id);

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenFindById_thenReturnLocation() {
        Location found = service.findById(id).get();

        assertThat(found).isEqualTo(location);
    }

    @Test
    public void whenSaved_thenReturnSavedLocation() {
        Location saved = service.save(location);

        assertThat(saved).isEqualToIgnoringGivenFields(location, "id");
    }

    @Test
    public void whenDelete_thenVerifyRepositoryMethodCalled() {
        service.deleteById(id);

        verify(repository).deleteById(anyLong());
    }

    @Test
    public void whenCheckIfExists_thenReturnTrue() {
        assertThat(service.existsById(id)).isTrue();
    }

    @TestConfiguration
    static class LocationServiceImplTestContextConfiguration {
        @Bean
        public LocationService locationService() {
            return new LocationServiceImpl();
        }
    }
}