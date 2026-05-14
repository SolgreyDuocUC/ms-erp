package backend.com.gestionUsuarios.usuario.service;

import backend.com.gestionUsuarios.usuario.exceptions.UserDuplicadoException;
import backend.com.gestionUsuarios.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateUniqueness(String email, String run) {
        if (userRepository.existsByUsuarioEmail(email)) {
            throw new UserDuplicadoException("email", email);
        }

        if (userRepository.existsByUsuarioRun(run)) {
            throw new UserDuplicadoException("run", run);
        }
    }
}
