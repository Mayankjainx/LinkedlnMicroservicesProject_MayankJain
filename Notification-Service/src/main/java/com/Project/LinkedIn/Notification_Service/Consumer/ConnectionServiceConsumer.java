package com.Project.LinkedIn.Notification_Service.Consumer;

import com.Project.LinkedIn.Notification_Service.Service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import com.Project.LinkedIn.Connection_Service.Event.acceptConnectionRequestEvent;
import com.Project.LinkedIn.Connection_Service.Event.sentConnectionRequestEvent;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceConsumer {

    private final SendNotification sendNotification;

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(sentConnectionRequestEvent sentConnectionRequestEvent){
        log.info("handle connections: handleSentConnectionRequest: {}",sentConnectionRequestEvent);
        String message="you have Received a connection request from user with id: %d"+sentConnectionRequestEvent.getSenderId();

        sendNotification.send(sentConnectionRequestEvent.getReceiverId(),message);
    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(acceptConnectionRequestEvent acceptConnectionRequestEvent){
        log.info("handle connections: handleAcceptConnectionRequest: {}",acceptConnectionRequestEvent);
        String message="your connection request has been accepted by userId: %d"+acceptConnectionRequestEvent.getReceiverId();

        sendNotification.send(acceptConnectionRequestEvent.getSenderId(),message);
    }


}
