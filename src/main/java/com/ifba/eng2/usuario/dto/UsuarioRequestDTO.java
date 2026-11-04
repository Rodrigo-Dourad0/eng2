package com.ifba.eng2.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferência de dados (DTO) para criação e atualização de Usuário.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 150, message = "O nome deve conter entre 2 e 150 caracteres")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório")
    @Size(max = 14, message = "O CPF deve conter no máximo 14 caracteres")
    private String cpf;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    @Size(max = 20, message = "O telefone deve conter no máximo 20 caracteres")
    private String telefone;

    @NotBlank(message = "O endereço é obrigatório")
    @Size(min = 3, max = 255, message = "O endereço deve conter entre 3 e 255 caracteres")
    private String endereco;
}
