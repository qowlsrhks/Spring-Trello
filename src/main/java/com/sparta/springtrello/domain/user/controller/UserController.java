package com.sparta.springtrello.domain.user.controller;

import com.sparta.springtrello.domain.user.config.JwtUtil;
import com.sparta.springtrello.domain.user.dto.DeleteRequestDto;
import com.sparta.springtrello.domain.user.dto.LoginRequestDto;
import com.sparta.springtrello.domain.user.dto.SignupRequestDto;
import com.sparta.springtrello.domain.user.entity.Role;
import com.sparta.springtrello.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
// @Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public String signUp(@RequestBody SignupRequestDto signupRequestDto) {
        Role userRole = Role.valueOf(signupRequestDto.getRole().toUpperCase());
        userService.signUp(signupRequestDto.getPassword(), signupRequestDto.getUsername(), signupRequestDto.getEmail() , userRole);
        return "회원가입 성공";
    }

    @DeleteMapping("/delete")
    public String deleteAccount(@RequestBody DeleteRequestDto deleteRequestDto) {
        userService.deleteAccount(deleteRequestDto.getEmail(), deleteRequestDto.getPassword());
        return "회원탈퇴 성공";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String token = userService.login(loginRequestDto, response);
        jwtUtil.addJwtToCookie(token, response);
        return token;
    }
}
