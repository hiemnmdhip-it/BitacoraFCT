package com.bitacorafct.bitacorafct.domain;

import java.time.LocalDate;

import com.bitacorafct.bitacorafct.exception.JornadaSinReflexionException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jornadas")
@Getter
@NoArgsConstructor
public class Jornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha", nullable = false, unique = true)
    private LocalDate fecha;

    @Column(name = "reflexion", columnDefinition = "TEXT")
    private String reflexion;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "cierre_id")
    private CierreSemanal cierreSemanal;

    public Jornada(LocalDate fecha, String reflexion) {
        this.fecha = fecha;
        this.reflexion = reflexion;
    }

    public boolean tieneReflexion() {
        return reflexion != null && !reflexion.isBlank();
    }
    
    public boolean estaCerrada() {
    	return cierreSemanal != null;
    }
    
    public void asignarCierre(CierreSemanal cierre) {
    	if (!tieneReflexion()) {
    		throw new JornadaSinReflexionException(this.reflexion);
    	}
    	this.cierreSemanal = cierre;
    	}
    }
