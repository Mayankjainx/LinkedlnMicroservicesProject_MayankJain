package com.Project.LinkedIn.Service.Service;


import com.Project.LinkedIn.Service.Entity.Post;
import com.Project.LinkedIn.Service.Entity.PostLike;
import com.Project.LinkedIn.Service.Event.PostLikedEvent;
import com.Project.LinkedIn.Service.Exceptions.BadRequestException;
import com.Project.LinkedIn.Service.Exceptions.ResourceNotFoundException;
import com.Project.LinkedIn.Service.Repositories.PostLikeRepositories;
import com.Project.LinkedIn.Service.Repositories.PostRepositories;
import com.Project.LinkedIn.Service.auth.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeService {

    private final PostRepositories postRepositories;
    private final PostLikeRepositories postLikeRepositories;
    private final KafkaTemplate<Long , PostLikedEvent> kafkaTemplate;

    public void likePost(Long postId) {
        Long userId= UserContextHolder.getCurrentUserId();
        log.info("Attempting to Like the Post with id {}",postId);

        Post post=postRepositories.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post Not Found With Id :",postId));

        boolean alreadyLiked=postLikeRepositories.existsByUserIdAndPostId(userId,postId);
        if(alreadyLiked) throw new BadRequestException("Cannot Like Same Post Again");

        PostLike postLike=new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepositories.save(postLike);
        log.info("Liked the Post with Id {}",postId);

        PostLikedEvent postLikedEvent= PostLikedEvent.builder()
                .likedByUserId(userId)
                .postId(postId)
                .creatorId(post.getUserId())
                .build();

        kafkaTemplate.send("post-liked-topic",postId,postLikedEvent);


    }

    public void unlikePost(Long postId) {
        Long userId= UserContextHolder.getCurrentUserId();
        log.info("Attempting to Unlike the Post with id {}",postId);
        boolean exists=postRepositories.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post Not Found With Id",postId);

        boolean alreadyLiked=postLikeRepositories.existsByUserIdAndPostId(userId,postId);
        if(!alreadyLiked) throw new BadRequestException("Post is Not Liked");

        postLikeRepositories.deleteByUserIdAndPostId(userId,postId);
        log.info("Unliked the Post with Id {}",postId);

    }
}
