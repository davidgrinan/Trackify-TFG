package com.example.trackify.service.auth;

import com.example.trackify.config.security.JwtTokenProvider;
import com.example.trackify.dto.Usuario.CrearUsuarioDTO;
import com.example.trackify.dto.Usuario.LoginUsuarioDTO;
import com.example.trackify.entity.Role;
import com.example.trackify.entity.Usuario;
import com.example.trackify.exceptions.CreateEntityException;
import com.example.trackify.exceptions.DuplicateEntityException;
import com.example.trackify.exceptions.UnauthorizedException;
import com.example.trackify.mapper.UsuarioMapper;
import com.example.trackify.repository.Role.IRoleRepository;
import com.example.trackify.repository.Usuario.IUsuarioRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

    private final IUsuarioRepository usuarioRepository;
    private final IRoleRepository rolRepository;
    private final UsuarioMapper usuarioMapper;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Override
    public void register(CrearUsuarioDTO dto) {

        logger.info("Registrando usuario {}", dto.getNombreUsuario());

        try {

            if (usuarioRepository.existsByUsername(dto.getNombreUsuario())) {
                throw new DuplicateEntityException("El nombre de usuario ya existe");
            }

            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new DuplicateEntityException("El email ya existe");
            }

            Usuario usuario = usuarioMapper.toUsuarioFromCreateUsuarioDTO(dto);

            usuario.setPassword(
                    passwordEncoder.encode(dto.getPassword())
            );

            usuario.setFechaCreacion(LocalDateTime.now());

            Set<Role> roles = rolRepository.findByNombreIn(dto.getRoles());

            usuario.setRoles(roles);

            usuarioRepository.save(usuario);

            logger.info("Usuario registrado correctamente {}", usuario.getNombreUsuario());

        } catch (Exception ex) {

            logger.error("Error registrando usuario {}", dto.getNombreUsuario(), ex);

            throw new CreateEntityException(
                    Usuario.class.getSimpleName(),
                    dto,
                    ex
            );
        }
    }

    @Override
    public String login(LoginUsuarioDTO dto) {

        logger.info("Intentando login usuario {}", dto.getNombreUsuario());

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    dto.getNombreUsuario(),
                                    dto.getPassword()
                            )
                    );

            String token = jwtTokenProvider.generateToken(authentication);

            logger.info("Login correcto usuario {}", dto.getNombreUsuario());

            return token;

        } catch (Exception ex) {

            logger.error("Error login usuario {}", dto.getNombreUsuario(), ex);

            throw new UnauthorizedException(
                    "Usuario o contraseña incorrectos"
            );
        }
    }
}