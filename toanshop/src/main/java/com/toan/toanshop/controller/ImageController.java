package com.toan.toanshop.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import com.toan.toanshop.Exception.ResourceNotFoundException;
import com.toan.toanshop.dto.ImageDto;
import com.toan.toanshop.model.Image;
import com.toan.toanshop.response.ApiResponse;
import com.toan.toanshop.response.ImageResponse;
import com.toan.toanshop.service.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {

    @Autowired private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(
            @RequestParam List<MultipartFile> files, Long productId) {
        try {
            List<ImageDto> imageDtos = imageService.uploadImage(files, productId);
            return ResponseEntity.ok(new ApiResponse("Success", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexpected error", e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<ApiResponse> downloadImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);

            ByteArrayResource resource =
                    new ByteArrayResource(
                            image.getImage().getBytes(1, (int) image.getImage().length()));

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            new ApiResponse(
                                    "Success",
                                    new ImageResponse(
                                            resource, image.getFileName(), image.getFileType())));
        } catch (SQLException e) {
            return ResponseEntity.status(SERVICE_UNAVAILABLE)
                    .body(new ApiResponse("Database error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexpected error", e.getMessage()));
        }
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(
            @PathVariable long imageId, @RequestParam("file") MultipartFile file) {
        try {
            imageService.updateImage(file, imageId);
            return ResponseEntity.ok().body(new ApiResponse("Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Not found", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(SERVICE_UNAVAILABLE)
                    .body(new ApiResponse("Database error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexpected error", e.getMessage()));
        }
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deteleImage(@PathVariable Long imageId) {
        try {
            imageService.deleteImageById(imageId);
            return ResponseEntity.status(NO_CONTENT).body(new ApiResponse("Success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("Not found", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Unexpected error", e.getMessage()));
        }
    }
}
