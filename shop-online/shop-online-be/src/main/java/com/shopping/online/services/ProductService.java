package com.shopping.online.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    String storeFile(MultipartFile file) throws IOException;
}
