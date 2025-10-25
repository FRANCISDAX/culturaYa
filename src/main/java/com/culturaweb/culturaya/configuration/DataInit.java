package com.culturaweb.culturaya.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.culturaweb.culturaya.model.entity.Usuario;
import com.culturaweb.culturaya.model.enums.Rol;
import com.culturaweb.culturaya.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Configuration
public class DataInit {

    @Bean
    CommandLineRunner initAdmin(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            boolean existeAdminReal = usuarioRepository.findAll().stream()
                .anyMatch(u -> u.getRol() == Rol.ADMIN && !u.getEmail().equals("admin@admin.com"));

            if (!existeAdminReal) {
                if (!usuarioRepository.findByEmail("admin@admin.com").isPresent()) {
                    Usuario adminPorDefecto = Usuario.builder()
                            .nombre("Administrador")
                            .email("admin@admin.com")
                            .password(passwordEncoder.encode("admin"))
                            .rol(Rol.ADMIN)
                            .build();
                    usuarioRepository.save(adminPorDefecto);
                }
            } else {
                eliminarAdminPorDefecto(usuarioRepository);
            }        
        };
    }

    @Transactional
    public void eliminarAdminPorDefecto(UsuarioRepository usuarioRepository) {
        usuarioRepository.findByEmail("admin@mitienda.com")
            .ifPresent(admin -> {
                if (admin.getRol() == Rol.ADMIN) {
                        usuarioRepository.delete(admin);
                    }
                }
            );
    }

}
