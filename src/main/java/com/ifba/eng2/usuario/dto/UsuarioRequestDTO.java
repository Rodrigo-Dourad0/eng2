package com.ifba.eng2.usuario.dto;

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

    @NotBlank(message = "O telefone é obrigatório")
    @Size(min = 8, max = 20, message = "O telefone deve conter entre 8 e 20 caracteres")
    private String telefone;

    @NotBlank(message = "O endereço é obrigatório")
    @Size(min = 3, max = 255, message = "O endereço deve conter entre 3 e 255 caracteres")
    private String endereco;
}
