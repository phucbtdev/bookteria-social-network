package com.recruitment.identity.entity;

import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "username", unique = true)
    String username;
    String password;

    @Column(name = "email", unique = true)
    String email;

    @Column(name = "email_verified", nullable = false)
    boolean emailVerified;

    @ManyToMany
    Set<Roles> roles;
}
