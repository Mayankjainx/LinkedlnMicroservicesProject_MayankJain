package com.Project.LinkedIn.User.Service.Controller;

import com.Project.LinkedIn.User.Service.DTO.LoginUserDTO;
import com.Project.LinkedIn.User.Service.DTO.SignUpRequestDTO;
import com.Project.LinkedIn.User.Service.DTO.UserDTO;
import com.Project.LinkedIn.User.Service.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody SignUpRequestDTO  signUpRequestDTO){
        UserDTO userDTO=authService.signUp(signUpRequestDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginUserDTO loginUserDTO){
        String token=authService.login(loginUserDTO);
        return ResponseEntity.ok(token);
    }
}
