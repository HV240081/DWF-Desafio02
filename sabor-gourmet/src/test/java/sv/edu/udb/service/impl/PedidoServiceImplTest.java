package sv.edu.udb.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import sv.edu.udb.dto.PedidoDTO;
import sv.edu.udb.dto.PedidoDetalleDTO;
import sv.edu.udb.model.*;
import sv.edu.udb.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private PedidoProductoRepository pedidoProductoRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Cliente cliente;
    private Producto producto;
    private PedidoDTO pedidoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Carlos");

        producto = new Producto();
        producto.setId(10L);
        producto.setNombre("Camiseta");
        producto.setPrecio(20.0);
        producto.setDisponible(true);
        producto.setStock(10);

        PedidoDetalleDTO detalleDTO = new PedidoDetalleDTO();
        detalleDTO.setProductoId(producto.getId());
        detalleDTO.setCantidad(2);

        pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(cliente.getId());
        pedidoDTO.setMetodoPago("TARJETA");
        pedidoDTO.setEstado("pendiente");
        pedidoDTO.setProductos(List.of(detalleDTO));
    }

    @Test
    void testCrearPedido_Exito() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(pedidoRepository.save(any())).thenAnswer(i -> {
            Pedido p = i.getArgument(0);
            p.setId(100L);
            return p;
        });
        when(pedidoProductoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Pedido resultado = pedidoService.crearPedido(pedidoDTO);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals(1, resultado.getProductos().size());
        assertEquals(40.0, resultado.getTotal());
        verify(productoRepository).save(producto);
        verify(pedidoRepository).save(any(Pedido.class));
        verify(pedidoProductoRepository, times(1)).save(any(PedidoProducto.class));
    }

    @Test
    void testCrearPedido_ClienteNoExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.crearPedido(pedidoDTO);
        });

        assertTrue(ex.getMessage().contains("Cliente no encontrado"));
    }

    @Test
    void testCrearPedido_ProductoNoExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.crearPedido(pedidoDTO);
        });

        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    @Test
    void testCrearPedido_ProductoNoDisponible() {
        producto.setDisponible(false);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.crearPedido(pedidoDTO);
        });

        assertTrue(ex.getMessage().contains("no está disponible"));
    }

    @Test
    void testCrearPedido_StockInsuficiente() {
        producto.setStock(1); // menos que los 2 solicitados

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.crearPedido(pedidoDTO);
        });

        assertTrue(ex.getMessage().contains("No hay suficiente stock"));
    }

    @Test
    void testCrearPedido_EstadoInvalido() {
        pedidoDTO.setEstado("desconocido");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.crearPedido(pedidoDTO);
        });

        assertEquals("El estado del pedido 'desconocido' no es válido.", ex.getMessage());
    }

    @Test
    void testObtenerTodos() {
        Pedido pedido = new Pedido();
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));

        List<Pedido> lista = pedidoService.obtenerTodos();

        assertEquals(1, lista.size());
    }

    @Test
    void testObtenerPorId_Exito() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido result = pedidoService.obtenerPorId(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testObtenerPorId_NoEncontrado() {
        when(pedidoRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pedidoService.obtenerPorId(2L);
        });

        assertTrue(ex.getMessage().contains("Pedido no encontrado"));
    }

    @Test
    void testObtenerPorCliente() {
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        when(pedidoRepository.findByCliente_Id(1L)).thenReturn(List.of(pedido));

        List<Pedido> lista = pedidoService.obtenerPorCliente(1L);

        assertEquals(1, lista.size());
        assertEquals(cliente, lista.get(0).getCliente());
    }
}
