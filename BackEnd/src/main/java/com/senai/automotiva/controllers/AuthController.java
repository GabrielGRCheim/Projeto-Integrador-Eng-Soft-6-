package com.senai.automotiva.controllers;

import com.senai.automotiva.dtos.AuthDTO;
import com.senai.automotiva.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller de autenticação.
 *
 * <p>Endpoint público — não exige token para ser acessado.</p>
 *
 * <p><b>Fluxo de uso:</b></p>
 * <ol>
 *   <li>Cliente envia POST /api/auth/login com e-mail e senha.</li>
 *   <li>Servidor valida e retorna o token JWT.</li>
 *   <li>Nas próximas requisições, o cliente inclui o header:
 *       {@code Authorization: Bearer <token>}</li>
 * </ol>
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Login e emissão de token JWT")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(
        summary = "Realizar login",
        description = "Autentica o usuário com e-mail e senha. Retorna um token JWT válido por 24h. "
                    + "Use o token no header: Authorization: Bearer {token}"
    )
    public ResponseEntity<AuthDTO.LoginResposta> login(@RequestBody @Valid AuthDTO.LoginRequisicao requisicao) {
        return ResponseEntity.ok(authService.login(requisicao));
    }
}
