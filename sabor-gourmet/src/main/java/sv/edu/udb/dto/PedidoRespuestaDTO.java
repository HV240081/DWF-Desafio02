package sv.edu.udb.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoRespuestaDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime fechaPedido;
    private Double total;
    private String metodoPago;
    private String estado;
    private List<PedidoProductoRespuestaDTO> productos;
}