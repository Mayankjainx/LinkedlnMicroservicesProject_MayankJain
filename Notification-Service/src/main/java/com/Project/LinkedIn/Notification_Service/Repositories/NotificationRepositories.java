package com.Project.LinkedIn.Notification_Service.Repositories;

import com.Project.LinkedIn.Notification_Service.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepositories extends JpaRepository<Notification,Long> {
}
