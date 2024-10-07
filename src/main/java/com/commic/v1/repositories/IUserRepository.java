package com.commic.v1.repositories;

import com.commic.v1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    boolean existsUserById(Integer id);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndOtp(String email, String otp);
    Optional<User> findByOtp(String otp);
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndEmail(String username, String email);
}
