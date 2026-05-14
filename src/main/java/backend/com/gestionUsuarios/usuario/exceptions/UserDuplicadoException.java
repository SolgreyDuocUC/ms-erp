package backend.com.gestionUsuarios.usuario.exceptions;

import backend.com.gestionUsuarios.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserDuplicadoException extends BusinessException {

    public UserDuplicadoException(String campo, String valor) {
        super("Ya existe un usuario con " + campo + ": " + valor, HttpStatus.CONFLICT);
    }
}
