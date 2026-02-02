package com.Project.LinkedIn.Service.Repositories;

import com.Project.LinkedIn.Service.Entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PostLikeRepositories extends JpaRepository<PostLike,Long> {
    boolean existsByUserIdAndPostId(Long userId,Long PostId);

    @Transactional
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
