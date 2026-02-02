package com.Project.LinkedIn.Service.Repositories;


import com.Project.LinkedIn.Service.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepositories extends JpaRepository<Post,Long> {

    List<Post> findByUserId(Long userId);
}
