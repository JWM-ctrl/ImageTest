package com.example.telros.mapper;

import com.example.telros.dto.PhotoDTO;
import com.example.telros.dto.UserDTO;
import com.example.telros.essence.Photo;
import com.example.telros.essence.User;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "birthDate", target = "dateOfBirth")
    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);

    PhotoDTO photoToPhotoDTO(Photo photo);

    @Mapping(target = "user", ignore = true)
    Photo photoDTOToPhoto(PhotoDTO photoDTO);
}
