package com.Project.LinkedIn.Notification_Service.Service;

import com.Project.LinkedIn.Notification_Service.Entity.Notification;
import com.Project.LinkedIn.Notification_Service.Repositories.NotificationRepositories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendNotification {

    private final NotificationRepositories notificationRepositories;

    public void send(Long userId, String message){
        Notification notification=new Notification();
        notification.setMessage(message);
        notification.setUserId(userId);

        notificationRepositories.save(notification);
        log.info("Notification saved for user: {}",userId);

    }
}
