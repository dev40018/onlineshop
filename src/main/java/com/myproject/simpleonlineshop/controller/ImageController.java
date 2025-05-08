package com.myproject.simpleonlineshop.controller;


import com.myproject.simpleonlineshop.dto.ApiResponse;
import com.myproject.simpleonlineshop.dto.ImageDto;
import com.myproject.simpleonlineshop.exception.ResourceNotFoundException;
import com.myproject.simpleonlineshop.model.Image;
import com.myproject.simpleonlineshop.service.ImageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
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
    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(
            @PathVariable("imageId") Long imageId) throws SQLException {

        Image imageById = imageService.getImageById(imageId);
        /*
        What getBytes() Does:
            Reads a specific number of bytes (length) starting from a specific position (pos) in the BLOB.
            Returns those bytes as a byte[] array.
            If the position is out of range or the BLOB is shorter than requested, it throws a SQLException.
         */
        ByteArrayResource resource = new ByteArrayResource(
                imageById.getImage().getBytes( 1, (int)imageById.getImage().length() ));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(imageById.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageById.getFileName() + "\"")
                .body(resource);
        /*
        .contentType(MediaType.parseMediaType(imageById.getFileType()))
            Sets the Content-Type header of the response (i.e., what kind of file is being returned).

            imageById.getFileType() returns a string, e.g., "image/jpeg", "application/pdf", etc.

            MediaType.parseMediaType(...) converts that string into a MediaType object.

            Purpose: Tell the browser/client what type of file is being sent.

        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageById.getFileName() + "\"")
                This sets the Content-Disposition header.

                "attachment; filename=\"..." tells the browser to:

                Treat the content as a downloadable file.

                Use the specified filename (imageById.getFileName()), e.g., "my_photo.jpg".

                Purpose: Make the browser show a download dialog with a custom file name.

            .body(resource)
                    resource is typically an object that implements the Resource interface (like ByteArrayResource, InputStreamResource, etc.).

                    This sets the actual body content of the HTTP response — the file data.

                    Purpose: Send the binary data (like an image or document) to the client.
         */
    }
    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(
            @PathVariable("imageId") Long imageId,
            @RequestBody MultipartFile file
    ){
        // first we find image
        try {
            Image imageById = imageService.getImageById(imageId);
            if (imageById != null){
                Image updatedImage = imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Update Successful", null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed", INTERNAL_SERVER_ERROR));
        /*
        Purpose of 500 INTERNAL_SERVER_ERROR:
            Generic Server Failure – It informs the client that something went wrong on the server, but the server cannot specify the exact problem.

            Fallback Error – Used when no other 5xx error code (e.g., 502 Bad Gateway, 503 Service Unavailable) fits the situation.

            Security Consideration – Prevents exposing sensitive internal errors (e.g., database failures, unhandled exceptions) to the client.

            Client Awareness – Lets the client know the failure is not their fault (unlike 4xx errors, which indicate client-side issues).
         */
    }
    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable("imageId") Long imageId){
        // first we find image
        try {
            Image imageById = imageService.getImageById(imageId);
            if (imageById != null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Delete Successful", null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed", INTERNAL_SERVER_ERROR));
    }
}
