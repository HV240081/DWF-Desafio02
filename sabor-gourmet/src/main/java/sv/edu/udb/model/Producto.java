package sv.edu.udb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
@Schema(name = "Producto", description = "Información de un producto del menú")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del producto", example = "5")
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio.")
    @Schema(description = "Nombre del producto", example = "Hamburguesa Clásica")
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Deliciosa hamburguesa con carne, queso, lechuga y tomate")
    private String descripcion;

    @NotNull(message = "El precio del producto es obligatorio.")
    @Positive(message = "El precio debe ser positivo.")
    @Schema(description = "Precio del producto", example = "5.50")
    private Double precio;

    @Schema(description = "Categoría del producto", example = "Platos Principales")
    private String categoria;

    @NotNull(message = "El stock del producto es obligatorio.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    @Schema(description = "Cantidad en stock del producto", example = "100")
    private Integer stock;

    @Schema(description = "Indica si el producto está disponible", example = "true")
    private Boolean disponible;

    public Boolean isDisponible() {
        return disponible != null ? disponible : false;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}