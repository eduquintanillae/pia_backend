package com.example.models.entitys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "FechaIntereses")
public class FechaIntereses {
	
	@Id
	private Long id;

	@Column(name = "fecha_interes")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@NotNull
	private Date fechaInteres;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechaInteres() {
		return fechaInteres;
	}

	public void setFechaInteres(Date fechaInteres) {
		this.fechaInteres = fechaInteres;
	}
	
}
