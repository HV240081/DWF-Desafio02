-- Productos
INSERT INTO producto (nombre, descripcion, precio, categoria, stock, disponible) VALUES
('Pizza Margarita', 'Pizza con tomate y albahaca', 7.99, 'Italiana', 10, true),
('Hamburguesa BBQ', 'Con cebolla caramelizada y tocino', 8.99, 'Americana', 5, true),
('Pasta Alfredo', 'Fettuccine con salsa Alfredo', 9.49, 'Italiana', 7, true),
('Ensalada César', 'Lechuga, parmesano y crutones', 4.99, 'Ensaladas', 12, true);

-- Clientes
INSERT INTO cliente (nombre, email, telefono, direccion, fecha_registro, activo) VALUES
('Carlos Pérez', 'carlos@example.com', '7777-7777', 'Calle Luna #123', CURRENT_DATE, true),
('Ana Torres', 'ana@example.com', '6666-6666', 'Av. Sol #456', CURRENT_DATE, true);

-- Pedidos
INSERT INTO pedido (cliente_id, total, metodo_pago, fecha_pedido, estado) VALUES
(1, 16.98, 'Tarjeta', CURRENT_TIMESTAMP, 'PENDIENTE'),
(2, 8.99, 'Efectivo', CURRENT_TIMESTAMP, 'ENTREGADO');

-- Productos del pedido
INSERT INTO pedido_producto (pedido_id, producto_id, cantidad, subtotal) VALUES
(1, 1, 2, 15.98),
(2, 2, 1, 8.99);
