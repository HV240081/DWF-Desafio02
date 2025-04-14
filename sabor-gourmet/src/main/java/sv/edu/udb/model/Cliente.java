package sv.edu.udb.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Schema(name = "Cliente", description = "Información de un cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del cliente", example = "1")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio.")
    @Schema(description = "Nombre del cliente", example = "Juan Pérez")
    private String nombre;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El email debe ser válido.")
    @Column(unique = true)
    @Schema(description = "Email del cliente", example = "juan.perez@example.com")
    private String email;

    @Schema(description = "Número de teléfono del cliente", example = "1234-5678")
    private String telefono;

    @Schema(description = "Dirección del cliente", example = "Calle Principal #123")
    private String direccion;

    @Schema(description = "Fecha de registro del cliente", example = "2024-01-15")
    private LocalDate fechaRegistro;

    @Schema(description = "Estado de actividad del cliente", example = "true")
    private Boolean activo = true;
}