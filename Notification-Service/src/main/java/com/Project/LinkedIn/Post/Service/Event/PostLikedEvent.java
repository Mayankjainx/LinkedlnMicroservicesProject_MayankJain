package com.Project.LinkedIn.Post.Service.Event;

import lombok.Builder;
import lombok.Data;

@Data
public class PostLikedEvent {
     Long creatorId;
     Long postId;
     Long likedByUserId;
}
