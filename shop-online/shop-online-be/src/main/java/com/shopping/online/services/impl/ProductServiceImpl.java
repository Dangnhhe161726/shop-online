package com.shopping.online.services.impl;

import com.shopping.online.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    public static final String UPLOADS_FOLDER = "uploads";

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // update name file with UUID help to file name is unique
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        //Get path to folder save file
        Path uploadDir = Paths.get(UPLOADS_FOLDER);
        //Check path to folder save file have exist if not exist will create new folder
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        //get full path to folder save image + name image
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        //save file to folder
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
}
