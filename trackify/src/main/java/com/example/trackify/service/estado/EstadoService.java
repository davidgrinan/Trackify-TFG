package com.example.trackify.service.estado;

import com.example.trackify.dto.Estado.EstadoDetalleDTO;
import com.example.trackify.entity.Estado;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.mapper.EstadoMapper;
import com.example.trackify.repository.Estado.IEstadoRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EstadoService implements IEstadoService {

    private final IEstadoRepository estadoRepository;
    private final EstadoMapper mapper;


    @Override
        public List<EstadoDetalleDTO> listarTodos() {
        return mapper.toDetalleDTOList((List<Estado>) estadoRepository.findAll());
    }

    @Override
    public EstadoDetalleDTO obtenerPorId(Long id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundEntityException("Estado con id " + id + " no encontrado")
                );

        return mapper.toDetalleDTO(estado);
    }
}
