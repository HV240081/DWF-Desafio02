package sv.edu.udb.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.udb.model.Producto;
import sv.edu.udb.repository.ProductoRepository;
import sv.edu.udb.service.ProductoService;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setDescripcion("Descripción de prueba");
        producto.setPrecio(100.0);
        producto.setCategoria("Electrónica");
        producto.setStock(50);
        producto.setDisponible(true);
    }

    @Test
    void testCrearProducto() {
        // Mocking el comportamiento del repositorio
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.crearProducto(producto);

        assertNotNull(resultado);
        assertEquals("Producto Test", resultado.getNombre());
        assertEquals("Descripción de prueba", resultado.getDescripcion());
    }

    @Test
    void testObtenerPorIdProductoExistente() {
        // Mocking el comportamiento del repositorio
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Producto Test", resultado.getNombre());
    }

    @Test
    void testObtenerPorIdProductoNoExistente() {
        // Mocking el comportamiento del repositorio
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());

        // Aseguramos que el producto no exista
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.obtenerPorId(2L);
        });

        assertEquals("Producto no encontrado", exception.getMessage());
    }

    @Test
    void testActualizarProducto() {
        Producto productoActualizado = new Producto();
        productoActualizado.setId(1L);
        productoActualizado.setNombre("Producto Actualizado");
        productoActualizado.setDescripcion("Descripción actualizada");
        productoActualizado.setPrecio(150.0);
        productoActualizado.setCategoria("Electrónica");
        productoActualizado.setStock(100);
        productoActualizado.setDisponible(true);

        // Mocking el comportamiento del repositorio
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoActualizado);

        Producto resultado = productoService.actualizarProducto(1L, productoActualizado);

        assertNotNull(resultado);
        assertEquals("Producto Actualizado", resultado.getNombre());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
    }

    @Test
    void testEliminarProducto() {
        assertDoesNotThrow(() -> productoService.eliminarProducto(1L));
        Mockito.verify(productoRepository).deleteById(1L);
    }
}
