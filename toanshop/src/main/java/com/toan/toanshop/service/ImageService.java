package com.toan.toanshop.service;

import com.toan.toanshop.dto.ImageDto;
import com.toan.toanshop.model.Image;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image getImageById(Long id);

    void deleteImageById(Long id);

    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);

    void updateImage(MultipartFile file, Long imageId);
}
