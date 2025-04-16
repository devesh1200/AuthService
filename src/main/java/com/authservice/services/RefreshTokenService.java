package com.authservice.services;

import com.authservice.entities.RefreshToken;
import com.authservice.entities.UserInfo;
import com.authservice.repository.RefreshTokenRespository;
import com.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private RefreshTokenRespository refreshTokenRespository;

    private UserRepository userRepository;
    public  RefreshTokenService(RefreshTokenRespository refreshTokenRespository,UserRepository userRepository){
        this.refreshTokenRespository=refreshTokenRespository;
        this.userRepository=userRepository;
    }

    public RefreshToken createRefreshToken(String userName) {
        UserInfo userInfoExtracted = userRepository.findByUsername(userName);
        RefreshToken refreshToken = RefreshToken.builder().
                token(UUID.randomUUID().toString()).
                expiryDate(Instant.now().plusMillis(600000)).
                userInfo(userInfoExtracted).build();
        return refreshTokenRespository.save(refreshToken);

    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRespository.delete(token);
            throw new RuntimeException("Token has expired.Please make A  new Login ");
        } else {
            return token;
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRespository.findByToken(token);
    }

}
