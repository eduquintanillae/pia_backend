package com.example.models.entitys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

//Aquí se va a guardar cuál es el usuario que tiene iniciada la sesión (sólo el id)
//Si es el admin, el id es 0
//Si es cliente, el id es mayor a 0

@Entity
@Table(name = "usuario_actual")
public class UsuarioActual {
	
	//Este id sólo es para tenerlo fijo en la tabla, sólo hay un registro que se actualiza
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_tabla")
	private int idTabla;
	
	//Este es en sí el id que indica qué usuario está conectado
	//Puede ser null cuando se sale sesión
	private Long idUsuario;
	
	//Indica si hay usuario con sesión iniciada o no
	@NotNull
	private boolean conectado;

	public int getIdTabla() {
		return idTabla;
	}

	public void setIdTabla(int idTabla) {
		this.idTabla = idTabla;
	}
	
	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public boolean isConectado() {
		return conectado;
	}

	public void setConectado(boolean conectado) {
		this.conectado = conectado;
	}

}
