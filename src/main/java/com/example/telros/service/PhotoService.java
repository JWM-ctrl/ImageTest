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

@AllArgsConstructor
@Service
@Slf4j
public class PhotoService {

    private PhotoDeleteRepository PhotoDeleteRepository;

    private UserRepository userRepository;

    private PhotoRepository photoRepository;

    private UserMapper userMapper;


    public PhotoDTO getPhotoById(long id) {
        log.info("Request to get photo with id: {}", id);
        return photoRepository.findById(id).map(userMapper::photoToPhotoDTO)
                .orElseThrow(()
                        -> new ResourceNotFoundException(String.format("No photo found with id: %s", id)));
    }

    public PhotoDTO createPhoto(PhotoDTO photoDTO, Long userId) {
        log.info("Request to create photo with id: {}", userId);
        Photo photo = userMapper.photoDTOToPhoto(photoDTO);
        User verifiableUser = userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException(String.format("No photo found with userId: %s", userId)));
        if (verifiableUser.getPhoto() != null) {
            throw new ResourceNotFoundException(String.format("The photo already exists: %s", userId));
        }
        photo.setUser(verifiableUser);
        photo = photoRepository.save(photo);
        return userMapper.photoToPhotoDTO(photo);
    }


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

    public void deletePhoto(long id) {
        log.info("Request to delete photo with id: {}", id);
        PhotoDeleteRepository.deleteById(id);
    }
}
