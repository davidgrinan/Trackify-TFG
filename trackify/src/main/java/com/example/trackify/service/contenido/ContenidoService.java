package com.example.trackify.service.contenido;

import com.example.trackify.dto.Contenido.ContenidoDTO;
import com.example.trackify.dto.Contenido.RequestContenidoDTO;
import com.example.trackify.entity.Contenido;
import com.example.trackify.entity.Usuario;
import com.example.trackify.exceptions.CreateEntityException;
import com.example.trackify.exceptions.DeleteEntityException;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.exceptions.UpdateEntityException;
import com.example.trackify.mapper.ContenidoMapper;
import com.example.trackify.repository.Contenido.IContenidoRepository;
import com.example.trackify.repository.Usuario.IUsuarioRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContenidoService implements IContenidoService {

    private final IContenidoRepository contenidoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ContenidoMapper contenidoMapper;

    private final Logger logger = LoggerFactory.getLogger(ContenidoService.class);

    @Override
    public ContenidoDTO crear(String username, RequestContenidoDTO dto) {

        logger.info("Creando contenido para usuario {}", username);

        try {
            Usuario usuario = obtenerUsuarioPorUsername(username);

            Contenido contenido = contenidoMapper.toEntity(dto);
            contenido.setUsuario(usuario);

            contenido = contenidoRepository.save(contenido);

            return contenidoMapper.toDTO(contenido);

        } catch (Exception ex) {
            logger.error("Error creando contenido para usuario {}", username, ex);

            throw new CreateEntityException(
                    Contenido.class.getSimpleName(),
                    "Usuario: " + username + " | DTO: " + dto,
                    ex
            );
        }
    }

    @Override
    public ContenidoDTO obtenerPorId(String username, Long id) {

        logger.info("Buscando contenido {} del usuario {}", id, username);

        Usuario usuario = obtenerUsuarioPorUsername(username);

        Contenido contenido = contenidoRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundEntityException("Contenido con id " + id + " no encontrado")
                );

        validarPropietario(contenido, usuario);

        return contenidoMapper.toDTO(contenido);
    }

    @Override
    public List<ContenidoDTO> listarPorUsuario(String username) {

        logger.info("Listando contenidos del usuario {}", username);

        Usuario usuario = obtenerUsuarioPorUsername(username);

        return contenidoMapper.toDTOList(
                contenidoRepository.listarContenidoPorUsuario(usuario.getCodigo())
        );
    }

    @Override
    public ContenidoDTO actualizar(String username, Long id, RequestContenidoDTO dto) {

        logger.info("Actualizando contenido {} del usuario {}", id, username);

        try {
            Usuario usuario = obtenerUsuarioPorUsername(username);

            Contenido contenido = contenidoRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException("Contenido con id " + id + " no encontrado")
                    );

            validarPropietario(contenido, usuario);

            contenidoMapper.updateEntityFromDTO(dto, contenido);

            contenido = contenidoRepository.save(contenido);

            return contenidoMapper.toDTO(contenido);

        } catch (Exception ex) {
            logger.error("Error actualizando contenido con id {}", id, ex);

            throw new UpdateEntityException(
                    Contenido.class.getSimpleName(),
                    "Contenido ID: " + id + " | DTO: " + dto,
                    ex
            );
        }
    }

    @Override
    public void eliminar(String username, Long id) {

        logger.info("Eliminando contenido {} del usuario {}", id, username);

        try {
            Usuario usuario = obtenerUsuarioPorUsername(username);

            Contenido contenido = contenidoRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException("Contenido con id " + id + " no encontrado")
                    );

            validarPropietario(contenido, usuario);

            contenidoRepository.delete(contenido);

        } catch (Exception ex) {
            logger.error("Error eliminando contenido con id {}", id, ex);

            throw new DeleteEntityException(
                    Contenido.class.getSimpleName(),
                    "Contenido ID: " + id,
                    ex
            );
        }
    }

    @Override
    public List<ContenidoDTO> filtrar(String username,
                                      String tipo,
                                      String genero,
                                      String estado,
                                      Integer valoracion,
                                      String titulo) {

        logger.info("Filtrando contenidos del usuario {}", username);

        Usuario usuario = obtenerUsuarioPorUsername(username);

        return contenidoMapper.toDTOList(
                contenidoRepository.filtrarContenido(
                        usuario.getCodigo(),
                        tipo,
                        genero,
                        estado,
                        valoracion,
                        titulo
                )
        );
    }

    private Usuario obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new NotFoundEntityException("Usuario " + username + " no encontrado")
                );
    }

    private void validarPropietario(Contenido contenido, Usuario usuario) {
        if (!contenido.getUsuario().getCodigo().equals(usuario.getCodigo())) {
            throw new NotFoundEntityException("Contenido no encontrado para este usuario");
        }
    }
}