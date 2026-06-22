package com.campus.foodtruck.infrastructure.adapters.in.web;

import com.campus.foodtruck.domain.model.Rol;
import com.campus.foodtruck.domain.model.Usuario;
import com.campus.foodtruck.domain.ports.in.UsuarioUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioUseCase usuarioUseCase;

    public AuthController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroDto dto) {
        try {
            Usuario usuario = Usuario.builder()
                    .nombre(dto.getNombre())
                    .email(dto.getEmail())
                    .password(dto.getPassword())
                    .rol(dto.getRol())
                    .build();
            Usuario registrado = usuarioUseCase.registrar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(registrado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto dto) {
        try {
            String token = usuarioUseCase.login(dto.getEmail(), dto.getPassword());
            Usuario usuario = usuarioUseCase.obtenerPorEmail(dto.getEmail());
            
            LoginRespuestaDto respuesta = new LoginRespuestaDto();
            respuesta.setToken(token);
            respuesta.setId(usuario.getId());
            respuesta.setEmail(usuario.getEmail());
            respuesta.setNombre(usuario.getNombre());
            respuesta.setRol(usuario.getRol().name());
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @Data
    public static class RegistroDto {
        @NotBlank(message = "El nombre es obligatorio")
        private String nombre;

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe ser un email valido")
        private String email;

        @NotBlank(message = "La contrasena es obligatoria")
        private String password;

        @NotNull(message = "El rol es obligatorio")
        private Rol rol;
    }

    @Data
    public static class LoginDto {
        @NotBlank(message = "El email es obligatorio")
        private String email;

        @NotBlank(message = "La contrasena es obligatoria")
        private String password;
    }

    @Data
    public static class LoginRespuestaDto {
        private String token;
        private Long id;
        private String email;
        private String nombre;
        private String rol;
    }
}
