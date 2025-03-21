package com.example.telros.controller;

import com.example.telros.dto.PhotoDTO;
import com.example.telros.service.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/photos")
public class PhotoController {

    private PhotoService photoService;


    @GetMapping(value = "/{photoId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("photoId") Long photoId) {
        PhotoDTO photoDTO = photoService.getPhotoById(photoId);
        return photoDTO != null ? photoDTO.getPhoto() : new byte[0];
    }


    @PostMapping(value = "/{photoId}", consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Void> setImage(@RequestBody byte[] image, @PathVariable("photoId") Long photoId) {
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setPhoto(image);
        photoService.createPhoto(photoDTO, photoId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping(value = "/{photoId}", consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Void> updateImage(@RequestBody byte[] image, @PathVariable("photoId") Long photoId) {
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setPhoto(image);
        photoService.updatePhoto(photoId, photoDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deleteImage(@PathVariable("photoId") Long photoId) {
        photoService.deletePhoto(photoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping(value = "/{photoId}/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhotoDTO> getPhotoInfo(@PathVariable("photoId") Long photoId) {
        PhotoDTO photoDTO = photoService.getPhotoById(photoId);
        if (photoDTO != null) {
            return ResponseEntity.ok(photoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
