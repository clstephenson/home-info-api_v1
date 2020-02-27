package com.clstephenson.homeinfo.api.service;

import com.clstephenson.homeinfo.model.Feature;
import com.clstephenson.homeinfo.model.Location;
import com.clstephenson.homeinfo.model.Property;
import com.clstephenson.homeinfo.model.User;
import com.clstephenson.homeinfo.api.repository.FeatureRepository;
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
public class FeatureServiceImplTest {

    final long id = 1;
    @Autowired
    private FeatureService service;
    @MockBean
    private FeatureRepository repository;
    private Feature feature;
    private List<Feature> featureList;

    @Before
    public void setUp() {
        User user = TestDataHelper.getTestUser();
        user.setId(id);
        Property property = TestDataHelper.getTestProperty(user);
        property.setId(id);
        Location location = TestDataHelper.getTestLocation(property);
        location.setId(id);
        feature = TestDataHelper.getTestFeature(property, location);

        featureList = Collections.singletonList(feature);

        when(repository.findAllByPropertyId(id)).thenReturn(featureList);
        when(repository.findAllByPropertyIdAndLocationId(id, id)).thenReturn(featureList);
        when(repository.findAll()).thenReturn(featureList);
        when(repository.findById(id)).thenReturn(Optional.of(feature));
        when(repository.save(feature)).thenReturn(feature);
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(anyLong());
    }

    @Test
    public void whenGetAll_thenReturnListWithOneFeature() {
        List<Feature> found = service.getAll();

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenGetByPropertyId_thenReturnListWithOneFeature() {
        List<Feature> found = service.findByPropertyId(id);

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenGetByPropertyIdAndLocationId_thenReturnListWithOneFeature() {
        List<Feature> found = service.findByPropertyIdAndLocationId(id, id);

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenFindById_thenReturnFeature() {
        Feature found = service.findById(id).get();

        assertThat(found).isEqualTo(feature);
    }

    @Test
    public void whenSaved_thenReturnSavedFeature() {
        Feature saved = service.save(feature);

        assertThat(saved).isEqualToIgnoringGivenFields(feature, "id");
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
    static class FeatureServiceImplTestContextConfiguration {
        @Bean
        public FeatureService featureService() {
            return new FeatureServiceImpl();
        }
    }
}