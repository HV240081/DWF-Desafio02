package sv.edu.udb.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import sv.edu.udb.dto.PedidoDTO;
import sv.edu.udb.dto.PedidoDetalleDTO;
import sv.edu.udb.model.*;
import sv.edu.udb.repository.*;
import sv.edu.udb.service.PedidoService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final PedidoProductoRepository pedidoProductoRepository;

    @Override
    @Transactional
    public Pedido crearPedido(@Valid PedidoDTO pedidoDTO) {
        Cliente cliente = clienteRepository.findById(pedidoDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + pedidoDTO.getClienteId()));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setMetodoPago(pedidoDTO.getMetodoPago());

        List<String> estadosValidos = Arrays.asList("PENDIENTE", "EN_PROCESO", "ENVIADO", "ENTREGADO", "CANCELADO");
        if (!estadosValidos.contains(pedidoDTO.getEstado().toUpperCase())) {
            throw new IllegalArgumentException("El estado del pedido '" + pedidoDTO.getEstado() + "' no es válido.");
        }
        pedido.setEstado(pedidoDTO.getEstado().toUpperCase());

        List<PedidoProducto> detalles = new ArrayList<>();
        double total = 0.0;

        for (PedidoDetalleDTO detalleDTO : pedidoDTO.getProductos()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + detalleDTO.getProductoId()));

            if (!producto.isDisponible()) {
                throw new RuntimeException("El producto " + producto.getNombre() + " no está disponible.");
            }

            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new RuntimeException("No hay suficiente stock del producto: " + producto.getNombre() + ". Stock actual: " + producto.getStock() + ", Cantidad requerida: " + detalleDTO.getCantidad());
            }

            producto.setStock(producto.getStock() - detalleDTO.getCantidad());
            productoRepository.save(producto);

            PedidoProducto detalle = new PedidoProducto();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setSubtotal(producto.getPrecio() * detalleDTO.getCantidad());

            total += detalle.getSubtotal();
            detalles.add(detalle);
        }

        pedido.setTotal(total);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);


        for (PedidoProducto detalle : detalles) {
            detalle.setPedido(pedidoGuardado);
            pedidoProductoRepository.save(detalle);
        }

        pedidoGuardado.setProductos(detalles);
        return pedidoGuardado;
    }

    @Override
    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    @Override
    public List<Pedido> obtenerPorCliente(Long clienteId) {
        return pedidoRepository.findByCliente_Id(clienteId);
    }
}