package com.daw.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pizza_pedido")
@Getter
@Setter
@NoArgsConstructor
public class PizzaPedidoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "id_pedido")
	private long idPedido;
	
	@Column(name = "id_pizza")
	private long idPizza;
	private double cantidad;
	private double precio;
	
	@ManyToOne
	@JoinColumn(name = "id_pedido", referencedColumnName = "id", insertable = false, updatable = false)
	private PedidoEntity pedido;
	
	@ManyToOne
	@JoinColumn(name = "id_pizza", referencedColumnName = "id", insertable = false, updatable = false)
	private PizzaEntity pizza;

}
