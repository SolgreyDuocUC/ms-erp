package backend.com.adquisiciones.controller;

import backend.com.adquisiciones.application.dto.EmitirOCCommand;
import backend.com.adquisiciones.application.dto.OCResponse;
import backend.com.adquisiciones.application.dto.RecepcionarOCCommand;
import backend.com.adquisiciones.application.usecase.EmitirOCUseCase;
import backend.com.adquisiciones.application.usecase.RecepcionarOCUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/adquisiciones/ordenes-compra")
@RequiredArgsConstructor
@Tag(name = "Adquisiciones - Orden de Compra", description = "Operaciones sobre Ã“rdenes de Compra")
public class OrdenCompraController {

    private final EmitirOCUseCase emitirOCUseCase;
    private final RecepcionarOCUseCase recepcionarOCUseCase;

    @PostMapping
    @Operation(summary = "Emitir Orden de Compra", description = "Emite una OC basada en una SC validada.")
    public ResponseEntity<OCResponse> emitir(@RequestBody EmitirOCCommand command) {
        OCResponse response = emitirOCUseCase.ejecutar(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/recepcionar")
    @Operation(summary = "Recepcionar Orden de Compra", description = "Confirma recepciÃ³n aislando el Aggregate.")
    public ResponseEntity<OCResponse> recepcionar(@PathVariable Long id) {
        RecepcionarOCCommand command = new RecepcionarOCCommand();
        command.setOrdenCompraId(id);
        OCResponse response = recepcionarOCUseCase.ejecutar(command);
        return ResponseEntity.ok(response);
    }
}
