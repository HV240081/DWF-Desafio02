// sv.edu.udb.model.PedidoProducto
package sv.edu.udb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Entity
@Data
@Table(name = "pedido_producto")
@Schema(name = "PedidoProducto", description = "Detalle de la relación entre un pedido y un producto")
public class PedidoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del detalle del pedido", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @Schema(description = "Pedido al que pertenece este detalle")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @Schema(description = "Producto incluido en este detalle")
    private Producto producto;

    @NotNull(message = "La cantidad es obligatoria.")
    @Positive(message = "La cantidad debe ser positiva.")
    @Column(nullable = false)
    @Schema(description = "Cantidad del producto en este detalle", example = "1")
    private Integer cantidad;

    @NotNull(message = "El subtotal es obligatorio.")
    @PositiveOrZero(message = "El subtotal no puede ser negativo.")
    @Column(nullable = false)
    @Schema(description = "Subtotal de este producto en el pedido", example = "12.75")
    private Double subtotal;
}