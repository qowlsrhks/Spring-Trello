package com.sparta.springtrello.user.dto;

import lombok.Data;

@Data
public class DeleteRequestDto {
    private String email;
    private String password;
}
