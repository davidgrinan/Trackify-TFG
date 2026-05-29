package com.example.trackify.service.usuario;

import com.example.trackify.entity.Usuario;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.repository.Usuario.IUsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new NotFoundEntityException("Usuario " + username + " no encontrado")
                );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );


    }
    @Override
    public void cambiarPassword(String username, String nuevaPassword) {
        Usuario usuario = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new NotFoundEntityException("Usuario " + username + " no encontrado")
                );

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        userRepository.save(usuario);
    }
}