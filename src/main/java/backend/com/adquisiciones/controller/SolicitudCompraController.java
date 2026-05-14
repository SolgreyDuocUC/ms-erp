package backend.com.adquisiciones.controller;

import backend.com.adquisiciones.application.dto.CrearSCCommand;
import backend.com.adquisiciones.application.dto.SCResponse;
import backend.com.adquisiciones.application.usecase.CrearSCUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/adquisiciones/solicitudes-compra")
@RequiredArgsConstructor
@Tag(name = "Adquisiciones - Solicitud de Compra", description = "Operaciones sobre Solicitudes de Compra")
public class SolicitudCompraController {

    private final CrearSCUseCase crearSCUseCase;
    private final backend.com.adquisiciones.domain.ports.SolicitudCompraRepository scRepository;

    @GetMapping
    @Operation(summary = "Listar todas las SC", description = "Retorna todas las solicitudes de compra.")
    public ResponseEntity<java.util.List<SCResponse>> getAll() {
        java.util.List<SCResponse> list = scRepository.findAll().stream()
                .map(SCResponse::fromDomain)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/next-number")
    public Long getNextNumber() {
        return scRepository.findMaxNumero().orElse(0L) + 1;
    }

    @PostMapping
    @Operation(summary = "Crear Solicitud de Compra", description = "Crea una SC aislada mediante DDD.")
    public ResponseEntity<SCResponse> crear(@RequestBody CrearSCCommand command) {
        SCResponse response = crearSCUseCase.ejecutar(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
