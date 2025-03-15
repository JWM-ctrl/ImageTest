package com.example.telros.repository;

import com.example.telros.essence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
//читать чем отличается круд от jpa репозиторий
public interface UserRepository extends JpaRepository<User, Long> {
}
