package com.authservice.request;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthRequestDto {
    private  String username;
    private String password;
}
