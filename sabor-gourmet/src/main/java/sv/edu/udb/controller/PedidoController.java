package sv.edu.udb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.dto.PedidoDTO;
import sv.edu.udb.dto.PedidoProductoRespuestaDTO;
import sv.edu.udb.dto.PedidoRespuestaDTO;
import sv.edu.udb.model.Pedido;
import sv.edu.udb.model.PedidoProducto;
import sv.edu.udb.service.PedidoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con la gestión de pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody @Valid @Schema(description = "Datos del nuevo pedido") PedidoDTO pedidoDTO) {
        Pedido pedidoCreado = pedidoService.crearPedido(pedidoDTO);
        return new ResponseEntity<>(pedidoCreado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PedidoRespuestaDTO>> obtenerTodos() {
        List<Pedido> pedidos = pedidoService.obtenerTodos();
        List<PedidoRespuestaDTO> pedidoRespuestaDTOS = pedidos.stream()
                .map(this::convertToPedidoRespuestaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pedidoRespuestaDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoRespuestaDTO> obtenerPorId(@Parameter(description = "ID del pedido a buscar", example = "1") @PathVariable Long id) {
        Pedido pedido = pedidoService.obtenerPorId(id);
        return ResponseEntity.ok(convertToPedidoRespuestaDTO(pedido));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoRespuestaDTO>> obtenerPorCliente(@Parameter(description = "ID del cliente para buscar sus pedidos", example = "1") @PathVariable Long clienteId) {
        List<Pedido> pedidos = pedidoService.obtenerPorCliente(clienteId);
        List<PedidoRespuestaDTO> pedidoRespuestaDTOS = pedidos.stream()
                .map(this::convertToPedidoRespuestaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pedidoRespuestaDTOS);
    }

    private PedidoRespuestaDTO convertToPedidoRespuestaDTO(Pedido pedido) {
        PedidoRespuestaDTO dto = new PedidoRespuestaDTO();
        dto.setId(pedido.getId());
        dto.setClienteId(pedido.getCliente().getId());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setTotal(pedido.getTotal());
        dto.setMetodoPago(pedido.getMetodoPago());
        dto.setEstado(pedido.getEstado());
        dto.setProductos(pedido.getProductos().stream()
                .map(this::convertToPedidoProductoRespuestaDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private PedidoProductoRespuestaDTO convertToPedidoProductoRespuestaDTO(PedidoProducto pedidoProducto) {
        PedidoProductoRespuestaDTO dto = new PedidoProductoRespuestaDTO();
        dto.setId(pedidoProducto.getId());
        dto.setProductoId(pedidoProducto.getProducto().getId());
        dto.setProductoNombre(pedidoProducto.getProducto().getNombre()); // Puedes agregar más detalles del producto
        dto.setCantidad(pedidoProducto.getCantidad());
        dto.setSubtotal(pedidoProducto.getSubtotal());
        return dto;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}