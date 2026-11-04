package com.ifba.eng2.usuario.controller;

import com.ifba.eng2.usuario.dto.UsuarioRequestDTO;
import com.ifba.eng2.usuario.dto.UsuarioResponseDTO;
import com.ifba.eng2.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST responsável por expor os endpoints da entidade Usuário para consumo via interface web.
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // Habilita comunicação CORS para conectar com interfaces web (React, Vue, Angular, etc.)
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Lista todos os usuários cadastrados ou filtra pelo nome se informado.
     *
     * @param nome Parâmetro de busca opcional por nome
     * @return Lista com os usuários
     */
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(usuarioService.buscarPorNome(nome));
        }
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    /**
     * Busca um usuário específico pelo ID.
     *
     * @param id Identificador UUID do usuário
     * @return Dados do usuário encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    /**
     * Cadastra um novo usuário no sistema.
     *
     * @param request Dados de cadastro do usuário
     * @return Usuário criado com status HTTP 201 Created
     */
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO criado = usuarioService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id Identificador UUID do usuário
     * @param request Novos dados atualizados
     * @return Usuário atualizado com status HTTP 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.ok(usuarioService.atualizar(id, request));
    }

    /**
     * Remove um usuário do sistema pelo ID.
     *
     * @param id Identificador UUID do usuário
     * @return Resposta sem conteúdo com status HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
