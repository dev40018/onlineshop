package com.myproject.simpleonlineshop.service.Impl;

import com.myproject.simpleonlineshop.dto.ImageDto;
import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.model.Image;
import com.myproject.simpleonlineshop.model.Product;
import com.myproject.simpleonlineshop.repository.ImageRepository;
import com.myproject.simpleonlineshop.service.ImageService;
import com.myproject.simpleonlineshop.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    @Transactional
    public void deleteImageById(Long id) {
        imageRepository.findById(id)
                .ifPresentOrElse(imageRepository::delete,
                        () -> {throw new ResourceNotFoundException("image not found");});
    }

    @Override
    @Transactional
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {

        //first we look for the product that we want to save associated images
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDtos = new ArrayList<>();
        for(MultipartFile file: files){
             try {
                 // initializing a new image object because we can't save file directly to database because first it needs to be serialized
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product); // this product is the product that we found
                 // now we need to build a downloadUrl
                 String downloadUrl = "/api/v1/images/image/download/";
                 String specificImageDownloadUrl = downloadUrl + image.getId();
                 image.setDownloadUrl(specificImageDownloadUrl);
                 // but the problem is id is generated after save() method
                 Image savedImage =  imageRepository.save(image);

                 // No need for second save - Hibernate tracks changes, not using saveAndFlush()
                 //JPA will automatically persist changes during transaction commit
                 savedImage.setDownloadUrl(downloadUrl + savedImage.getId());


                 ImageDto imageDto = new ImageDto();
                 imageDto.setImageName(savedImage.getFileName());
                 imageDto.setImageId(savedImage.getId());
                 imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                 savedImageDtos.add(imageDto);

             }catch (IOException | SQLException e){
                 throw new RuntimeException(e.getMessage());
             }
        }
        return savedImageDtos;
    }
    private ImageDto convertToDto(Image image) {
        ImageDto dto = new ImageDto();
        dto.setImageId(image.getId());
        dto.setImageName(image.getFileName());
        dto.setDownloadUrl(image.getDownloadUrl());
        return dto;
    }

    @Override
    @Transactional
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
