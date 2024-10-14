package com.sparta.springtrello.user.service;

import com.sparta.springtrello.user.entity.Role;
import com.sparta.springtrello.user.entity.User;
import com.sparta.springtrello.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;



    public void signUp(String password, String username, String email, String profileimage, Role role) {
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


        user = userRepository.save(user);

        // 카트도 함께 생성
        Cart cart = new Cart(user);
        cart.updateTime();
        cartRepository.save(cart);
    }
}
