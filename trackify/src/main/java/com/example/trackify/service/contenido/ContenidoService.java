package com.example.trackify.service.contenido;

import com.example.trackify.dto.Contenido.ContenidoDTO;
import com.example.trackify.dto.Contenido.RequestContenidoDTO;
import com.example.trackify.entity.*;
import com.example.trackify.exceptions.*;
import com.example.trackify.mapper.ContenidoMapper;
import com.example.trackify.repository.Contenido.IContenidoRepository;
import com.example.trackify.repository.Estado.IEstadoRepository;
import com.example.trackify.repository.Genero.IGeneroRepository;
import com.example.trackify.repository.Tipo.ITipoRepository;
import com.example.trackify.repository.Usuario.IUsuarioRepository;
import lombok.AllArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContenidoService implements IContenidoService {

    private final IContenidoRepository contenidoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IGeneroRepository generoRepository;
    private final ITipoRepository tipoRepository;
    private final IEstadoRepository estadoRepository;
    private final ContenidoMapper contenidoMapper;

    private final Logger logger = LoggerFactory.getLogger(ContenidoService.class);

    @Override
    public ContenidoDTO crear(String username, RequestContenidoDTO dto) {
        try {
            Usuario usuario = obtenerUsuarioPorUsername(username);

            Contenido contenido = contenidoMapper.toEntity(dto);
            contenido.setUsuario(usuario);
            asignarRelaciones(contenido, dto);

            contenido = contenidoRepository.save(contenido);

            return contenidoMapper.toDTO(contenido);

        } catch (Exception ex) {
            throw new CreateEntityException(
                    Contenido.class.getSimpleName(),
                    "Usuario: " + username + " | DTO: " + dto,
                    ex
            );
        }
    }

    @Override
    public ContenidoDTO actualizar(String username, Long id, RequestContenidoDTO dto) {
        try {
            Usuario usuario = obtenerUsuarioPorUsername(username);

            Contenido contenido = contenidoRepository.findById(id)
                    .orElseThrow(() -> new NotFoundEntityException("Contenido con id " + id + " no encontrado"));

            validarPropietario(contenido, usuario);

            contenidoMapper.updateEntityFromDTO(dto, contenido);
            asignarRelaciones(contenido, dto);

            contenido = contenidoRepository.save(contenido);

            return contenidoMapper.toDTO(contenido);

        } catch (Exception ex) {
            throw new UpdateEntityException(
                    Contenido.class.getSimpleName(),
                    "Contenido ID: " + id + " | DTO: " + dto,
                    ex
            );
        }
    }

    @Override
    public ContenidoDTO obtenerPorId(String username, Long id) {
        Usuario usuario = obtenerUsuarioPorUsername(username);

        Contenido contenido = contenidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException("Contenido con id " + id + " no encontrado"));

        validarPropietario(contenido, usuario);

        return contenidoMapper.toDTO(contenido);
    }

    @Override
    public List<ContenidoDTO> listarPorUsuario(String username) {
        Usuario usuario = obtenerUsuarioPorUsername(username);

        return contenidoMapper.toDTOList(
                contenidoRepository.listarContenidoPorUsuario(usuario.getCodigo())
        );
    }

    @Override
    public void eliminar(String username, Long id) {
        try {
            Usuario usuario = obtenerUsuarioPorUsername(username);

            Contenido contenido = contenidoRepository.findById(id)
                    .orElseThrow(() -> new NotFoundEntityException("Contenido con id " + id + " no encontrado"));

            validarPropietario(contenido, usuario);

            contenidoRepository.delete(contenido);

        } catch (Exception ex) {
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

    private void asignarRelaciones(Contenido contenido, RequestContenidoDTO dto) {
        Genero genero = generoRepository.findByNombre(dto.getGenero())
                .orElseThrow(() -> new NotFoundEntityException("Género " + dto.getGenero() + " no encontrado"));

        Tipo tipo = tipoRepository.findByNombre(dto.getTipo())
                .orElseThrow(() -> new NotFoundEntityException("Tipo " + dto.getTipo() + " no encontrado"));

        Estado estado = estadoRepository.findByNombre(dto.getEstado())
                .orElseThrow(() -> new NotFoundEntityException("Estado " + dto.getEstado() + " no encontrado"));

        contenido.setGenero(genero);
        contenido.setTipo(tipo);
        contenido.setEstado(estado);
    }

    private Usuario obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundEntityException("Usuario " + username + " no encontrado"));
    }

    private void validarPropietario(Contenido contenido, Usuario usuario) {
        if (!contenido.getUsuario().getCodigo().equals(usuario.getCodigo())) {
            throw new NotFoundEntityException("Contenido no encontrado para este usuario");
        }
    }
}