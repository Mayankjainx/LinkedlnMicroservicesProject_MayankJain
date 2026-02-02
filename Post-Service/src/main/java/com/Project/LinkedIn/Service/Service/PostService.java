package com.Project.LinkedIn.Service.Service;

import com.Project.LinkedIn.Service.DTO.PostCreateRequestDTO;
import com.Project.LinkedIn.Service.DTO.PostDTO;
import com.Project.LinkedIn.Service.Entity.Post;
import com.Project.LinkedIn.Service.Event.PostCreatedEvent;
import com.Project.LinkedIn.Service.Exceptions.ResourceNotFoundException;
import com.Project.LinkedIn.Service.Repositories.PostRepositories;
import com.Project.LinkedIn.Service.auth.UserContextHolder;
import com.Project.LinkedIn.Service.clients.ConnectionsClients;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepositories postRepositories;
    private final ModelMapper modelMapper;
    private final ConnectionsClients connectionClients;

    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    public PostDTO createPost(PostCreateRequestDTO postDTO) {
        Long userId=UserContextHolder.getCurrentUserId();
        Post post=modelMapper.map(postDTO,Post.class);
        post.setUserId(userId);

        Post savedPost=postRepositories.save(post);

        PostCreatedEvent postCreatedEvent= PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .creatorId(userId)
                .content(savedPost.getContent())
                .build();

        kafkaTemplate.send("post-created-topic",postCreatedEvent);

        return modelMapper.map(savedPost,PostDTO.class);
    }

    public PostDTO getPostByID(Long postId) {
        log.debug("Retrieving Post with ID : {}",postId);


        Post post= postRepositories.findById(postId).orElseThrow(()->
                new ResourceNotFoundException("Post ot Found With Id",postId));

        return modelMapper.map(post,PostDTO.class);
    }

    public List<PostDTO> getAllPosts(Long userId) {
        List<Post> posts= postRepositories.findByUserId(userId);
        return posts.stream()
                .map((element)->modelMapper.map(element,PostDTO.class))
                .collect(Collectors.toList());
    }
}
