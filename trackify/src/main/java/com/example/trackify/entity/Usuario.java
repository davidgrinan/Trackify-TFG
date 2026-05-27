package com.example.trackify.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(unique = true, nullable = false)
    private String nombreUsuario;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_codigo"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<Contenido> contenidos = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRol().name())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return nombreUsuario;
    }
}
