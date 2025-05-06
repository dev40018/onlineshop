package com.myproject.simpleonlineshop.controller;


import com.myproject.simpleonlineshop.dto.ApiResponse;
import com.myproject.simpleonlineshop.dto.ImageDto;
import com.myproject.simpleonlineshop.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(path = "${api.prefix}/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    //parameters annotated with @RequestParams are declared by user in body of HttpRequest, @RequestParams is to get them
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(
            @RequestParam List<MultipartFile> files, // multiple images
            @RequestParam Long productId // the product you want to save associated images
    ){

        try {
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("Image Uploading was Successful", imageDtos));
            // below e object is the thrown Exception by saveImages() method so we are catching them below
        } catch (Exception e) {
            return  ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Image Uploading was NOT Successful: ", e.getMessage()));
        }

    }
}
