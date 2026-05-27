package com.example.trackify.mapper;

import com.example.trackify.dto.Genero.GeneroDetalleDTO;
import com.example.trackify.entity.Genero;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GeneroMapper {

    GeneroDetalleDTO toDetalleDTO(Genero genero);

    List<GeneroDetalleDTO> toDetalleDTOList(List<Genero> generos);
}
