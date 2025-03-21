package com.example.telros.service;


import com.example.telros.dto.PhotoDTO;
import com.example.telros.essence.Photo;
import com.example.telros.mapper.UserMapper;
import com.example.telros.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.telros.repository.PhotoDeleteRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PhotoServiceTest {

    @Mock
    private PhotoDeleteRepository photoDeleteRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private PhotoService photoService;

    @Test
    public void testGetPhotoById() {
        Long userId = 1L;
        byte[] imageData = "fake-image-data".getBytes();

        Photo photo = new Photo();
        photo.setId(userId);
        photo.setPhoto(imageData);

        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setId(userId);
        photoDTO.setPhoto(imageData);

        when(photoRepository.findById(userId)).thenReturn(Optional.of(photo));
        when(userMapper.photoToPhotoDTO(any(Photo.class))).thenReturn(photoDTO);
        PhotoDTO result = photoService.getPhotoById(userId);
        assertEquals(imageData, result.getPhoto());
    }

    @Test
    public void testUpdatePhoto() {
        Long userId = 1L;
        byte[] imageData = "fake-image-data".getBytes();

        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setPhoto(imageData);

        Photo photo = new Photo();
        photo.setId(userId);
        photo.setPhoto(imageData);

        when(photoRepository.findById(userId)).thenReturn(Optional.of(photo));
        when(photoRepository.save(any(Photo.class))).thenReturn(photo);
        when(userMapper.photoToPhotoDTO(any(Photo.class))).thenReturn(photoDTO);
        PhotoDTO result = photoService.updatePhoto(userId, photoDTO);
        assertEquals(imageData, result.getPhoto());
    }

    @Test
    public void testDeletePhoto() {
        Long userId = 1L;
        doNothing().when(photoDeleteRepository).deleteById(userId);
        photoService.deletePhoto(userId);
        verify(photoDeleteRepository, times(1)).deleteById(userId);
    }
}