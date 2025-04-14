package sv.edu.udb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import sv.edu.udb.dto.PedidoDTO;
import sv.edu.udb.dto.PedidoDetalleDTO;
import sv.edu.udb.model.Cliente;
import sv.edu.udb.model.Pedido;
import sv.edu.udb.model.PedidoProducto;
import sv.edu.udb.service.PedidoService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setMetodoPago("Tarjeta");
        pedido.setEstado("ENTREGADO");
        pedido.setTotal(30.0);

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        pedido.setCliente(cliente);

        pedido.setProductos(Collections.emptyList());
    }

    @Test
    void testCrearPedido() throws Exception {
        PedidoDetalleDTO producto = new PedidoDetalleDTO();
        producto.setProductoId(1L);
        producto.setCantidad(2);

        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(1L);
        pedidoDTO.setMetodoPago("Tarjeta");
        pedidoDTO.setEstado("ENTREGADO");
        pedidoDTO.setProductos(List.of(producto));

        Mockito.when(pedidoService.crearPedido(any(PedidoDTO.class))).thenReturn(pedido);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }


    @Test
    void testObtenerTodos() throws Exception {
        Mockito.when(pedidoService.obtenerTodos()).thenReturn(List.of(pedido));

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].clienteId").value(1));
    }

    @Test
    void testObtenerPorId() throws Exception {
        Mockito.when(pedidoService.obtenerPorId(1L)).thenReturn(pedido);

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clienteId").value(1));
    }

    @Test
    void testObtenerPorCliente() throws Exception {
        Mockito.when(pedidoService.obtenerPorCliente(1L)).thenReturn(List.of(pedido));

        mockMvc.perform(get("/api/pedidos/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].clienteId").value(1));
    }
}
