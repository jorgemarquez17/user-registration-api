package com.example.userapi.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad Agregada (Aggregate Root) que representa un Usuario.
 * Sigue los principios de DDD - encapsula la lógica de negocio del dominio.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "phones", joinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    private List<Phone> phones = new ArrayList<>();
    
    @Column(columnDefinition = "TEXT")
    private String token;
    
    @Column(nullable = false)
    private LocalDateTime created;
    
    @Column(nullable = false)
    private LocalDateTime modified;
    
    @Column(nullable = false, name = "last_login")
    private LocalDateTime lastLogin;
    
    @Column(nullable = false)
    private Boolean isactive;
    
    /**
     * Metodo de dominio para activar el usuario
     */
    public void activate() {
        this.isactive = true;
    }
    
    /**
     * Metodo de dominio para desactivar el usuario
     */
    public void deactivate() {
        this.isactive = false;
    }
    
    /**
     * Método de dominio para actualizar el último login
     */
    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }
    
    /**
     * Metodo de dominio para actualizar el token
     */
    public void updateToken(String newToken) {
        this.token = newToken;
        this.modified = LocalDateTime.now();
    }
    
    /**
     * Hook del ciclo de vida JPA - se ejecuta antes de persistir
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.created = now;
        this.modified = now;
        this.lastLogin = now;
        if (this.isactive == null) {
            this.isactive = true;
        }
    }
    
    /**
     * Hook del ciclo de vida JPA - se ejecuta antes de actualizar
     */
    @PreUpdate
    protected void onUpdate() {
        this.modified = LocalDateTime.now();
    }
}
