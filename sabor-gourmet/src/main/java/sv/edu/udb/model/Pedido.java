package sv.edu.udb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Schema(name = "Pedido", description = "Información de un pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del pedido", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @Schema(description = "Cliente que realizó el pedido")
    private Cliente cliente;

    @Column(nullable = false)
    @Schema(description = "Fecha y hora del pedido", example = "2025-04-13T14:30:00")
    private LocalDateTime fechaPedido;

    @NotNull(message = "El total es obligatorio.")
    @Positive(message = "El total debe ser positivo.")
    @Column(nullable = false)
    @Schema(description = "Total a pagar del pedido", example = "25.50")
    private Double total;

    @NotBlank(message = "El método de pago es obligatorio.")
    @Column(nullable = false)
    @Schema(description = "Método de pago del pedido", example = "Efectivo")
    private String metodoPago;

    @NotBlank(message = "El estado del pedido es obligatorio.")
    @Column(nullable = false)
    @Schema(description = "Estado del pedido", example = "ENTREGADO")
    private String estado;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de productos en este pedido")
    private List<PedidoProducto> productos;
}