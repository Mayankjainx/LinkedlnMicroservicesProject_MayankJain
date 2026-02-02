package com.Project.LinkedIn.Connection_Service.Service;

import com.Project.LinkedIn.Connection_Service.Entity.Person;
import com.Project.LinkedIn.Connection_Service.Repository.ConnectionRepositories;
import com.Project.LinkedIn.Connection_Service.Event.acceptConnectionRequestEvent;
import com.Project.LinkedIn.Connection_Service.Event.sentConnectionRequestEvent;
import com.Project.LinkedIn.Connection_Service.auth.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {

    private final ConnectionRepositories connectionRepositories;
    private final KafkaTemplate<Long,sentConnectionRequestEvent> sendRequestKafkaTemplate;
    private final KafkaTemplate<Long,acceptConnectionRequestEvent> acceptRequestKafkaTemplate;

    public List<Person> getFirstDegreeConnection(){
        Long userId= UserContextHolder.getCurrentUserId();
        log.info("Getting First Degree Connection with userId :{}",userId);
        return connectionRepositories.getFirstDegreeConnection(userId);
    }

    public List<Person> getSecondDegreeConnection(Long userId){
        log.info("Getting Second Degree Connection with userId :{}",userId);
        return connectionRepositories.getSecondDegreeConnection(userId);
    }

    public List<Person> getThirdDegreeConnection(Long userId){
        log.info("Getting Third Degree Connection with userId :{}",userId);
        return connectionRepositories.getThirdDegreeConnection(userId);
    }

    public Boolean sentConnectionRequest(Long receiverId) {
        Long senderId=UserContextHolder.getCurrentUserId();
        log.info("Trying to send connection Request,sender:{},receiver:{}",senderId,receiverId);

        if(senderId.equals(receiverId)){
            throw new RuntimeException("Both sender and receiver are the same");
        }

        boolean alreadySendRequest=connectionRepositories.connectionRequestExists(senderId,receiverId);
        if(alreadySendRequest){
            throw new RuntimeException("Connection Request Already Send");
        }

        boolean alreadyConnected=connectionRepositories.alreadyConnected(senderId,receiverId);
        if(alreadyConnected){
            throw new RuntimeException("Already Connected User, Cannot add Connection Request");
        }

        log.info("Successfully Sent the connection request");
        connectionRepositories.addConnectionRequest(senderId,receiverId);

        sentConnectionRequestEvent ConnectionRequestEvent=sentConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        sendRequestKafkaTemplate.send("sent-connection-request-topic",ConnectionRequestEvent);

        return true;
    }

    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId=UserContextHolder.getCurrentUserId();


        boolean ConnectionRequestExists=connectionRepositories.connectionRequestExists(senderId,receiverId);
        if(!ConnectionRequestExists){
            throw new RuntimeException("No Connection Request to Accept");
        }

        log.info("Successfully Accepted the Connection Request");
        connectionRepositories.AcceptRequest(senderId,receiverId);

        acceptConnectionRequestEvent AcceptConnectionRequestEvent=acceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        acceptRequestKafkaTemplate.send("accept-connection-request-topic",AcceptConnectionRequestEvent);

        return true;
    }

    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId=UserContextHolder.getCurrentUserId();

        if(senderId.equals(receiverId)){
            throw new RuntimeException("Both sender and receiver are the same");
        }

        boolean ConnectionRequestExists=connectionRepositories.connectionRequestExists(senderId,receiverId);
        if(!ConnectionRequestExists){
            throw new RuntimeException("No Connection Request to Reject");
        }

        log.info("Successfully Rejected the Connection Request");
        connectionRepositories.RejectConnectionRequest(senderId,receiverId);
        return true;

    }
}
