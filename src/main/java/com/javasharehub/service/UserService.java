package com.javasharehub.service;

import com.javasharehub.entity.User;
import com.javasharehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(String email, String name, String password) {
        log.info("Регистрация пользователя: {}", email);
        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Email уже занят: {}", email);
            throw new RuntimeException("Email уже используется");
        }
        User user = User.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(password))
                .role("ROLE_USER")
                .build();
        userRepository.save(user);
        log.info("Пользователь зарегистрирован: {}", email);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public long countUsers() {
        return userRepository.count();
    }
}