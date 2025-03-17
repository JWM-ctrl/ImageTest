package com.example.telros.service;

import com.example.telros.dto.PhotoDTO;
import com.example.telros.essence.Photo;
import com.example.telros.essence.User;
import com.example.telros.exception.ResourceNotFoundException;
import com.example.telros.mapper.UserMapper;
import com.example.telros.repository.PhotoDeleteRepository;
import com.example.telros.repository.PhotoRepository;
import com.example.telros.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с фотографиями пользователей.
 * Предоставляет методы для получения, создания, обновления и удаления фотографий.
 */
@AllArgsConstructor
@Service
@Slf4j
public class PhotoService {

    private final PhotoDeleteRepository photoDeleteRepository;

    private final UserRepository userRepository;

    private final PhotoRepository photoRepository;

    private final UserMapper userMapper;

    /**
     * Получает фотографию по её идентификатору.
     *
     * @param id идентификатор фотографии.
     * @return {@link PhotoDTO}, представляющий фотографию.
     * @throws ResourceNotFoundException если фотография с указанным идентификатором не найдена.
     */
    public PhotoDTO getPhotoById(long id) {
        log.info("Request to get photo with id: {}", id);
        return photoRepository.findById(id).map(userMapper::photoToPhotoDTO)
                .orElseThrow(()
                        -> new ResourceNotFoundException(String.format("No photo found with id: %s", id)));
    }

    /**
     * Создает новую фотографию для пользователя.
     *
     * @param photoDTO {@link PhotoDTO}, содержащий данные для создания фотографии.
     * @param userId идентификатор пользователя, для которого создается фотография.
     * @return {@link PhotoDTO}, представляющий созданную фотографию.
     * @throws ResourceNotFoundException если пользователь с указанным идентификатором не найден
     *                                   или у пользователя уже есть фотография.
     */
    public PhotoDTO createPhoto(PhotoDTO photoDTO, Long userId) {
        log.info("Request to create photo for user with id: {}", userId);
        Photo photo = userMapper.photoDTOToPhoto(photoDTO);
        User verifiableUser = userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException(String.format("No user found with userId: %s", userId)));
        if (verifiableUser.getPhoto() != null) {
            throw new ResourceNotFoundException(String.format("The photo already exists for user with id: %s", userId));
        }
        photo.setUser(verifiableUser);
        photo = photoRepository.save(photo);
        return userMapper.photoToPhotoDTO(photo);
    }

    /**
     * Обновляет существующую фотографию.
     *
     * @param id идентификатор фотографии, которую нужно обновить.
     * @param photoDTO {@link PhotoDTO}, содержащий новые данные для фотографии.
     * @return {@link PhotoDTO}, представляющий обновленную фотографию.
     * @throws ResourceNotFoundException если фотография с указанным идентификатором не найдена.
     */
    public PhotoDTO updatePhoto(long id, PhotoDTO photoDTO) {
        log.info("Request to update photo with id: {}", id);
        Photo photo = photoRepository
                .findById(id).orElseThrow(()
                        -> new ResourceNotFoundException(String.format("No photo found with id: %s ", id)));
        if (photo != null) {
            photo.setPhoto(photoDTO.getPhoto());
            photo = photoRepository.save(photo);
            return userMapper.photoToPhotoDTO(photo);
        }
        throw new ResourceNotFoundException(String.format("No photo found with id: %s ", id));
    }

    /**
     * Удаляет фотографию по её идентификатору.
     *
     * @param id идентификатор фотографии, которую нужно удалить.
     */
    public void deletePhoto(long id) {
        log.info("Request to delete photo with id: {}", id);
        photoDeleteRepository.deleteById(id);
    }
}
