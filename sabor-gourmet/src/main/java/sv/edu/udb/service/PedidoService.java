package sv.edu.udb.service;

import jakarta.validation.Valid;
import sv.edu.udb.dto.PedidoDTO;
import sv.edu.udb.model.Pedido;

import java.util.List;

public interface PedidoService {
    Pedido crearPedido(@Valid PedidoDTO pedidoDTO);
    List<Pedido> obtenerTodos();
    Pedido obtenerPorId(Long id);
    List<Pedido> obtenerPorCliente(Long clienteId);
}