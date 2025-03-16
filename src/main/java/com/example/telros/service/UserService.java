package com.example.telros.service;

import com.example.telros.dto.UserDTO;
import com.example.telros.essence.User;
import com.example.telros.exception.ResourceNotFoundException;
import com.example.telros.maper.UserMapper;
import com.example.telros.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::userToUserDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        user = userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        //  Проверка есть ли пользователь
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        // Преобразуем UserDTO в User
        User updatedUser = userMapper.userDTOToUser(userDTO);
        if (updatedUser == null) {
            throw new IllegalArgumentException("UserDTO cannot be mapped to User");
        }
        //обновление полей
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setMiddleName(updatedUser.getMiddleName());
        existingUser.setBirthDate(updatedUser.getBirthDate());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());

        // Сохраняем обновленного пользователя в базе данных
        User newUser = userRepository.save(existingUser);
        return userMapper.userToUserDTO(newUser);

    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}
