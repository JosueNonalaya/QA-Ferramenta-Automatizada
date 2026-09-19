package br.com.autoaudit.controller;

import br.com.autoaudit.model.Usuario;
import br.com.autoaudit.service.UsuarioService;

public class ConfiguracoesController {

    private final UsuarioService usuarioService =
            new UsuarioService();

    private Usuario usuario;

    // Carrega o usuário que terá seus dados alterados.
    public void carregarUsuario(Long usuarioId) {

        usuario = usuarioService.buscarPorId(
                usuarioId
        );

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "Usuário não encontrado."
            );
        }
    }

    // Retorna o usuário carregado.
    public Usuario getUsuario() {
        return usuario;
    }

    // Modifica o nome do usuário.
    public void alterarNome(String nome) {
        usuario.setNome(nome);
    }

    // Modifica o e-mail do usuário.
    public void alterarEmail(String email) {
        usuario.setEmail(email);
    }

    // Modifica o telefone do usuário.
    public void alterarTelefone(String telefone) {
        usuario.setTelefone(telefone);
    }

    // Modifica a senha do usuário.
    public void alterarSenha(String senha) {
        usuario.setSenha(senha);
    }

    // Salva as alterações dos dados do usuário.
    public void salvarAlteracoes() {
        usuarioService.atualizar(usuario);
    }
}