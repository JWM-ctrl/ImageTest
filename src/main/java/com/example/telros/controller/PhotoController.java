package com.example.telros.controller;

import com.example.telros.dto.PhotoDTO;
import com.example.telros.repository.PhotoRepository;
import com.example.telros.service.PhotoService;
import jakarta.persistence.Entity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/photos")
public class PhotoController {

    private PhotoService photoService;

    // Получение фотографии пользователя по ID
    @GetMapping(value = "/{photoId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("photoId") Long photoId) {
        PhotoDTO photoDTO = photoService.getPhotoById(photoId);
        //доп проверка на null
        return photoDTO != null ? photoDTO.getPhoto() : new byte[0];//ResponseEntity.status(HttpStatus.CREATED).build()
    }

    // Загрузка фотографии пользователя
    @PostMapping(value = "/{userId}", consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Void> setImage(@RequestBody byte[] image, @PathVariable("userId") Long userId) {
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setPhoto(image);
        photoService.createPhoto(photoDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/{userId}", consumes = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Void> updateImage(@RequestBody byte[] image, @PathVariable("userId") Long userId) {
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setPhoto(image);
        photoService.updatePhoto(userId, photoDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteImage(@PathVariable("userId") Long userId) {
        photoService.deletePhoto(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //Возвращает метаданные о фотографии (например, ID и размер изображения) в формате JSON
    @GetMapping(value = "/{userId}/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhotoDTO> getPhotoInfo(@PathVariable("userId") Long userId) {
        PhotoDTO photoDTO = photoService.getPhotoById(userId);
        if (photoDTO != null) {
            return ResponseEntity.ok(photoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
