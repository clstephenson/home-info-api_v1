package com.clstephenson.homeinfo.web.controller;

import com.clstephenson.homeinfo.logging.MyLogger;
import com.clstephenson.homeinfo.model.UploadFileResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class FileController {

    private final MyLogger LOGGER = new MyLogger(LoggerFactory.getLogger(getClass()));

    @Autowired
    private RestTemplateHelper restTemplateHelper;

    @GetMapping("/file")
    public void downloadFile(Model model,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestParam(value = "id") Long fileId) {

        String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/storedFile/{fileId}";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Resource> responseEntity = restTemplate.getForEntity(endpoint, Resource.class, fileId);

        Resource resource = responseEntity.getBody();
        response.setContentType(responseEntity.getHeaders().getContentType().toString());
        response.setContentLength((int) responseEntity.getHeaders().getContentLength());
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", resource.getFilename()));

        try (InputStream inputStream = resource.getInputStream()) {
            StreamUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            LOGGER.info(String.format("A file was downloaded.  Filename was '%s'", resource.getFilename()));
        } catch (IOException ex) {
            String message = String.format("Error writing file to output stream.  Filename was '%s'", resource.getFilename());
            LOGGER.log(message);
            throw new RuntimeException(message, ex);
        }
    }

    @PostMapping(value = "/file", params = "action=upload")
    public RedirectView uploadFile(Model model, HttpServletRequest request,
                                   @RequestParam(value = "category") String category,
                                   @RequestParam(value = "category_item_id") Long categoryItemId,
                                   @RequestParam(value = "property_id") Long propertyId,
                                   @RequestParam("file") MultipartFile file) {

        if (file != null && file.getSize() > 0) {
            if (categoryItemId != null && categoryItemId > 0) {
                MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                map.add("fileName", file.getOriginalFilename());
                ByteArrayResource fileAsResource = null;
                try {
                    fileAsResource = new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    };
                } catch (IOException ex) {
                    String message = String.format("Error creating ByteArrayResource.  Filename was '%s'", file.getOriginalFilename());
                    LOGGER.log(message);
                    throw new RuntimeException(message, ex);
                }
                map.add("file", fileAsResource);

                String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/property/{propertyId}/storedFile/{category}/{categoryItemId}";
                UploadFileResponse response = restTemplateHelper.postForEntity(UploadFileResponse.class, endpoint, map,
                        propertyId, category, categoryItemId);
            } else {
                LOGGER.log("File was not uploaded... invalid category_item_id");
            }
        } else {
            LOGGER.log("Invalid file... did not save");
        }

        final String referer = request.getHeader("referer");
        LOGGER.info("Redirecting to " + referer);
        return new RedirectView(referer);
    }

    @PostMapping(value = "/file", params = "action=delete")
    public RedirectView deleteFile(Model model,
                                   HttpServletRequest request,
                                   @RequestParam(value = "id") Long fileId) {
        String message = "";
        if (fileId > 0) {
            String endpoint = ControllerHelper.getUrlBase(request) + "/apiv1/storedFile/{storedFileId}";
            restTemplateHelper.delete(endpoint, fileId);
            message = "The location was deleted.";
        } else {
            message = "The file could not be deleted.  Please try again.";
        }
        final String referer = request.getHeader("referer");
        LOGGER.info("File with id '" + fileId + "' deleted. Redirecting to " + referer);
        return new RedirectView(referer);
    }

}
