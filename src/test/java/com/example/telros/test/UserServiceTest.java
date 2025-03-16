package com.example.telros.test;


import com.example.telros.dto.UserDTO;
import com.example.telros.essence.User;
import com.example.telros.maper.UserMapper;
import com.example.telros.repository.UserRepository;
import com.example.telros.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetAllUsers() {
        // Подготовка данных
        User user = new User();
        user.setId(1L);
        user.setFirstName("Иван");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Иван");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        // Выполнение теста
        List<UserDTO> result = userService.getAllUsers();

        // Проверка результата
        assertEquals(1, result.size());
        assertEquals("Иван", result.get(0).getFirstName());
    }

    @Test
    public void testGetUserById() {
        // Подготовка данных
        User user = new User();
        user.setId(1L);
        user.setFirstName("Иван");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Иван");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        // Выполнение теста
        UserDTO result = userService.getUserById(1L);

        // Проверка результата
        assertEquals("Иван", result.getFirstName());
    }

    @Test
    public void testCreateUser() {
        // Подготовка данных
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Иван");

        User user = new User();
        user.setId(1L);
        user.setFirstName("Иван");

        when(userMapper.userDTOToUser(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        // Выполнение теста
        UserDTO result = userService.createUser(userDTO);

        // Проверка результата
        assertEquals("Иван", result.getFirstName());
    }

    @Test
    public void testUpdateUser() {
        // Подготовка данных
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Иван");
        userDTO.setLastName("Иванов");
        userDTO.setMiddleName("Иванович");

        User user = new User();
        user.setId(1L);
        user.setFirstName("Иван");
        user.setLastName("Иванов");
        user.setMiddleName("Иванович");

        // Мокируем вызовы
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        // Настройка маппера для преобразования UserDTO в User
        User updatedUser = new User();
        updatedUser.setFirstName("Иван");
        updatedUser.setLastName("Иванов");
        updatedUser.setMiddleName("Иванович");
        when(userMapper.userDTOToUser(any(UserDTO.class))).thenReturn(updatedUser);

        // Выполнение теста
        UserDTO result = userService.updateUser(1L, userDTO);

        // Проверка результата
        assertEquals("Иван", result.getFirstName());
        assertEquals("Иванов", result.getLastName());
        assertEquals("Иванович", result.getMiddleName());
    }

    @Test
    public void testUpdateUserIfNull() {
        // Подготовка данных
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Иван");

        // Мокируем вызов findById, чтобы вернуть Optional.empty()
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Выполнение теста и проверка исключения
        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L, userDTO);
        });

        // Проверка, что save не вызывался
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testDeleteUser() {
        // Мокируем вызов репозитория
        doNothing().when(userRepository).deleteById(1L);

        // Выполнение теста
        userService.deleteUser(1L);

        // Проверка, что метод был вызван
        verify(userRepository, times(1)).deleteById(1L);
    }
}