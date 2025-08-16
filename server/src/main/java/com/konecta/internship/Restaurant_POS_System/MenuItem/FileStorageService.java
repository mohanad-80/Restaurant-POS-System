package com.konecta.internship.Restaurant_POS_System.MenuItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService 
{
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file, Long menuItemId) throws IOException {
        String fileName = file.getOriginalFilename();
        Path targetLocation = Paths.get(uploadDir + "/" + menuItemId + "/");
        Files.createDirectories(targetLocation);
        Path filePath = targetLocation.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/menu/" + menuItemId + "/" + fileName;
    }
}
