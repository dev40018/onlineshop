package com.myproject.simpleonlineshop.service.Impl;

import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.model.Image;
import com.myproject.simpleonlineshop.repository.ImageRepository;
import com.myproject.simpleonlineshop.service.ImageService;
import com.myproject.simpleonlineshop.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final ProductService productService;

    public ImageServiceImpl(ImageRepository imageRepository,
                            ProductService productService) {
        this.imageRepository = imageRepository;
        this.productService = productService;
    }

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no such image exists"));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id)
                .ifPresentOrElse(imageRepository::delete,
                        () -> {throw new ResourceNotFoundException("image not found");});
    }

    @Override
    public List<Image> saveImages(List<MultipartFile> files, Long productId) {

        return List.of();
    }

    @Override
    public Image updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename()); // getting image file name
            // Transferring binary data between Java applications and databases because object needs to be serialized to
            image.setImage(new SerialBlob(file.getBytes()));
            return imageRepository.save(image);
        }catch (IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
