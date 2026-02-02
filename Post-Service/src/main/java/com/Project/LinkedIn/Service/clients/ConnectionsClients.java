package com.Project.LinkedIn.Service.clients;

import com.Project.LinkedIn.Service.DTO.PersonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "Connection-Service", path = "/connections")
public interface ConnectionsClients {

    @GetMapping("/core/first-degree")
    List<PersonDTO> getFirstConnections();
}
