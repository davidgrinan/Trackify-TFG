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
    public ContenidoDTO crear(Long usuarioId, RequestContenidoDTO dto) {

        logger.info("Creando contenido para usuario {}", usuarioId);

        try {

            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() ->
                            new NotFoundEntityException(
                                    "El usuario con id " +  usuarioId + " no encontrado"
                            ));

            Contenido contenido = contenidoMapper.toEntity(dto);
            contenido.setUsuario(usuario);

            contenido = contenidoRepository.save(contenido);

            logger.info("Contenido creado correctamente con id {}", contenido.getId());

            return contenidoMapper.toDTO(contenido);

        } catch (Exception ex) {

            logger.error("Error creando contenido para usuario {}", usuarioId, ex);

            throw new CreateEntityException(
                    Contenido.class.getSimpleName(),
                    "Usuario ID: " + usuarioId + " | DTO: " + dto,
                    ex
            );
        }
    }

    @Override
    public ContenidoDTO obtenerPorId(Long id) {

        logger.info("Buscando contenido con id {}", id);

        Contenido contenido = contenidoRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundEntityException(
                                "Contenido con id " + id + " no encontrado"
                        ));

        return contenidoMapper.toDTO(contenido);
    }

    @Override
    public List<ContenidoDTO> listarPorUsuario(Long usuarioId) {

        logger.info("Listando contenidos del usuario {}", usuarioId);

        return contenidoMapper.toDTOList(
                contenidoRepository.listarContenidoPorUsuario(usuarioId)
        );
    }

    @Override
    public ContenidoDTO actualizar(Long id, RequestContenidoDTO dto) {

        logger.info("Actualizando contenido con id {}", id);

        try {

            Contenido contenido = contenidoRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException(
                                    "Contenido con id " + id + " no encontrado"
                            ));

            contenidoMapper.updateEntityFromDTO(dto, contenido);

            contenido = contenidoRepository.save(contenido);

            logger.info("Contenido actualizado correctamente con id {}", id);

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
    public void eliminar(Long id) {

        logger.info("Eliminando contenido con id {}", id);

        try {

            Contenido contenido = contenidoRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundEntityException(
                                    "Contenido con id " + id + " no encontrado"
                            ));

            contenidoRepository.delete(contenido);

            logger.info("Contenido eliminado correctamente con id {}", id);

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
    public List<ContenidoDTO> filtrar(Long usuarioId, String tipo, String genero, String estado, Integer valoracion, String titulo) {

        logger.info("Filtrando contenidos del usuario {}", usuarioId);

        return contenidoMapper.toDTOList(
                contenidoRepository.filtrarContenido(
                        usuarioId,
                        tipo,
                        genero,
                        estado,
                        valoracion,
                        titulo
                )
        );
    }
}