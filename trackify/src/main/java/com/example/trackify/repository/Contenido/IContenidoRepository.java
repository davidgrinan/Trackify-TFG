package com.example.trackify.repository.Contenido;

import com.example.trackify.entity.Contenido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IContenidoRepository extends CrudRepository<Contenido, Long> {

    @Query("SELECT c FROM Contenido c WHERE c.usuario.codigo = :usuarioId")
    List<Contenido> listarContenidoPorUsuario(@Param("usuarioId") Long usuarioId);

    @Query("""
            SELECT c
            FROM Contenido c
            WHERE c.usuario.codigo = :usuarioId
              AND (:tipo IS NULL OR LOWER(c.tipo.nombre) = LOWER(:tipo))
              AND (:genero IS NULL OR LOWER(c.genero.nombre) = LOWER(:genero))
              AND (:estado IS NULL OR LOWER(c.estado.nombre) = LOWER(:estado))
              AND (:valoracion IS NULL OR c.valoracion = :valoracion)
            """)
    List<Contenido> filtrarContenidoPorUsuario(
            @Param("usuarioId") Long usuarioId,
            @Param("tipo") String tipo,
            @Param("genero") String genero,
            @Param("estado") String estado,
            @Param("valoracion") Integer valoracion
    );

    @Query("""
            SELECT c
            FROM Contenido c
            WHERE c.usuario.codigo = :usuarioId
              AND LOWER(c.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))
            """)
    List<Contenido> buscarContenidoPorTitulo(
            @Param("usuarioId") Long usuarioId,
            @Param("titulo") String titulo
    );
}
