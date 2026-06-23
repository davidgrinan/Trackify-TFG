package com.example.trackify.service.genero;

import com.example.trackify.dto.Genero.GeneroDetalleDTO;
import com.example.trackify.entity.Genero;
import com.example.trackify.exceptions.NotFoundEntityException;
import com.example.trackify.mapper.GeneroMapper;
import com.example.trackify.repository.Genero.IGeneroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GeneroService implements IGeneroService {
    private final IGeneroRepository generoRepository;
    private final GeneroMapper mapper;

    @Override
    public List<GeneroDetalleDTO> listarTodos() {
        return mapper.toDetalleDTOList((List<Genero>) generoRepository.findAll());
    }

    @Override
    public GeneroDetalleDTO obtenerPorId(Long id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(
                        "Genero con id " + id + " no encontrado"
                ));
        return mapper.toDetalleDTO(genero);
    }
}
