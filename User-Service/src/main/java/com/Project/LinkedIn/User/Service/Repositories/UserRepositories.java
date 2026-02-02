package com.Project.LinkedIn.User.Service.Repositories;

import com.Project.LinkedIn.User.Service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositories extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);
}
