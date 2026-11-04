package com.ifba.eng2.usuario.service;

import com.ifba.eng2.infraestructure.exception.ResourceNotFoundException;
import com.ifba.eng2.usuario.dto.UsuarioRequestDTO;
import com.ifba.eng2.usuario.dto.UsuarioResponseDTO;
import com.ifba.eng2.usuario.entity.Usuario;
import com.ifba.eng2.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes unitários da camada UsuarioService isolando o banco de dados com Mockito.
 */
// [Anotação 1/5]: @ExtendWith - habilita o suporte do Mockito no JUnit 5
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    // [Anotação 2/5]: @Mock - cria um dublê simulado do repositório (sem acesso ao banco real)
    @Mock
    private UsuarioRepository usuarioRepository;

    // [Anotação 3/5]: @InjectMocks - instancia UsuarioService injetando o mock do repositório nela
    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRequestDTO usuarioRequestDTO;
    private UUID idUsuario;

    // [Anotação 4/5]: @BeforeEach - executa antes de cada teste, garantindo dados limpos e independentes
    @BeforeEach
    void setUp() {
        idUsuario = UUID.randomUUID();

        usuario = Usuario.builder()
                .nome("Carlos Silva")
                .cpf("123.456.789-00")
                .email("carlos@teste.com")
                .senha("123456")
                .telefone("71999998888")
                .endereco("Rua das Acácias, 123")
                .build();
        usuario.setId(idUsuario);

        usuarioRequestDTO = UsuarioRequestDTO.builder()
                .nome("Carlos Silva")
                .cpf("123.456.789-00")
                .email("carlos@teste.com")
                .senha("123456")
                .telefone("71999998888")
                .endereco("Rua das Acácias, 123")
                .build();
    }

    // [Anotação 5/5]: @Test (marca método de teste) e @DisplayName (nome descritivo para relatórios)
    @Test
    @DisplayName("Deve criar e salvar um novo usuário com sucesso")
    void deveSalvarUsuarioComSucesso() {
        // Arrange: simula o save do repositório retornando a entidade com ID
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act: executa a regra de criação no serviço
        UsuarioResponseDTO resposta = usuarioService.criar(usuarioRequestDTO);

        // [Asserção 1/5]: assertNotNull - garante que o objeto e o ID não são nulos
        assertNotNull(resposta);
        assertNotNull(resposta.getId());

        // [Asserção 2/5]: assertEquals - compara se o valor esperado é igual ao retornado
        assertEquals(usuario.getNome(), resposta.getNome());
        assertEquals(usuario.getCpf(), resposta.getCpf());
        assertEquals(usuario.getEmail(), resposta.getEmail());
        assertEquals(usuario.getTelefone(), resposta.getTelefone());
        assertEquals(usuario.getEndereco(), resposta.getEndereco());

        // Verify: confirma que save() foi chamado exatamente 1 vez
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve atualizar usuário existente com sucesso")
    void deveAtualizarUsuarioComSucesso() {
        // Arrange: simula busca e save
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act: executa atualização
        UsuarioResponseDTO resposta = usuarioService.atualizar(idUsuario, usuarioRequestDTO);

        // Assert: valida dados atualizados
        assertNotNull(resposta);
        assertEquals(usuario.getNome(), resposta.getNome());
        assertEquals(usuario.getCpf(), resposta.getCpf());
        assertEquals(usuario.getEmail(), resposta.getEmail());
        assertEquals(usuario.getTelefone(), resposta.getTelefone());
        assertEquals(usuario.getEndereco(), resposta.getEndereco());

        verify(usuarioRepository, times(1)).findById(idUsuario);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {
        // Arrange: simula que o usuário foi localizado no banco
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));

        // Act: consulta o usuário pelo UUID
        UsuarioResponseDTO resposta = usuarioService.buscarPorId(idUsuario);

        // Assert: valida os dados retornados
        assertNotNull(resposta);
        assertEquals(idUsuario, resposta.getId());
        assertEquals("Carlos Silva", resposta.getNome());
        assertEquals(usuario.getCpf(), resposta.getCpf());
        assertEquals(usuario.getEmail(), resposta.getEmail());

        verify(usuarioRepository, times(1)).findById(idUsuario);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar ID inexistente")
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        UUID idInexistente = UUID.randomUUID();
        // Arrange: simula busca sem resultado
        when(usuarioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // [Asserção 5/5]: assertThrows - valida que a exceção esperada é disparada ao executar a ação
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.buscarPorId(idInexistente)
        );

        // [Asserção 3/5]: assertTrue - valida que a mensagem de erro contém o texto esperado
        assertTrue(exception.getMessage().contains("Usuário não encontrado com o ID"));

        verify(usuarioRepository, times(1)).findById(idInexistente);
    }

    @Test
    @DisplayName("Deve buscar usuários por nome e validar conteúdo da lista")
    void deveBuscarUsuariosPorNomeComSucesso() {
        // Arrange: simula o retorno de usuários que contenham o termo "Carlos"
        when(usuarioRepository.findByNomeContainingIgnoreCase("Carlos")).thenReturn(List.of(usuario));

        // Act: executa a busca por nome
        List<UsuarioResponseDTO> resultado = usuarioService.buscarPorNome("Carlos");

        // [Asserção 3/5]: assertTrue - valida que a lista contém o usuário pesquisado
        assertTrue(resultado.stream().anyMatch(u -> u.getNome().contains("Carlos")));

        // [Asserção 4/5]: assertFalse - valida que a lista não veio vazia
        assertFalse(resultado.isEmpty());

        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findByNomeContainingIgnoreCase("Carlos");
    }

    @Test
    @DisplayName("Deve deletar usuário existente com sucesso")
    void deveDeletarUsuarioComSucesso() {
        // Arrange: simula que o usuário existe para exclusão
        when(usuarioRepository.existsById(idUsuario)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(idUsuario);

        // Act: executa a exclusão
        usuarioService.deletar(idUsuario);

        // Verify: confirma que checou existência e chamou a deleção
        verify(usuarioRepository, times(1)).existsById(idUsuario);
        verify(usuarioRepository, times(1)).deleteById(idUsuario);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao tentar deletar usuário inexistente")
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        UUID idInexistente = UUID.randomUUID();
        // Arrange: simula que o usuário não existe
        when(usuarioRepository.existsById(idInexistente)).thenReturn(false);

        // Assert: valida lançamento de exceção ao tentar deletar
        assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.deletar(idInexistente)
        );

        verify(usuarioRepository, times(1)).existsById(idInexistente);
        verify(usuarioRepository, times(0)).deleteById(idInexistente);
    }
}
