package org.localrecipe.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.localrecipe.user.User;
import org.localrecipe.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtProvider jwt;

    public AuthController(AuthService authService, UserRepository userRepo,
                          PasswordEncoder encoder, JwtProvider jwt) {
        this.authService = authService;
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    // DTO (내부 클래스로 간단히)
    public static class SignUpRequest {
        @NotBlank @Size(min=4, max=30) public String username;
        @NotBlank @Size(min=2, max=30) public String nickname;
        @NotBlank @Size(min=8, max=64) public String password;
        @NotBlank public String passwordConfirm;
    }
    public static class LoginRequest {
        @NotBlank public String username;
        @NotBlank public String password;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest req) {
        if (!req.password.equals(req.passwordConfirm)) {
            throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        }
        User u = authService.signup(req.username, req.nickname, req.password);
        return ResponseEntity.ok().body(
                java.util.Map.of("id", u.getId(), "username", u.getUsername(), "nickname", u.getNickname())
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        User user = userRepo.findByUsername(req.username);
        if (user == null || !encoder.matches(req.password, user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        String token = jwt.createAccessToken(user.getUsername(), user.getRoles());
        return ResponseEntity.ok(java.util.Map.of("accessToken", token, "tokenType", "Bearer"));
    }

    // 토큰 검증 테스트용
    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestAttribute(name="username", required=false) String username) {
        return ResponseEntity.ok(java.util.Map.of("username", username));
    }
}