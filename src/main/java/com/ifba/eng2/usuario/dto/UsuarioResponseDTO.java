package com.ifba.eng2.usuario.dto;

import com.ifba.eng2.usuario.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Objeto de transferência de dados (DTO) para envio de dados do Usuário à interface web.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private UUID id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String endereco;

    /**
     * Mapeia uma entidade Usuario para o DTO de resposta omitindo a senha.
     *
     * @param usuario Entidade do usuário
     * @return DTO correspondente ou null caso o usuário seja nulo
     */
    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .cpf(usuario.getCpf())
                .email(usuario.getEmail())
                .telefone(usuario.getTelefone())
                .endereco(usuario.getEndereco())
                .build();
    }
}
