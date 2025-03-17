package com.example.telros.service;


import com.example.telros.dto.UserDTO;
import com.example.telros.essence.User;
import com.example.telros.mapper.UserMapper;
import com.example.telros.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        User user = new User();
        user.setId(1L);
        user.setFirstName("Иван");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Иван");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);
        List<UserDTO> result = userService.getAllUsers();
        assertEquals(1, result.size());
        assertEquals("Иван", result.get(0).getFirstName());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Иван");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Иван");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);
        UserDTO result = userService.getUserById(1L);
        assertEquals("Иван", result.getFirstName());
    }

    @Test
    public void testCreateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Иван");

        User user = new User();
        user.setId(1L);
        user.setFirstName("Иван");

        when(userMapper.userDTOToUser(any(UserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);

        assertEquals("Иван", result.getFirstName());
    }

    @Test
    public void testUpdateUser() {

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("Иван");
        userDTO.setLastName("Иванов");
        userDTO.setMiddleName("Иванович");

        User user = new User();
        user.setId(1L);
        user.setFirstName("Иван");
        user.setLastName("Иванов");
        user.setMiddleName("Иванович");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        User updatedUser = new User();
        updatedUser.setFirstName("Иван");
        updatedUser.setLastName("Иванов");
        updatedUser.setMiddleName("Иванович");
        when(userMapper.userDTOToUser(any(UserDTO.class))).thenReturn(updatedUser);

        UserDTO result = userService.updateUser(1L, userDTO);

        assertEquals("Иван", result.getFirstName());
        assertEquals("Иванов", result.getLastName());
        assertEquals("Иванович", result.getMiddleName());
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}