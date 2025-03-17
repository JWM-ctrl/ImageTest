package com.example.telros.service;

import com.example.telros.dto.UserDTO;
import com.example.telros.essence.User;
import com.example.telros.exception.ResourceNotFoundException;
import com.example.telros.exception.UserDTOMappingException;
import com.example.telros.mapper.UserMapper;
import com.example.telros.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с пользователями.
 * Предоставляет методы для создания, обновления, удаления и получения информации о пользователях.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    /**
     * Получает список всех пользователей.
     *
     * @return список {@link UserDTO}, представляющих всех пользователей.
     */
    public List<UserDTO> getAllUsers() {
        log.info("Request received to get all users");
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return {@link UserDTO}, представляющий пользователя.
     * @throws ResourceNotFoundException если пользователь с указанным идентификатором не найден.
     */
    public UserDTO getUserById(Long id) {
        log.info("Received user request with id: {}", id);
        return userRepository.findById(id)
                .map(userMapper::userToUserDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id: %s not found ", id)));
    }

    /**
     * Создает нового пользователя.
     *
     * @param userDTO {@link UserDTO}, содержащий данные для создания пользователя.
     * @return {@link UserDTO}, представляющий созданного пользователя.
     */
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        user = userRepository.save(user);
        log.info("User created: {}", user);
        return userMapper.userToUserDTO(user);
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param id идентификатор пользователя, которого нужно обновить.
     * @param userDTO {@link UserDTO}, содержащий новые данные пользователя.
     * @return {@link UserDTO}, представляющий обновленного пользователя.
     * @throws UserDTOMappingException если данные из {@link UserDTO} не могут быть преобразованы в сущность {@link User}.
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User updatedUser = userMapper.userDTOToUser(userDTO);
        log.info("Updating user with id: " + id);
        if (updatedUser == null) {
            throw new UserDTOMappingException("UserDTO cannot be mapped to User");
        }
        updatedUser.setId(id);
        User savedUser = userRepository.save(updatedUser);
        log.info("Saved user with id: " + savedUser.getId());
        return userMapper.userToUserDTO(savedUser);
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя, которого нужно удалить.
     */
    public void deleteUser(Long id) {
        log.info("Deleting user with id: " + id);
        userRepository.deleteById(id);
    }
}