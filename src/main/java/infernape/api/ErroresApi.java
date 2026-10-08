package infernape.api;

import infernape.domain.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ErroresApi {
    public record ErrorRespuesta(String codigo, String mensaje) { }
    @ExceptionHandler(ReglaOperacionException.class)
    public ResponseEntity<ErrorRespuesta> regla(ReglaOperacionException error) {
        var status = error.codigo() == CodigoRechazo.MISION_DUPLICADA ? HttpStatus.CONFLICT : HttpStatus.UNPROCESSABLE_ENTITY;
        return ResponseEntity.status(status).body(new ErrorRespuesta(error.codigo().name(), error.getMessage()));
    }
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorRespuesta> datos(Exception error) {
        return ResponseEntity.badRequest().body(new ErrorRespuesta("DATOS_INVALIDOS", "Revise los campos y el formato de la solicitud."));
    }
    @ExceptionHandler({IllegalStateException.class, DataAccessException.class})
    public ResponseEntity<ErrorRespuesta> dependencia(Exception error) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ErrorRespuesta("DEPENDENCIA_NO_DISPONIBLE", "No se pudo verificar o registrar la operacion. Reintente mas tarde."));
    }
}
