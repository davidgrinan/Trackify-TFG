package com.example.trackify.mapper;

import com.example.trackify.dto.Usuario.CrearUsuarioDTO;
import com.example.trackify.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {

    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "contenidos", ignore = true)
    Usuario toEntity(CrearUsuarioDTO crearUsuarioDTO);

    UsuarioDetalleDTO toDetalleDTO(Usuario usuario);

    List<UsuarioDetalleDTO> toDetalleDTOList(List<Usuario> usuarios);
}
