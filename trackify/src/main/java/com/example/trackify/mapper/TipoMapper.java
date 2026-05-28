package com.example.trackify.mapper;

import com.example.trackify.dto.Tipo.TipoDetalleDTO;
import com.example.trackify.entity.Tipo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TipoMapper {

    TipoDetalleDTO toDetalleDTO(Tipo tipo);

    List<TipoDetalleDTO> toDetalleDTOList(List<Tipo> tipos);
}
