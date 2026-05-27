package com.example.trackify.repository.Contenido;

import com.example.trackify.entity.Contenido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IContenidoRepository extends CrudRepository<Contenido, Long> {
    @Query("""
        SELECT c FROM Contenido c
        WHERE c.usuario.codigo = :usuarioId
    """)
    List<Contenido> listarContenidoPorUsuario(@Param("usuarioId") Long usuarioId);

    @Query("""
        SELECT conte FROM Contenido conte
        WHERE conte.usuario.codigo = :codigo
        AND (:tipo IS NULL OR conte.tipo.nombre = :tipo)
        AND (:genero IS NULL OR conte.genero.nombre = :genero)
        AND (:estado IS NULL OR conte.estado.nombre = :estado)
        AND (:valoracion IS NULL OR conte.valoracion >= :valoracion)
        AND (:titulo IS NULL OR LOWER(conte.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')))
    """)
    List<Contenido> filtrarContenido(
            @Param("codigo") Long codigo,
            @Param("tipo") String tipo,
            @Param("genero") String genero,
            @Param("estado") String estado,
            @Param("valoracion") Integer valoracion,
            @Param("titulo") String titulo
    );
}