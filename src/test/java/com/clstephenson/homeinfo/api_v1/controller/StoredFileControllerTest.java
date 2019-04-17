package com.clstephenson.homeinfo.api_v1.controller;

import com.clstephenson.homeinfo.api_v1.model.Property;
import com.clstephenson.homeinfo.api_v1.model.StoredFile;
import com.clstephenson.homeinfo.api_v1.model.UploadFileResponse;
import com.clstephenson.homeinfo.api_v1.model.User;
import com.clstephenson.homeinfo.api_v1.service.FileStorageService;
import com.clstephenson.homeinfo.api_v1.service.PropertyService;
import com.clstephenson.homeinfo.api_v1.service.StoredFileService;
import com.clstephenson.homeinfo.api_v1.testutils.TestDataHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(StoredFileController.class)
public class StoredFileControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoredFileService storedFileService;

    MockMultipartFile multipartFile = new MockMultipartFile("test.txt", "some text".getBytes());

    @MockBean
    private PropertyService propertyService;

    private Property property;
    private StoredFile storedFile;
    private List<StoredFile> allStoredFiles;
    private long validId = 1;
    private long invalidId = 100;
    @Mock
    UploadFileResponse uploadFileResponse;
    @MockBean
    private FileStorageService fileStorageService;

    @Before
    public void setup() {
        User user = TestDataHelper.getTestUser();
        user.setId(validId);
        property = TestDataHelper.getTestProperty(user);
        property.setId(validId);
        storedFile = TestDataHelper.getTestFile(property);
        storedFile.setId(validId);
        allStoredFiles = Collections.singletonList(storedFile);

        given(propertyService.existsById(validId)).willReturn(true);
        given(propertyService.existsById(invalidId)).willReturn(false);
        given(propertyService.findById(validId)).willReturn(Optional.of(property));
        given(storedFileService.getAll()).willReturn(allStoredFiles);
        given(storedFileService.findByPropertyId(validId)).willReturn(allStoredFiles);
        given(storedFileService.findById(validId)).willReturn(Optional.of(storedFile));
        given(storedFileService.save(storedFile, multipartFile)).willReturn(uploadFileResponse);
//        given(storedFileService.save(storedFile)).willReturn(storedFile);
    }

    @Test
    public void whenGetByValidPropertyId_thenReturn200AndJsonArray() throws Exception {
        mockMvc.perform(get(String.format("/property/%d/storedFile", validId)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid", is(storedFile.getUuid())));
    }

    @Test
    public void whenGetByInvalidPropertyId_thenReturn404() throws Exception {
        mockMvc.perform(get(String.format("/property/%d/storedFile", invalidId)))
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void whenCreateForValidProperty_thenReturn200() throws Exception {
////        mockMvc.perform(post(String.format("/property/%d/storedFile", validId))
////                .content(multipartFile.getBytes())
////                .contentType(MediaType.MULTIPART_FORM_DATA))
////                .andExpect(status().isOk());
//
//        mockMvc.perform(MockMvcRequestBuilders
//                .multipart(String.format("/property/%d/storedFile", validId))
//                .file("file", multipartFile.getBytes())
//                .characterEncoding("UTF-8"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void whenCreateForInvalidProperty_theReturn404() throws Exception {
//        mockMvc.perform(post("/storedFile/property/" + invalidId)
//                .content(objectMapper.writeValueAsString(storedFile))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isNotFound());
//    }

//    @Test
//    public void whenUpdateWithValidId_thenReturnNotImplemented() throws Exception {
//        mockMvc.perform(put("/storedFile/" + validId)
//                .content(objectMapper.writeValueAsString(storedFile))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isNotImplemented());
//    }
//
//    @Test
//    public void whenUpdateWithInvalidId_thenReturn404() throws Exception {
//        mockMvc.perform(put("/storedFile/" + invalidId)
//                .content(objectMapper.writeValueAsString(storedFile))
//                .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    public void whenDeleteByValidId_thenReturnNotImplemented() throws Exception {
//        mockMvc.perform(delete("/storedFile/" + validId))
//                .andExpect(status().isNotImplemented());
//    }
//
//    @Test
//    public void whenDeleteByInvalidId_thenReturn404() throws Exception {
//        mockMvc.perform(delete("/storedFile/" + invalidId))
//                .andExpect(status().isNotFound());
//    }

}