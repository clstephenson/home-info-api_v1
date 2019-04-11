package com.clstephenson.homeinfo.api_v1.service;

import com.clstephenson.homeinfo.api_v1.model.Idea;
import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.repository.IdeaRepository;
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
public class IdeaServiceImplTest {

    final long id = 1;
    @Autowired
    private IdeaService service;
    @MockBean
    private IdeaRepository repository;
    private Idea idea;
    private List<Idea> ideaList;

    @Before
    public void setUp() {
        User user = TestDataHelper.getTestUser();
        user.setId(id);
        Property property = TestDataHelper.getTestProperty(user);
        property.setId(id);
        idea = TestDataHelper.getTestIdea(property);

        ideaList = Collections.singletonList(idea);

        when(repository.findAllByPropertyId(id)).thenReturn(ideaList);
        when(repository.findAll()).thenReturn(ideaList);
        when(repository.findById(id)).thenReturn(Optional.of(idea));
        when(repository.save(idea)).thenReturn(idea);
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(anyLong());
    }

    @Test
    public void whenGetAll_thenReturnListWithOneIdea() {
        List<Idea> found = service.getAll();

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenGetByUserId_thenReturnListWithOneIdea() {
        List<Idea> found = service.findByPropertyId(id);

        assertThat(found).size().isEqualTo(1);
    }

    @Test
    public void whenFindById_thenReturnIdea() {
        Idea found = service.findById(id).get();

        assertThat(found).isEqualTo(idea);
    }

    @Test
    public void whenSaved_thenReturnSavedIdea() {
        Idea saved = service.save(idea);

        assertThat(saved).isEqualToIgnoringGivenFields(idea, "id");
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
    static class IdeaServiceImplTestContextConfiguration {
        @Bean
        public IdeaService ideaService() {
            return new IdeaServiceImpl();
        }
    }
}