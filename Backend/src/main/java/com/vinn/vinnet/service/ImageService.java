package com.vinn.vinnet.service;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.stereotype.Service;

import com.vinn.vinnet.dto.ImageResponse;

@Service
public interface ImageService {
    public String getPathToGoodleCredentials();
    public ImageResponse uploadImageToDrive(File file) throws GeneralSecurityException, IOException;
}
