package com.daw.persistence.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.daw.persistence.entities.enums.MetodoPedido;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
public class PedidoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "id_cliente")
	private long idCliente;
	
	private LocalDateTime fecha;
	private double total;
	
	@Enumerated(EnumType.STRING)
	private MetodoPedido metodo;
	private String notas;	
	
	@ManyToOne
	// En name ponemos el nombre de la FK en base de datos 
	// En referencedColumnName ponemos el nombre en base de datos de la PK a la que hacer referencia
	@JoinColumn(name = "id_cliente", referencedColumnName = "id", insertable = false, updatable = false)
	// El nombre de este atributo es lo que ponemos en el mappedBy
	private ClienteEntity cliente;
	
	@OneToMany(mappedBy = "pedido")
	@JsonIgnore
	private List<PizzaPedidoEntity> pizzaPedidos;

}
