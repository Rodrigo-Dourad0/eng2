package com.ifba.eng2.usuario.service;

import com.ifba.eng2.infraestructure.exception.ResourceNotFoundException;
import com.ifba.eng2.usuario.dto.UsuarioRequestDTO;
import com.ifba.eng2.usuario.dto.UsuarioResponseDTO;
import com.ifba.eng2.usuario.entity.Usuario;
import com.ifba.eng2.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Camada de serviço responsável pelas regras de negócio e orquestração de operações com Usuários.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Retorna a lista de todos os usuários cadastrados.
     *
     * @return Lista de UsuarioResponseDTO
     */
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Busca um usuário pelo seu identificador único UUID.
     *
     * @param id Identificador do usuário
     * @return DTO com os dados do usuário encontrado
     * @throws ResourceNotFoundException se o usuário não for localizado
     */
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(UUID id) {
        Usuario usuario = buscarEntidadePorId(id);
        return UsuarioResponseDTO.fromEntity(usuario);
    }

    /**
     * Busca usuários pelo nome (pesquisa parcial, sem diferenciar maiúsculas/minúsculas).
     *
     * @param nome Termo ou parte do nome
     * @return Lista de usuários encontrados
     */
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    /**
     * Cria e persiste um novo usuário no banco de dados.
     *
     * @param request Dados de cadastro do usuário
     * @return DTO do usuário recém-criado
     */
    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO request) {
        Usuario usuario = Usuario.builder()
                .nome(request.getNome().trim())
                .cpf(request.getCpf().trim())
                .email(request.getEmail().trim())
                .senha(request.getSenha())
                .telefone(request.getTelefone() != null && !request.getTelefone().isBlank() ? request.getTelefone().trim() : null)
                .endereco(request.getEndereco().trim())
                .build();

        Usuario salvo = usuarioRepository.save(usuario);
        return UsuarioResponseDTO.fromEntity(salvo);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id Identificador do usuário a ser atualizado
     * @param request Novos dados do usuário
     * @return DTO do usuário atualizado
     */
    @Transactional
    public UsuarioResponseDTO atualizar(UUID id, UsuarioRequestDTO request) {
        Usuario usuario = buscarEntidadePorId(id);

        usuario.setNome(request.getNome().trim());
        usuario.setCpf(request.getCpf().trim());
        usuario.setEmail(request.getEmail().trim());
        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            usuario.setSenha(request.getSenha());
        }
        usuario.setTelefone(request.getTelefone() != null && !request.getTelefone().isBlank() ? request.getTelefone().trim() : null);
        usuario.setEndereco(request.getEndereco().trim());

        Usuario atualizado = usuarioRepository.save(usuario);
        return UsuarioResponseDTO.fromEntity(atualizado);
    }

    /**
     * Remove um usuário a partir do seu identificador único.
     *
     * @param id Identificador do usuário a ser removido
     */
    @Transactional
    public void deletar(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado para exclusão com o ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    /**
     * Método auxiliar privado para buscar a entidade ou lançar exceção padronizada.
     *
     * @param id Identificador do usuário
     * @return Entidade Usuario
     */
    private Usuario buscarEntidadePorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
    }
}
