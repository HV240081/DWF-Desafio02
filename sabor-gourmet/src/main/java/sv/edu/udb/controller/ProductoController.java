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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sv.edu.udb.model.Producto;
import sv.edu.udb.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con la gestión de productos del menú")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @Operation(summary = "Registrar un nuevo producto")
    @ApiResponse(responseCode = "200", description = "Producto registrado exitosamente",
            content = @Content(schema = @Schema(implementation = Producto.class)))
    @ApiResponse(responseCode = "400", description = "Datos del producto inválidos")
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody @Valid @Schema(description = "Datos del nuevo producto") Producto producto) {
        return ResponseEntity.ok(productoService.crearProducto(producto));
    }

    @Operation(summary = "Obtener la lista de todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Producto.class))))
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @Operation(summary = "Obtener un producto por su ID")
    @ApiResponse(responseCode = "200", description = "Producto encontrado",
            content = @Content(schema = @Schema(implementation = Producto.class)))
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@Parameter(description = "ID del producto a buscar", example = "5") @PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @Operation(summary = "Actualizar la información de un producto existente")
    @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = Producto.class)))
    @ApiResponse(responseCode = "400", description = "Datos del producto inválidos")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar", example = "5") @PathVariable Long id,
            @RequestBody @Valid @Schema(description = "Nuevos datos del producto") Producto producto) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, producto));
    }

    @Operation(summary = "Eliminar un producto por su ID")
    @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@Parameter(description = "ID del producto a eliminar", example = "5") @PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}