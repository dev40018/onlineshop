package com.myproject.simpleonlineshop.service;

import com.myproject.simpleonlineshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<Image> saveImages(List<MultipartFile> files, Long productId);
    Image updateImage(MultipartFile file, Long imageId);
}
