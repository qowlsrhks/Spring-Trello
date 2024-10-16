package com.sparta.springtrello.user.service;

import com.sparta.springtrello.user.config.JwtUtil;
import com.sparta.springtrello.user.config.PasswordEncoder;
import com.sparta.springtrello.user.dto.LoginRequestDto;
import com.sparta.springtrello.user.entity.Role;
import com.sparta.springtrello.user.entity.User;
import com.sparta.springtrello.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signUp(String password, String username, String email, Role role) {
        if (userRepository.findByEmail(email).isPresent()) { // 이메일로 중복 체크
            throw new RuntimeException("중복된 사용자 아이디입니다.");
        }

        if (!isValidEmail(email) || !isValidPassword(password)) {
            throw new RuntimeException("이메일 또는 비밀번호 형식이 올바르지 않습니다.");
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setEmail(email);
        user.setUsername(username);
        user.setSignupTime(LocalDateTime.now());
        userRepository.save(user);

    }

    public void deleteAccount(String email, String password) {
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (user.isDeleted()) {
            throw new RuntimeException("이미 탈퇴한 사용자입니다.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        user.setDeleted(true); // 탈퇴 처리

        userRepository.save(user);
    }

    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        User user = userRepository.findByEmailAndIsDeletedFalse(loginRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        String token = jwtUtil.createToken(user.getUser_id(), user.getEmail(), user.getRole());
        jwtUtil.addJwtToCookie(token, response);
        return token;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return Pattern.matches(passwordRegex, password);
    }
}
