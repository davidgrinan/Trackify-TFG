package com.example.trackify.mapper;

import com.example.trackify.dto.Estado.EstadoDetalleDTO;
import com.example.trackify.entity.Estado;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EstadoMapper {

    EstadoDetalleDTO toDetalleDTO(Estado estado);

    List<EstadoDetalleDTO> toDetalleDTOList(List<Estado> estados);
}
