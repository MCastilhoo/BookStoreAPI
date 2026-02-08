package com.br.BookStoreAPI.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class CloudinaryService {
    private Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    public void validateImage(MultipartFile file) throws IOException {
        if (file.isEmpty()){
            throw new FileNotFoundException("File not found");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.matches("image/(jpeg|jpg|png)")) {
            throw new RuntimeException("Invalid image type: " + contentType);
        }
        if (file.getSize() > 5 * 1024 * 1024){
            throw new RuntimeException("File too big");
        }
    }
    public String uploadImage(MultipartFile file) throws IOException {
        try {
            validateImage(file);

            var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Error processing image upload.", e);
        }
    }


}
