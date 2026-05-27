package com.example.trackify.service.tipo;

import com.example.trackify.dto.Tipo.*;
import com.example.trackify.entity.Tipo;
import com.example.trackify.exceptions.*;
import com.example.trackify.mapper.TipoMapper;
import com.example.trackify.repository.Tipo.ITipoRepository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TipoService implements ITipoService {

    private final ITipoRepository tipoRepository;
    private final TipoMapper mapper;

    private final Logger logger = LoggerFactory.getLogger(TipoService.class);


    @Override
    public List<TipoDetalleDTO> listarTodos() {
        logger.info("listando tipos");
        return mapper.toDetalleDTOList((List<Tipo>) tipoRepository.findAll());
    }

    @Override
    public TipoDetalleDTO obtenerPorId(Long id) {
        logger.info("obteniendo tipo con id {}", id);
        Tipo tipo = tipoRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundEntityException("Tipo con id " + id + " no encontrado")
                );
        logger.info("tipo obtenido correctamente");
        return mapper.toDetalleDTO(tipo);
    }
}
