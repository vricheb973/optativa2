-- DML: Datos de prueba variados

-- 1. Clientes
INSERT INTO cliente (nombre, direccion, email, telefono) VALUES
('Carlos Mendoza', 'Calle Sierpes 12, Sevilla', 'carlos.mendoza@email.com', '612345678'),
('María García', 'Avenida de la Constitución 45, 3ºB, Sevilla', 'maria.garcia@email.com', '698765432'),
('Alejandro Ruiz', 'Calle Tetuán 8, Sevilla', 'alex.ruiz@email.com', '655443322'),
('Elena Benítez', 'Plaza del Salvador 3, Sevilla', 'elena.benitez@email.com', '677889900'),
('Lucía Fernández', 'Calle Betis 19, Sevilla', 'lucia.f@email.com', '611223344');

-- 2. Pizzas
INSERT INTO pizza (nombre, descripcion, precio, vegetariana, vegana, disponible) VALUES
('Margarita', 'Salsa de tomate, mozzarella fresca y albahaca', 8.50, TRUE, FALSE, TRUE),
('Cuatro Quesos', 'Mozzarella, gorgonzola, parmesano y fontina', 11.00, TRUE, FALSE, TRUE),
('Barbacoa', 'Salsa barbacoa, carne picada, bacon, pollo y mozzarella', 12.50, FALSE, FALSE, TRUE),
('Vegana Suprema', 'Salsa de tomate, queso vegano, champiñones, pimientos y aceitunas', 10.50, TRUE, TRUE, TRUE),
('Pepperoni', 'Salsa de tomate, mozzarella y abundante pepperoni crujiente', 10.00, FALSE, FALSE, TRUE),
('Prosciutto e Funghi', 'Salsa de tomate, mozzarella, jamón cocido y champiñones', 10.50, FALSE, FALSE, TRUE),
('Huerta Especial', 'Salsa de tomate, mozzarella, calabacín, berenjena y alcachofas', 9.50, TRUE, FALSE, FALSE);

-- 3. Pedidos (actualizados con los valores exactos del enum MetodoPedido)
INSERT INTO pedido (id_cliente, fecha, total, metodo, notas) VALUES
(1, '2026-10-25 14:30:00', 21.00, 'DOMICILIO', 'Entregar en portería'),
(2, '2026-10-28 21:15:00', 12.50, 'RECOGER', 'Sin picante en la salsa'),
(1, '2026-11-01 13:45:00', 29.50, 'DOMICILIO', 'Llamar al timbre 3ºB'),
(3, '2026-11-02 20:00:00', 10.50, 'LOCAL', 'Reserva mesa terraza'),
(4, '2026-11-03 21:30:00', 31.50, 'DOMICILIO', 'Dejar la pizza en la puerta');

-- 4. Detalle del pedido
INSERT INTO pizza_pedido (id_pedido, id_pizza, cantidad, precio) VALUES
(1, 1, 1.0, 8.50),
(1, 3, 1.0, 12.50),
(2, 3, 1.0, 12.50),
(3, 2, 1.0, 11.00),
(3, 5, 1.0, 10.00),
(3, 1, 1.0, 8.50),
(4, 4, 1.0, 10.50),
(5, 6, 3.0, 10.50);