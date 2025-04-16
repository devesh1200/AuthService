package com.authservice.controller;

import com.authservice.entities.RefreshToken;
import com.authservice.model.UserInfoDto;
import com.authservice.response.JwtResponseDto;
import com.authservice.services.JwtService;
import com.authservice.services.RefreshTokenService;
import com.authservice.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
@Autowired
    private  JwtService jwtService;
   @Autowired
    private  RefreshTokenService refreshTokenService;

  @Autowired
  private UserDetailsServiceImpl userDetailsServiceImpl;


    @PostMapping("auth/v1/signup")
    public ResponseEntity  SignUp(@RequestBody UserInfoDto userInfoDto) {
        try {Boolean isSignUped = userDetailsServiceImpl.signupUser(userInfoDto);
            if (Boolean.FALSE.equals(isSignUped)) {
                return new ResponseEntity<>("Already Exist", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.generateToken(userInfoDto.getUsername());//JWT TOKEN
            return new ResponseEntity<>(JwtResponseDto.builder().accessToken(jwtToken).
                    token(refreshToken.getToken()).build(), HttpStatus.OK);
        }catch (Exception ex) {
            ex.printStackTrace(); // Print the exception in logs
            return new ResponseEntity<>("Exception in User Service: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
