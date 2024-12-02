package com.toan.toanshop.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.core.io.ByteArrayResource;

@AllArgsConstructor
@Data
public class ImageResponse {

    private ByteArrayResource resource;
    private String fileName;
    private String fileType;
}
