package infernape.api;

import infernape.domain.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
}
