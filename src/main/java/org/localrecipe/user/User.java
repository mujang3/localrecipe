package org.localrecipe.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=80)
    private String username;

    @Column(nullable=false, length=80)
    private String nickname;

    @Column(nullable=false, length=100)
    private String password;

    @Column(nullable=false, length=120)
    private String roles = "ROLE_USER";

    protected User() {} // JPA
    public User(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
    }
    // getters/setters 생략해도 되지만 필요시 생성
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getNickname() { return nickname; }
    public String getPassword() { return password; }
    public String getRoles() { return roles; }
}