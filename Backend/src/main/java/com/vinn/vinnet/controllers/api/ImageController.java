package com.vinn.vinnet.controllers.api;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vinn.vinnet.dto.ImageResponse;
import com.vinn.vinnet.service.ImageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class ImageController {
    
    @Autowired
    private ImageService imageService;

    @PostMapping("/uploadImage")
    public Object handleFileUpload(@RequestParam ("image") MultipartFile file) throws IOException, GeneralSecurityException {
        if (file.isEmpty()) {
            return "File is empty";
        }
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        ImageResponse imageResponse = imageService.uploadImageToDrive(tempFile);
        System.out.println(imageResponse);
        return imageResponse;
    }
    
}
