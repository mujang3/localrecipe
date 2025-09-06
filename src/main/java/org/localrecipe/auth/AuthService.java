package org.localrecipe.auth;

import org.localrecipe.user.User;
import org.localrecipe.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    public User signup(String username, String nickname, String rawPassword) {
        if (userRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        String hashed = encoder.encode(rawPassword);
        return userRepo.save(new User(username.trim(), nickname.trim(), hashed));
    }
}