package com.example.trackify.config;

import com.example.trackify.Enum.RoleType;
import com.example.trackify.entity.Estado;
import com.example.trackify.entity.Genero;
import com.example.trackify.entity.Role;
import com.example.trackify.entity.Tipo;
import com.example.trackify.repository.Estado.IEstadoRepository;
import com.example.trackify.repository.Genero.IGeneroRepository;
import com.example.trackify.repository.Role.IRoleRepository;
import com.example.trackify.repository.Tipo.ITipoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatosIniciales.class);

    private final IGeneroRepository generoRepository;
    private final ITipoRepository tipoRepository;
    private final IEstadoRepository estadoRepository;
    private final IRoleRepository roleRepository;

    public DatosIniciales(IGeneroRepository generoRepository,
                          ITipoRepository tipoRepository,
                          IEstadoRepository estadoRepository,
                          IRoleRepository roleRepository) {
        this.generoRepository = generoRepository;
        this.tipoRepository = tipoRepository;
        this.estadoRepository = estadoRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        try {
            logger.info("Iniciando insercion de datos iniciales...");

            crearGeneroSiNoExiste("Acción");
            crearGeneroSiNoExiste("Drama");
            crearGeneroSiNoExiste("Terror");
            crearGeneroSiNoExiste("Comedia");

            crearTipoSiNoExiste("Película");
            crearTipoSiNoExiste("Serie");
            crearTipoSiNoExiste("Videojuego");

            crearEstadoSiNoExiste("Visto");
            crearEstadoSiNoExiste("Pendiente");
            crearEstadoSiNoExiste("No ver");

            crearRoleSiNoExiste(RoleType.ADMIN);
            crearRoleSiNoExiste(RoleType.USER);

            logger.info("Insercion de datos iniciales finalizada");

        } catch (Exception e) {

            logger.error("Error en inicialización de datos: {}", e.getMessage(), e);

        }
    }

    private void crearGeneroSiNoExiste(String nombre) {
        if (!generoRepository.existsByNombre(nombre)) {
            generoRepository.save(new Genero(null, nombre, null));
            logger.info("Género añadido: {}", nombre);
        }
    }

    private void crearTipoSiNoExiste(String nombre) {
        if (!tipoRepository.existsByNombre(nombre)) {
            tipoRepository.save(new Tipo(null, nombre, null));
            logger.info("Tipo añadido: {}", nombre);
        }
    }

    private void crearEstadoSiNoExiste(String nombre) {
        if (!estadoRepository.existsByNombre(nombre)) {
            estadoRepository.save(new Estado(null, nombre, null));
            logger.info("Estado añadido: {}", nombre);
        }
    }

    private void crearRoleSiNoExiste(RoleType rol) {
        if (!roleRepository.existsByRol(rol)) {
            roleRepository.save(new Role(null, rol));
            logger.info("Role añadido: {}", rol);
        }
    }
}
