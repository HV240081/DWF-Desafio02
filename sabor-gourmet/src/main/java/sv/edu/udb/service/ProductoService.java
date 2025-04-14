package sv.edu.udb.service;

import jakarta.validation.Valid;
import sv.edu.udb.model.Producto;

import java.util.List;

public interface ProductoService {
    Producto crearProducto(@Valid Producto producto);
    List<Producto> obtenerTodos();
    Producto obtenerPorId(Long id);
    Producto actualizarProducto(Long id, @Valid Producto producto);
    void eliminarProducto(Long id);
}