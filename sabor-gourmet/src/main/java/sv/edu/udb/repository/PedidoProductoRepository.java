package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.model.PedidoProducto;

public interface PedidoProductoRepository extends JpaRepository<PedidoProducto, Long> {
}