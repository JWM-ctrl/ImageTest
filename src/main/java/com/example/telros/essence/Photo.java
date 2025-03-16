package com.example.telros.essence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "photos")
@AllArgsConstructor
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //создаем переменную для хранения больших объектов Large OBject
    @Lob
    private byte[] photo;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "userId")
    private User user;

}
