package sv.edu.udb.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import sv.edu.udb.model.Cliente;
import sv.edu.udb.repository.ClienteRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Ana López");
        cliente.setEmail("ana@example.com");
        cliente.setTelefono("7777-8888");
        cliente.setDireccion("San Salvador");
    }

    @Test
    void testCrearCliente_Exito() {
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente creado = clienteService.crearCliente(cliente);

        assertNotNull(creado);
        assertTrue(creado.getActivo());
        assertEquals(LocalDate.now(), creado.getFechaRegistro());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testCrearCliente_EmailYaExiste() {
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente(cliente);
        });

        assertEquals("El email ya está registrado.", ex.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void testObtenerTodos() {
        List<Cliente> lista = Arrays.asList(cliente);
        when(clienteRepository.findAll()).thenReturn(lista);

        List<Cliente> resultado = clienteService.obtenerTodos();

        assertEquals(1, resultado.size());
        verify(clienteRepository).findAll();
    }

    @Test
    void testObtenerPorId_Exito() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente encontrado = clienteService.obtenerPorId(1L);

        assertEquals(cliente.getNombre(), encontrado.getNombre());
        verify(clienteRepository).findById(1L);
    }

    @Test
    void testObtenerPorId_NoEncontrado() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            clienteService.obtenerPorId(99L);
        });

        assertEquals("Cliente no encontrado", ex.getMessage());
        verify(clienteRepository).findById(99L);
    }

    @Test
    void testActualizarCliente() {
        Cliente actualizado = new Cliente();
        actualizado.setNombre("Nuevo Nombre");
        actualizado.setTelefono("6666-9999");
        actualizado.setDireccion("Santa Tecla");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente result = clienteService.actualizarCliente(1L, actualizado);

        assertEquals("Nuevo Nombre", result.getNombre());
        assertEquals("6666-9999", result.getTelefono());
        assertEquals("Santa Tecla", result.getDireccion());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testEliminarCliente() {
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.eliminarCliente(1L);

        verify(clienteRepository).deleteById(1L);
    }
}
