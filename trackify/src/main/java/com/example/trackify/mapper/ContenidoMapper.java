package com.example.trackify.mapper;

import com.example.trackify.dto.Contenido.ContenidoDTO;
import com.example.trackify.dto.Contenido.ContenidoDetalleDTO;
import com.example.trackify.dto.Contenido.RequestContenidoDTO;
import com.example.trackify.entity.Contenido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {GeneroMapper.class, TipoMapper.class, EstadoMapper.class, UsuarioMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ContenidoMapper {

    ContenidoDTO toDTO(Contenido contenido);

    List<ContenidoDTO> toDTOList(List<Contenido> contenidos);

    ContenidoDetalleDTO toDetalleDTO(Contenido contenido);

    List<ContenidoDetalleDTO> toDetalleDTOList(List<Contenido> contenidos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "genero", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Contenido toEntity(RequestContenidoDTO requestContenidoDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "genero", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "estado", ignore = true)
    void updateEntityFromDTO(RequestContenidoDTO dto, @MappingTarget Contenido entity);
}
