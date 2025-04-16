package com.authservice.services;

import com.authservice.entities.UserInfo;
import com.authservice.eventProducer.UserInfoEvent;
import com.authservice.eventProducer.UserInfoProducer;
import com.authservice.model.UserInfoDto;
import com.authservice.repository.UserRepository;
import com.authservice.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserInfoProducer userInfoProducer;

    /**
     * public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
     * this.userRepository = userRepository;
     * this.passwordEncoder = passwordEncoder;
     * <p>
     * }
     **/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto) {
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public boolean signupUser(UserInfoDto userInfoDto) {
        if (!ValidationUtil.isValidEmail(userInfoDto)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!ValidationUtil.isValidPassword(userInfoDto)) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and include uppercase, lowercase, number, and special character.");
        }

        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));

        if (Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))) {
            return false;
        }

        String userId = UUID.randomUUID().toString();
        userRepository.save(
                UserInfo.builder()
                        .userId(userId)
                        .username(userInfoDto.getUsername())
                        .password(userInfoDto.getPassword())
                        .roles(new HashSet<>())
                        .build()
        );
        userInfoProducer.sendEventToKafka(publishUserEventToKafka(userInfoDto, userId));
        return true;
    }

    private UserInfoEvent publishUserEventToKafka(UserInfoDto userInfoDto, String userId) {
        return UserInfoEvent.builder()
                .userId(userId)
                .firstName(userInfoDto.getUsername())
                .lastName(userInfoDto.getLastname())
                .phoneNumber(userInfoDto.getPhonenumber())
                .email(userInfoDto.getEmail())
                .build();

    }

}

