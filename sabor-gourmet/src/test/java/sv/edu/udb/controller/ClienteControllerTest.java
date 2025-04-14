package sv.edu.udb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.model.Cliente;
import sv.edu.udb.service.ClienteService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setTelefono("1234-5678");
        cliente.setDireccion("Calle Principal #123");
        cliente.setFechaRegistro(LocalDate.of(2024, 1, 15));
        cliente.setActivo(true);
    }

    @Test
    void testCrearCliente_Exito() throws Exception {
        when(clienteService.crearCliente(ArgumentMatchers.any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated()) // Debería ser 201 Created
                .andExpect(jsonPath("$.nombre", is(cliente.getNombre())))
                .andExpect(jsonPath("$.email", is(cliente.getEmail())));
    }

    @Test
    void testCrearCliente_EmailDuplicado() throws Exception {
        when(clienteService.crearCliente(ArgumentMatchers.any(Cliente.class))).thenThrow(new DataIntegrityViolationException("Email ya registrado"));

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isConflict()) // Debería ser 409 Conflict
                .andExpect(content().string(containsString("El email ya está registrado.")));
    }

    @Test
    void testCrearCliente_ValidacionFalla() throws Exception {
        Cliente clienteInvalido = new Cliente(); // Sin nombre ni email

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre", is("El nombre es obligatorio.")))
                .andExpect(jsonPath("$.email", is("El email es obligatorio.")));
    }

    @Test
    void testObtenerTodos() throws Exception {
        List<Cliente> lista = Arrays.asList(cliente);
        when(clienteService.obtenerTodos()).thenReturn(lista);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].email", is(cliente.getEmail())));
    }

    @Test
    void testObtenerPorId_Exito() throws Exception {
        when(clienteService.obtenerPorId(1L)).thenReturn(cliente);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(cliente.getNombre())));
    }

    @Test
    void testObtenerPorId_NotFound() throws Exception {
        when(clienteService.obtenerPorId(1L)).thenThrow(new RuntimeException("Cliente no encontrado"));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarCliente_Exito() throws Exception {
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId(1L);
        clienteActualizado.setNombre("Carlos López");
        clienteActualizado.setEmail("carlos.lopez@example.com");
        clienteActualizado.setTelefono("9876-5432");
        clienteActualizado.setDireccion("Avenida Central #456");
        clienteActualizado.setFechaRegistro(LocalDate.of(2024, 2, 20));
        clienteActualizado.setActivo(true);

        when(clienteService.actualizarCliente(eq(1L), ArgumentMatchers.any(Cliente.class))).thenReturn(clienteActualizado);

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Carlos López")))
                .andExpect(jsonPath("$.email", is("carlos.lopez@example.com")));
    }

    @Test
    void testActualizarCliente_NotFound() throws Exception {
        when(clienteService.actualizarCliente(eq(1L), ArgumentMatchers.any(Cliente.class))).thenThrow(new RuntimeException("Cliente no encontrado"));

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarCliente_ValidacionFalla() throws Exception {
        Cliente clienteInvalido = new Cliente(); // Sin nombre ni email

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre", is("El nombre es obligatorio.")))
                .andExpect(jsonPath("$.email", is("El email es obligatorio.")));
    }

    @Test
    void testEliminarCliente_Exito() throws Exception {
        doNothing().when(clienteService).eliminarCliente(1L);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarCliente_NotFound() throws Exception {
        doThrow(new RuntimeException("Cliente no encontrado")).when(clienteService).eliminarCliente(1L);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNotFound());
    }
}