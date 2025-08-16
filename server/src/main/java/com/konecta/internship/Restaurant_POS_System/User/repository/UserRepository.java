package com.konecta.internship.Restaurant_POS_System.User.repository;

import com.konecta.internship.Restaurant_POS_System.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {



    Optional<User> findByEmail(String email);
}
