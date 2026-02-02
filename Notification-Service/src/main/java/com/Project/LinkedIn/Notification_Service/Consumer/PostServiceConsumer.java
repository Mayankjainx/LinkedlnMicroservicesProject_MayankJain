package com.Project.LinkedIn.Notification_Service.Consumer;

import com.Project.LinkedIn.Notification_Service.DTO.PersonDTO;
import com.Project.LinkedIn.Notification_Service.Service.SendNotification;
import com.Project.LinkedIn.Notification_Service.clients.ConnectionsClients;
import com.Project.LinkedIn.Post.Service.Event.PostCreatedEvent;
import com.Project.LinkedIn.Post.Service.Event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceConsumer {

    private final ConnectionsClients connectionsClients;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent){
        log.info("Sending notifications: handlePostCreated : {}", postCreatedEvent);
       List<PersonDTO> connections=connectionsClients.getFirstConnections(postCreatedEvent.getCreatorId());

        for(PersonDTO connection: connections){
            sendNotification.send(connection.getUserId(),"Your connection"+postCreatedEvent.getCreatorId()+" created a post , Check it out");

        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLike(PostLikedEvent postLikedEvent){
        log.info("Sending notifications : handlePostLike {}",postLikedEvent);
        String message=String.format("Your post, %d has been liked by %d", postLikedEvent.getPostId(),
                postLikedEvent.getLikedByUserId());

        sendNotification.send(postLikedEvent.getCreatorId(),message);

    }
}
