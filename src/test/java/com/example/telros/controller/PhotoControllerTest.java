package com.example.telros.controller;

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

        byte[] imageData = "fake-image-data".getBytes();
        Long userId = 1L;

        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setPhoto(imageData);
        when(photoService.getPhotoById(userId)).thenReturn(photoDTO);
        mockMvc = MockMvcBuilders.standaloneSetup(photoController).build();
        mockMvc.perform(get("/photos/{userId}", userId)
                        .accept(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().bytes(imageData));
    }

    @Test
    public void testSetImage() throws Exception {

        byte[] imageData = "fake-image-data".getBytes();
        Long userId = 1L;

        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setPhoto(imageData);

        when(photoService.createPhoto(any(PhotoDTO.class), eq(userId))).thenReturn(photoDTO);
        mockMvc = MockMvcBuilders.standaloneSetup(photoController).build();

        mockMvc.perform(post("/photos/{userId}", userId)
                        .contentType(MediaType.IMAGE_JPEG_VALUE)
                        .content(imageData))
                .andExpect(status().isCreated());
        verify(photoService, times(1)).createPhoto(any(PhotoDTO.class), eq(userId));
    }

    @Test
    public void testDeleteImage() throws Exception {
        Long userId = 1L;
        doNothing().when(photoService).deletePhoto(userId);

        mockMvc = MockMvcBuilders.standaloneSetup(photoController).build();
        mockMvc.perform(delete("/photos/{userId}", userId))
                .andExpect(status().isNoContent());
    }
}