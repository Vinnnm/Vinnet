package com.vinn.vinnet.util;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Helper {

    @Autowired
    private final GoogleDriveJSONConnector googleDriveJSONConnector;

    public boolean uploadImageToDrive(File file) throws GeneralSecurityException, IOException {
        //String imageUrl = null;
        boolean isUploaded = false;
        try {
            String folderId = "17BM9AZsj_A40GDmRMEPkU6DNJsAzJW05";
            Drive drive = googleDriveJSONConnector.createDriveService();    
            com.google.api.services.drive.model.File fileMetaData = new com.google.api.services.drive.model.File();
            fileMetaData.setName(file.getName());
            fileMetaData.setParents(Collections.singletonList(folderId));
            FileContent mediaContent = new FileContent("image/jpeg", file);
            com.google.api.services.drive.model.File uploadedFile = drive.files().create(fileMetaData, mediaContent).setFields("id").execute();
            System.out.println(uploadedFile.getName());
            //imageUrl = "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();
            //System.out.println("IMAGE URL : " + imageUrl);
            file.delete();
            isUploaded = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            isUploaded = false;
        }
        return isUploaded;
    }

    public static long getCurrentTimestamp() {
        Date createdAt = new Date();
        return createdAt.getTime();
    }
}
