package com.sparta.springtrello.domain.user.dto;

import lombok.Data;

@Data
public class DeleteRequestDto {
    private String email;
    private String password;
}
