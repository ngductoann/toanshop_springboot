package com.toan.toanshop.service;

import java.util.List;

import com.toan.toanshop.model.Image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image getImageById(Long id);

    void deleteImageById(Long id);

    Image saveImage(List<MultipartFile> file, Long productId);

    void updateImage(MultipartFile file, Long imageId);
}
