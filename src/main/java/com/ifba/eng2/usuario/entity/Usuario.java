package com.ifba.eng2.usuario.entity;

import com.ifba.eng2.infraestructure.entity.PersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidade de domínio que representa um Usuário do sistema,
 * herdando a identificação UUID da classe PersistenceEntity.
 */
@Entity
@Table(name = "tb_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Usuario extends PersistenceEntity {

    /**
     * Nome completo do usuário.
     */
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    /**
     * CPF do usuário (único e obrigatório).
     */
    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    /**
     * E-mail de contato do usuário (único e obrigatório).
     */
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Senha de acesso do usuário (obrigatório).
     */
    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    /**
     * Número de telefone para contato (opcional).
     */
    @Column(name = "telefone", nullable = true, length = 20)
    private String telefone;

    /**
     * Endereço residencial ou comercial do usuário.
     */
    @Column(name = "endereco", nullable = false, length = 255)
    private String endereco;
}
