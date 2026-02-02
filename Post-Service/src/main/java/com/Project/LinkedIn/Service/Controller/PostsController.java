package com.Project.LinkedIn.Service.Controller;

import com.Project.LinkedIn.Service.DTO.PostCreateRequestDTO;
import com.Project.LinkedIn.Service.DTO.PostDTO;
import com.Project.LinkedIn.Service.Service.PostService;
import com.Project.LinkedIn.Service.auth.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostsController {

    private final PostService postService;


    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostCreateRequestDTO postDTO){
        PostDTO createdPost=postService.createPost(postDTO);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId){
        Long userId= UserContextHolder.getCurrentUserId();
        PostDTO postDTO=postService.getPostByID(postId);
        return ResponseEntity.ok(postDTO);
    }

    @GetMapping("/users/{UserId}/allPosts")
    public ResponseEntity<List<PostDTO>> getAllPosts(@PathVariable Long UserId){
        List<PostDTO> posts=postService.getAllPosts(UserId);
        return ResponseEntity.ok(posts);
    }
}
