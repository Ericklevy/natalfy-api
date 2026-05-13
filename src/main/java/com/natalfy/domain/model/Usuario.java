package com.natalfy.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "tb_usuarios")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String login; // Vai ser o e-mail do funcionário

    @Column(nullable = false)
    private String senha; // Vai ser criptografada no banco!

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // Nível de acesso (ADMIN, AUXILIAR, MEDICO)

    // 👇 Os métodos abaixo são obrigatórios do Spring Security (UserDetails) 👇

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Se for ADMIN, ele tem poder de ADMIN e de USER comum. Se for padrão, só USER.
        if (this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getPassword() { return senha; }

    @Override
    public String getUsername() { return login; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}