package infernape.api;

import infernape.application.IniciadorMision;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3/misiones")
public class MisionesController {
    private final IniciadorMision iniciador;
    public MisionesController(IniciadorMision iniciador) { this.iniciador = iniciador; }
    @PostMapping public ResponseEntity<MisionResponse> crear(@Valid @RequestBody MisionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(MisionResponse.desde(iniciador.iniciar(request.aDominio())));
    }
}
