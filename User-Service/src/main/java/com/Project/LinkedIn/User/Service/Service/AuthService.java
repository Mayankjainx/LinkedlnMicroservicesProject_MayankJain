package com.Project.LinkedIn.User.Service.Service;

import com.Project.LinkedIn.User.Service.DTO.LoginUserDTO;
import com.Project.LinkedIn.User.Service.DTO.SignUpRequestDTO;
import com.Project.LinkedIn.User.Service.DTO.UserDTO;
import com.Project.LinkedIn.User.Service.Entity.User;
import com.Project.LinkedIn.User.Service.Exceptions.BadRequestException;
import com.Project.LinkedIn.User.Service.Exceptions.ResourceNotFoundException;
import com.Project.LinkedIn.User.Service.Repositories.UserRepositories;
import com.Project.LinkedIn.User.Service.Utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepositories userRepositories;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDTO signUp(SignUpRequestDTO signUpRequestDTO) {
        boolean exists=userRepositories.existsByEmail(signUpRequestDTO.getEmail());
        if(exists) throw new BadRequestException("User Already Exists");

        User user=modelMapper.map(signUpRequestDTO,User.class);
        user.setPassword(PasswordUtil.hashPassword(signUpRequestDTO.getPassword()));

        User savedUser=userRepositories.save(user);
        return modelMapper.map(savedUser,UserDTO.class);
    }


    public String login(LoginUserDTO loginUserDTO) {
        User user=userRepositories.findByEmail(loginUserDTO.getEmail()).orElseThrow(()->
                new ResourceNotFoundException("User Not Found with Email"+loginUserDTO.getEmail()));

        boolean isPasswordMatch=PasswordUtil.checkPassword(loginUserDTO.getPassword(),user.getPassword());

        if(!isPasswordMatch){
            throw new BadRequestException("Password Not Matched");
        }

        return jwtService.generateAccessToken(user);


    }
}
