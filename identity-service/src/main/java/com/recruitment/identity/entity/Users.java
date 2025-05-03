package com.recruitment.identity.entity;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

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
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    UUID id;

    @Column(name = "email", unique = true)
    String email;
    String password;

    @Column(name = "email_verified", nullable = false)
    boolean emailVerified;

    @Builder.Default
    boolean active = false;

    @ManyToMany
    Set<Roles> roles;
}
