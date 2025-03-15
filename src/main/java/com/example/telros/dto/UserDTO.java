package com.example.telros.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
//добавить валидацию?
//сделать класс DTO record? позволяет создать неизменяемый класс с final полями (мы не можем изменить поля после создания объекта), а также автоматически сгенерированными hashcode()&equals() + toString() + get() методами.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
}
