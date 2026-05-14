package backend.com.comercial.controller;

import backend.com.comercial.service.dto.CrearEVNCommand;
import backend.com.comercial.service.dto.EVNResponse;
import backend.com.comercial.service.CrearEVNUseCase;
import backend.com.comercial.service.AdjudicarEVNUseCase;
import backend.com.comercial.domain.ports.EvaluacionNegocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comercial/evaluaciones-negocio")
@RequiredArgsConstructor
public class EvaluacionNegocioController {

    private final CrearEVNUseCase crearEVNUseCase;
    private final AdjudicarEVNUseCase adjudicarEVNUseCase;
    private final EvaluacionNegocioRepository repository;

    @GetMapping
    public ResponseEntity<java.util.List<EVNResponse>> listar() {
        return ResponseEntity.ok(repository.findAll().stream()
                .map(EVNResponse::fromDomain)
                .collect(java.util.stream.Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<EVNResponse> crear(@RequestBody CrearEVNCommand command) {
        return ResponseEntity.ok(crearEVNUseCase.ejecutar(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EVNResponse> obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(EVNResponse::fromDomain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/adjudicar")
    public ResponseEntity<EVNResponse> adjudicar(@PathVariable Long id) {
        return ResponseEntity.ok(adjudicarEVNUseCase.ejecutar(id));
    }
}
