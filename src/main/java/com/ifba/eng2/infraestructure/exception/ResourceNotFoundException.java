package com.ifba.eng2.infraestructure.exception;

/**
 * Exceção lançada quando um recurso solicitado não for encontrado.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }
}
