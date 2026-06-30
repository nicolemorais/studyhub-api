package br.com.studyhub.auth.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testes do Domínio")
class UsuarioTest {

    @Test
    @DisplayName("Deve instanciar um usuário válido com estado inicial correto")
    void deveCriarUsuarioValido() {

        Usuario usuario = new Usuario("Luciano Silva","professor@studyhub.com", "senhaCriptografada123", PerfilUsuario.PROFESSOR);

        assertNotNull(usuario);
        assertEquals("professor@studyhub.com", usuario.getEmail());
        assertEquals("senhaCriptografada123", usuario.getSenha());
        assertEquals(PerfilUsuario.PROFESSOR, usuario.getPerfil());
        assertTrue(usuario.isAtivo(), "O usuário deve nascer ativo por padrão de negócio.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar usuário com e-mail inválido ou nulo")
    void deveLancarExcecaoParaEmailInvalido() {
        IllegalArgumentException excecaoNulo = assertThrows(IllegalArgumentException.class, () -> {
             new Usuario("Luciano Silva","professor.com", "senhaCriptografada123", PerfilUsuario.PROFESSOR);

        });
        assertEquals("O e-mail é obrigatório.", excecaoNulo.getMessage());
        IllegalArgumentException excecaoVazio = assertThrows(IllegalArgumentException.class, () -> {
             new Usuario("Luciano Silva"," ", "senhaCriptografada123", PerfilUsuario.PROFESSOR);

        });
        assertEquals("O e-mail é obrigatório.", excecaoVazio.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar usuário com senha inválida ou nula")
    void deveLancarExcecaoParaSenhaInvalida() {
        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
           new Usuario("Luciano Silva","professor@studyhub.com", " ", PerfilUsuario.PROFESSOR);
        });
        assertEquals("A senha é obrigatória.", excecao.getMessage());
    }

    @Test
    @DisplayName("Deve desativar e reativar o acesso do usuário modificando o estado corretamente")
    void deveAlterarEstadoAtivoEDesativo() {

       Usuario usuario = new Usuario("Luciano Silva","professor@studyhub.com", "senhaCriptografada123", PerfilUsuario.PROFESSOR);

        usuario.desativarAcesso();

        assertFalse(usuario.isAtivo(), "O usuário deveria estar inativo após desativarAcesso().");

        usuario.reativarAcesso();

        assertTrue(usuario.isAtivo(), "O usuário deveria estar ativo novamente após reativarAcesso().");
    }

    @Test
    @DisplayName("Deve alterar a senha do usuário com sucesso quando fornecido um hash válido")
    void deveAlterarSenhaComSucesso() {

        Usuario usuario = new Usuario("Luciano Silva","professor@studyhub.com", "senhaAntigaHash", PerfilUsuario.PROFESSOR);

        // Act
        usuario.alterarSenha("novaSenhaCriptografadaHash");

        assertEquals("novaSenhaCriptografadaHash", usuario.getSenha());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar a senha para um valor em branco ou nulo")
    void deveLancarExcecaoAoAlterarSenhaInvalida() {

        Usuario usuario = new Usuario("Luciano Silva", "professor@studyhub.com", "senhaAntigaHash", PerfilUsuario.PROFESSOR);

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            usuario.alterarSenha("   ");
        });
        assertEquals("A nova senha não pode ser vazia.", excecao.getMessage());
    }
}