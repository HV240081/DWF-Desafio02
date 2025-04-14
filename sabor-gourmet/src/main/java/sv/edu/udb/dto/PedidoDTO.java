package sv.edu.udb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
@Schema(name = "PedidoDTO", description = "Objeto para crear un nuevo pedido")
public class PedidoDTO {

    @NotNull(message = "El ID del cliente es obligatorio.")
    @Schema(description = "ID del cliente que realiza el pedido", example = "1")
    private Long clienteId;

    @NotBlank(message = "El método de pago es obligatorio.")
    @Size(min = 2, max = 50, message = "El método de pago debe tener entre 2 y 50 caracteres.")
    @Schema(description = "Método de pago del pedido", example = "Tarjeta de crédito")
    private String metodoPago;

    @NotBlank(message = "El estado del pedido es obligatorio.")
    @Size(min = 2, max = 20, message = "El estado del pedido debe tener entre 2 y 20 caracteres.")
    @Schema(description = "Estado del pedido", example = "PENDIENTE", allowableValues = {"PENDIENTE", "EN_PROCESO", "ENVIADO", "ENTREGADO", "CANCELADO"})
    private String estado;

    @Valid
    @Size(min = 1, message = "El pedido debe contener al menos un producto.")
    @Schema(description = "Lista de productos en el pedido")
    private List<PedidoDetalleDTO> productos;
}