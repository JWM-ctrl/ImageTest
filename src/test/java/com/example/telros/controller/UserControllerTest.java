package com.example.telros.controller;

import com.example.telros.dto.UserDTO;
import com.example.telros.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllUsers() throws Exception {
        // Подготовка данных
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Иван");

        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userDTO));

        // Настройка MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Иван"));
    }

    @Test
    public void testGetUserById() throws Exception {
        // Подготовка данных
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Иван");

        when(userService.getUserById(anyLong())).thenReturn(userDTO);

        // Настройка MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Иван"));
    }

    @Test
    public void testCreateUser() throws Exception {
        // Подготовка данных
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Иван");

        when(userService.createUser(any(UserDTO.class))).thenReturn(userDTO);

        // Настройка MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Иван"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Подготовка данных
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Иван");

        when(userService.updateUser(anyLong(), any(UserDTO.class))).thenReturn(userDTO);

        // Настройка MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Выполнение запроса и проверка результата
        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Иван"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Мокируем вызов сервиса
        doNothing().when(userService).deleteUser(anyLong());

        // Настройка MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Выполнение запроса и проверка результата
        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}