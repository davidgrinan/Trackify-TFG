package com.example.trackify.service.genero;

import com.example.trackify.dto.Genero.GeneroDetalleDTO;
import com.example.trackify.entity.Genero;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.mapper.GeneroMapper;
import com.example.trackify.repository.Genero.IGeneroRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GeneroService implements IGeneroService {
    private final IGeneroRepository generoRepository;
    private final GeneroMapper mapper;

    private final Logger logger = LoggerFactory.getLogger(GeneroService.class);

    @Override
    public List<GeneroDetalleDTO> listarTodos() {
        logger.info("listando generos");
        return mapper.toDetalleDTOList((List<Genero>) generoRepository.findAll());
    }

    @Override
    public GeneroDetalleDTO obtenerPorId(Long id) {
        logger.info("obteniendo genero con id {}", id);
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(
                        "Genero con id " + id + " no encontrado"
                ));
        logger.info("genero obtenido correctamente");

        return mapper.toDetalleDTO(genero);
    }
}
