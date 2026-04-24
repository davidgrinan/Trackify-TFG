package com.example.trackify.entity;

import com.example.trackify.Enum.Tipo;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "contenido")
public class Contenido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Column(nullable = false)
    private String genero;

    @Column(nullable = false)
    private String estado;

    @Column()
    private Integer valoracion;

    @Column(name = "descripcion")
    private String descripcion;


}
