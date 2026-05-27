package com.example.trackify.mapper;

import com.example.trackify.dto.Usuario.CrearUsuarioDTO;
import com.example.trackify.entity.Usuario;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UsuarioMapper {

    @Mapping(target = "roles", ignore = true)
    Usuario toUsuarioFromCreateUsuarioDTO(CrearUsuarioDTO dto);

    @Mapping(target = "roles", ignore = true)
    CrearUsuarioDTO toCrearUsuarioDTOFromUsuario(Usuario usuario);
}