package com.bitacorafct.bitacorafct.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "actividades")
@Getter
@NoArgsConstructor
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "jornada_id", nullable = false)
    private Jornada jornada;

    @Column(name = "descripcion", length = 255, nullable = false)
    private String descripcion;

    @Column(name = "horas", nullable = false)
    private double horas;

    public Actividad(Jornada jornada, String descripcion, double horas) {
        if (horas < 0) {
            throw new IllegalArgumentException("Las horas deben ser mayores que 0");
        }
        this.jornada = jornada;
        this.descripcion = descripcion;
        this.horas = horas;
    }
}