package sv.edu.udb.dto;

import lombok.Data;

@Data
public class PedidoProductoRespuestaDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private Double subtotal;
}