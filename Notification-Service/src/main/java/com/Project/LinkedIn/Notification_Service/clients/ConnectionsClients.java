package com.Project.LinkedIn.Notification_Service.clients;


import com.Project.LinkedIn.Notification_Service.DTO.PersonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "Connection-Service", path = "/connections")
public interface ConnectionsClients {

    @GetMapping("/core/first-degree")
    List<PersonDTO> getFirstConnections(@RequestHeader("X-User-Id") Long userId);

}
