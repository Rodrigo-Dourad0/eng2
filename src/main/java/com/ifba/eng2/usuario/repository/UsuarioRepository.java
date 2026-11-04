package com.ifba.eng2.usuario.repository;

import com.ifba.eng2.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositório Spring Data JPA para gerenciamento e persistência da entidade Usuario.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    /**
     * Busca usuários cujo nome contenha o termo informado, ignorando diferenças entre maiúsculas e minúsculas.
     *
     * @param nome Termo de busca do nome
     * @return Lista de usuários encontrados
     */
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
}
