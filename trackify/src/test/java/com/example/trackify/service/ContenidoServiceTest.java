package com.example.trackify.service;

import com.example.trackify.dto.Contenido.ContenidoDTO;
import com.example.trackify.dto.Contenido.RequestContenidoDTO;
import com.example.trackify.entity.*;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.mapper.ContenidoMapper;
import com.example.trackify.repository.Contenido.IContenidoRepository;
import com.example.trackify.repository.Estado.IEstadoRepository;
import com.example.trackify.repository.Genero.IGeneroRepository;
import com.example.trackify.repository.Tipo.ITipoRepository;
import com.example.trackify.repository.Usuario.IUsuarioRepository;
import com.example.trackify.service.contenido.ContenidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContenidoServiceTest {

    @Mock
    private IContenidoRepository contenidoRepository;

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private IGeneroRepository generoRepository;

    @Mock
    private ITipoRepository tipoRepository;

    @Mock
    private IEstadoRepository estadoRepository;

    @Mock
    private ContenidoMapper contenidoMapper;

    @InjectMocks
    private ContenidoService contenidoService;

    private Usuario usuario;
    private Contenido contenido;
    private ContenidoDTO contenidoDTO;
    private RequestContenidoDTO requestDTO;
    private Genero genero;
    private Tipo tipo;
    private Estado estado;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setCodigo(1L);
        usuario.setNombreUsuario("david");

        genero = new Genero();
        genero.setId(1L);
        genero.setNombre("Acción");

        tipo = new Tipo();
        tipo.setId(1L);
        tipo.setNombre("Película");

        estado = new Estado();
        estado.setId(1L);
        estado.setNombre("Visto");

        contenido = new Contenido();
        contenido.setId(1L);
        contenido.setTitulo("Superman");
        contenido.setValoracion(5);
        contenido.setDescripcion("Película de superhéroes");
        contenido.setUsuario(usuario);
        contenido.setGenero(genero);
        contenido.setTipo(tipo);
        contenido.setEstado(estado);

        contenidoDTO = new ContenidoDTO();
        contenidoDTO.setId(1L);
        contenidoDTO.setTitulo("Superman");
        contenidoDTO.setValoracion(5);
        contenidoDTO.setDescripcion("Película de superhéroes");

        requestDTO = new RequestContenidoDTO(
                "Superman",
                5,
                "Película de superhéroes",
                "Acción",
                "Película",
                "Visto"
        );
    }

    @Test
    void crearDebeCrearContenidoDelUsuarioAutenticado() {
        when(usuarioRepository.findByUsername("david")).thenReturn(Optional.of(usuario));
        when(contenidoMapper.toEntity(requestDTO)).thenReturn(new Contenido());
        when(generoRepository.findByNombre("Acción")).thenReturn(Optional.of(genero));
        when(tipoRepository.findByNombre("Película")).thenReturn(Optional.of(tipo));
        when(estadoRepository.findByNombre("Visto")).thenReturn(Optional.of(estado));
        when(contenidoRepository.save(any(Contenido.class))).thenReturn(contenido);
        when(contenidoMapper.toDTO(contenido)).thenReturn(contenidoDTO);

        ContenidoDTO resultado = contenidoService.crear("david", requestDTO);

        assertEquals("Superman", resultado.getTitulo());
        verify(contenidoRepository).save(any(Contenido.class));
    }

    @Test
    void obtenerPorIdDebeDevolverContenidoSiEsDelUsuario() {
        when(usuarioRepository.findByUsername("david")).thenReturn(Optional.of(usuario));
        when(contenidoRepository.findById(1L)).thenReturn(Optional.of(contenido));
        when(contenidoMapper.toDTO(contenido)).thenReturn(contenidoDTO);

        ContenidoDTO resultado = contenidoService.obtenerPorId("david", 1L);

        assertEquals(1L, resultado.getId());
        verify(contenidoRepository).findById(1L);
    }

    @Test
    void obtenerPorIdDebeLanzarNotFoundSiNoEsDelUsuario() {
        Usuario otroUsuario = new Usuario();
        otroUsuario.setCodigo(2L);
        contenido.setUsuario(otroUsuario);

        when(usuarioRepository.findByUsername("david")).thenReturn(Optional.of(usuario));
        when(contenidoRepository.findById(1L)).thenReturn(Optional.of(contenido));

        assertThrows(NotFoundEntityException.class, () -> contenidoService.obtenerPorId("david", 1L));
    }

    @Test
    void listarPorUsuarioDebeListarContenidosDelUsuario() {
        when(usuarioRepository.findByUsername("david")).thenReturn(Optional.of(usuario));
        when(contenidoRepository.listarContenidoPorUsuario(1L)).thenReturn(List.of(contenido));
        when(contenidoMapper.toDTOList(List.of(contenido))).thenReturn(List.of(contenidoDTO));

        List<ContenidoDTO> resultado = contenidoService.listarPorUsuario("david");

        assertEquals(1, resultado.size());
        verify(contenidoRepository).listarContenidoPorUsuario(1L);
    }

    @Test
    void actualizarDebeActualizarContenidoSiEsDelUsuario() {
        when(usuarioRepository.findByUsername("david")).thenReturn(Optional.of(usuario));
        when(contenidoRepository.findById(1L)).thenReturn(Optional.of(contenido));
        when(generoRepository.findByNombre("Acción")).thenReturn(Optional.of(genero));
        when(tipoRepository.findByNombre("Película")).thenReturn(Optional.of(tipo));
        when(estadoRepository.findByNombre("Visto")).thenReturn(Optional.of(estado));
        when(contenidoRepository.save(contenido)).thenReturn(contenido);
        when(contenidoMapper.toDTO(contenido)).thenReturn(contenidoDTO);

        ContenidoDTO resultado = contenidoService.actualizar("david", 1L, requestDTO);

        assertEquals("Superman", resultado.getTitulo());
        verify(contenidoMapper).updateEntityFromDTO(requestDTO, contenido);
        verify(contenidoRepository).save(contenido);
    }

    @Test
    void eliminarDebeEliminarContenidoSiEsDelUsuario() {
        when(usuarioRepository.findByUsername("david")).thenReturn(Optional.of(usuario));
        when(contenidoRepository.findById(1L)).thenReturn(Optional.of(contenido));

        contenidoService.eliminar("david", 1L);

        verify(contenidoRepository).delete(contenido);
    }

    @Test
    void filtrarDebeFiltrarContenidoDelUsuario() {
        when(usuarioRepository.findByUsername("david")).thenReturn(Optional.of(usuario));
        when(contenidoRepository.filtrarContenido(1L, "Película", "Acción", "Visto", 5, "Super"))
                .thenReturn(List.of(contenido));
        when(contenidoMapper.toDTOList(List.of(contenido))).thenReturn(List.of(contenidoDTO));

        List<ContenidoDTO> resultado = contenidoService.filtrar("david", "Película", "Acción", "Visto", 5, "Super");

        assertEquals(1, resultado.size());
        verify(contenidoRepository).filtrarContenido(1L, "Película", "Acción", "Visto", 5, "Super");
    }
}
