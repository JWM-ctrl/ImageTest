package com.example.telros.service;

import com.example.telros.dto.PhotoDTO;
import com.example.telros.essence.Photo;
import com.example.telros.exception.ResourceNotFoundException;
import com.example.telros.maper.UserMapper;
import com.example.telros.repository.PhotoRepository;
import com.example.telros.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PhotoService {

    private UserRepository userRepository;

    private PhotoRepository photoRepository;

    private UserMapper userMapper;

//логика возврата по id шнику с выбросом исключения,
// в случае ошибки (если мапер не найдет сущность)
    public PhotoDTO getPhotoById(long id) {
       return photoRepository.findById(id).map(userMapper::photoToPhotoDTO)
                .orElseThrow(()
                        -> new ResourceNotFoundException("No photo found with id " + id));
    }
    //создаем фото
    public PhotoDTO createPhoto(PhotoDTO photoDTO, Long userId) {
        Photo photo = userMapper.photoDTOToPhoto(photoDTO);
        photo.setUser(userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException("No photo found with userId " + userId))) ;
        photo = photoRepository.save(photo);
        return userMapper.photoToPhotoDTO(photo);
    }

    //логика обновления по id шнику с выбросом исключения,
// в случае ошибки
    public PhotoDTO updatePhoto(long id, PhotoDTO photoDTO) {
        Photo photo = photoRepository
                .findById(id).orElseThrow(()
                        -> new ResourceNotFoundException("No photo found with id " + id));
        if(photo != null){
            photo.setPhoto(photoDTO.getPhoto());
            photo = photoRepository.save(photo);
            return userMapper.photoToPhotoDTO(photo);
        }
        throw new ResourceNotFoundException("No photo found with id " + id);
    }
    //удаляем по id
    public void deletePhoto(long id) {
        photoRepository.deleteById(id);

        //аутентификация спринг секюрити (Base Auth)
        //тесты Unit
    }
}
