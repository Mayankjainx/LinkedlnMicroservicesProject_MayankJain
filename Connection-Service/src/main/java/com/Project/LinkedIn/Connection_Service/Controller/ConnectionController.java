package com.Project.LinkedIn.Connection_Service.Controller;

import com.Project.LinkedIn.Connection_Service.Entity.Person;
import com.Project.LinkedIn.Connection_Service.Service.ConnectionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
public class ConnectionController {

    private final ConnectionService connectionService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getFirstConnections(){
        return ResponseEntity.ok(connectionService.getFirstDegreeConnection());
    }

    @GetMapping("/{userId}/second-degree")
    public ResponseEntity<List<Person>> getSecondConnections(@PathVariable Long userId){
        return ResponseEntity.ok(connectionService.getSecondDegreeConnection(userId));
    }

    @GetMapping("/{userId}/third-degree")
    public ResponseEntity<List<Person>> getThirdConnections(@PathVariable Long userId){
        return ResponseEntity.ok(connectionService.getThirdDegreeConnection(userId));
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<Boolean> sendConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(connectionService.sentConnectionRequest(userId));
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(connectionService.acceptConnectionRequest(userId));
    }

    @PostMapping("/reject/{userID}")
    public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(connectionService.rejectConnectionRequest(userId));
    }

}
