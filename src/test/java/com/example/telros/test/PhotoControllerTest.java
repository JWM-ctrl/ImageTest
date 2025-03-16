package com.example.telros.test;

import com.example.telros.controller.PhotoController;
import com.example.telros.dto.PhotoDTO;
import com.example.telros.service.PhotoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PhotoControllerTest {

    @Mock
    private PhotoService photoService;

    @InjectMocks
    private PhotoController photoController;

    private MockMvc mockMvc;

    @Test
    public void testGetImage() throws Exception {
        // Подготовка данных
        byte[] imageData = "fake-image-data".getBytes();
        Long userId = 1L;

        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setPhoto(imageData);

        when(photoService.getPhotoById(userId)).thenReturn(photoDTO);

        // Настройка MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(photoController).build();

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/photos/{userId}", userId)
                        .accept(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().bytes(imageData));
    }

    @Test
    public void testSetImage() throws Exception {
        // Подготовка данных
        byte[] imageData = "fake-image-data".getBytes();
        Long userId = 1L;

        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setPhoto(imageData);

        // Мокируем вызов сервиса
        when(photoService.createPhoto( any(PhotoDTO.class),eq(userId))).thenReturn(photoDTO);

        // Настройка MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(photoController).build();

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/photos/{userId}", userId)
                        .contentType(MediaType.IMAGE_JPEG_VALUE)
                        .content(imageData))
                .andExpect(status().isCreated());
        // Проверка, что метод был вызван
        verify(photoService, times(1)).createPhoto( any(PhotoDTO.class),eq(userId));
    }

    @Test
    public void testDeleteImage() throws Exception {
        // Подготовка данных
        Long userId = 1L;

        // Мокируем вызов сервиса
        doNothing().when(photoService).deletePhoto(userId);

        // Настройка MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(photoController).build();

        // Выполнение запроса и проверка результата
        mockMvc.perform(delete("/photos/{userId}", userId))
                .andExpect(status().isNoContent());
    }
}