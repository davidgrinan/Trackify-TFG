package com.example.trackify.service.usuario;

import com.example.trackify.entity.Usuario;
import com.example.trackify.exceptions.ErrorGenericoException;
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
    public UserDetails loadUserByUsername(String NombreUsuario) throws UsernameNotFoundException {

        Usuario user = userRepository.findByNombreUsuario(NombreUsuario)
                .orElseThrow(() ->
                        new NotFoundEntityException("Usuario " + NombreUsuario + " no encontrado")
                );


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );


    }
    @Override
    public void cambiarPassword(String NombreUsuario, String nuevaPassword) {
        Usuario usuario = userRepository.findByNombreUsuario(NombreUsuario)
                .orElseThrow(() -> new NotFoundEntityException("Usuario no encontrado"));

        validarPassword(nuevaPassword);

        if (passwordEncoder.matches(nuevaPassword, usuario.getPassword())) {
            throw new ErrorGenericoException("La nueva contraseña no puede ser igual a la actual");
        }

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));

        userRepository.save(usuario);
    }

    public void validarPassword(String password) {

        if (password == null || password.trim().isEmpty()) {
            throw new ErrorGenericoException("La contraseña no puede estar vacía");
        }

        if (password.length() < 8) {
            throw new ErrorGenericoException("La contraseña debe tener al menos 8 caracteres");
        }
    }
}