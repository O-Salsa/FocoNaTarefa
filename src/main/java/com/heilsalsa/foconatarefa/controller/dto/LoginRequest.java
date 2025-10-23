package com.heilsalsa.foconatarefa.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "O usuário é obrigatório") String usuario,
        @NotBlank(message = "A senha é obrigatória") String senha
) {
}
