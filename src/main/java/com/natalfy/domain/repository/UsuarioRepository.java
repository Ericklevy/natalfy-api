package com.natalfy.domain.repository;

import com.natalfy.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // O Spring Security exige que o retorno seja UserDetails na hora do login
    UserDetails findByLogin(String login);
}