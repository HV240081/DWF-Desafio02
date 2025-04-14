package sv.edu.udb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(name = "PedidoDetalleDTO", description = "Detalle de un producto dentro de un pedido")
public class PedidoDetalleDTO {

    @NotNull(message = "El ID del producto es obligatorio.")
    @Schema(description = "ID del producto", example = "1")
    private Long productoId;

    @Positive(message = "La cantidad debe ser positiva.")
    @Schema(description = "Cantidad del producto", example = "2")
    private Integer cantidad;
}