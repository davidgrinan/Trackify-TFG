package com.example.trackify.service.auth;

import com.example.trackify.Enum.RoleType;
import com.example.trackify.config.security.JwtTokenProvider;
import com.example.trackify.dto.Usuario.CrearUsuarioDTO;
import com.example.trackify.dto.Usuario.LoginUsuarioDTO;
import com.example.trackify.entity.Role;
import com.example.trackify.entity.Usuario;
import com.example.trackify.exceptions.CreateEntityException;
import com.example.trackify.exceptions.DuplicateEntityException;
import com.example.trackify.exceptions.UnauthorizedException;
import com.example.trackify.mapper.UsuarioMapper;
import com.example.trackify.repository.Usuario.IUsuarioRepository;
import com.example.trackify.service.role.IRoleService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

    private final IUsuarioRepository usuarioRepository;
    private final IRoleService roleService;
    private final UsuarioMapper usuarioMapper;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Override
    public void register(CrearUsuarioDTO dto) {
        try {
            if (usuarioRepository.existsByNombreUsuario(dto.getNombreUsuario())) {
                throw new DuplicateEntityException("El nombre de usuario ya existe");
            }

            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new DuplicateEntityException("El email ya existe");
            }

            Usuario usuario = usuarioMapper.toUsuarioFromCreateUsuarioDTO(dto);

            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            usuario.setFechaCreacion(LocalDateTime.now());

            Role roleUser = roleService.obtenerPorRol(RoleType.USER);
            usuario.setRoles(Set.of(roleUser));

            usuarioRepository.save(usuario);

            logger.info("Usuario registrado correctamente {}", usuario.getNombreUsuario());

        } catch (DuplicateEntityException ex) {
            throw ex;

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

            return jwtTokenProvider.generateToken(authentication);

        } catch (Exception ex) {

            logger.error("Error login usuario {}", dto.getNombreUsuario(), ex);

            throw new UnauthorizedException("Usuario o contraseña incorrectos");
        }
    }
}