package med.voll.api.infra.errores;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // Usado bastante en AOP (Programacion Orientado a Aspectos)
public class TratadorDeErrores {

    // Le pasamos la clase del error como parametro, para que sepa cual error debe tratar.
    // Tratamos error 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404(){
        return ResponseEntity.notFound().build();
    }

    // Tratamos error 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException e){ // El parametro recibido siempre sera la Excepcion que vamos a tratar.
        var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }

    //DTO para errores de Validacion
    private record DatosErrorValidacion(String campo, String error){

        //Constructor para Mapear los Errores al DTO.
        public DatosErrorValidacion(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
