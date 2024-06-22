package com.vinn.vinnet.service.implement;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.stereotype.Service;

import com.vinn.vinnet.dto.ImageResponse;
import com.vinn.vinnet.service.ImageService;
import com.vinn.vinnet.util.GoogleDriveJSONConnector;

@Service
public class ImageServiceImplement implements ImageService {

    @Override
    public String getPathToGoodleCredentials() {
        return GoogleDriveJSONConnector.getPathToGoodleCredentials();
    }

    @Override
    public ImageResponse uploadImageToDrive(File file) throws GeneralSecurityException, IOException {
        return new GoogleDriveJSONConnector().uploadImageToDrive(file);
    }
    
}
