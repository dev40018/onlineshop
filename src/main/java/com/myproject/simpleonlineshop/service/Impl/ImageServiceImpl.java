package com.myproject.simpleonlineshop.service.Impl;

import com.myproject.simpleonlineshop.model.Image;
import com.myproject.simpleonlineshop.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public Image getImageById(Long id) {
        return null;
    }

    @Override
    public void deleteImageById(Long id) {

    }

    @Override
    public List<Image> saveImages(List<MultipartFile> files, Long productId) {
        return List.of();
    }

    @Override
    public Image updateImage(MultipartFile file, Long imageId) {
        return null;
    }
}
