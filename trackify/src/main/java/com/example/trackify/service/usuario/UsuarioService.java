package com.example.trackify.service.usuario;

import com.example.trackify.entity.Usuario;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.repository.Usuario.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService {
    @Autowired
    private IUsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundEntityException("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
    }
}
