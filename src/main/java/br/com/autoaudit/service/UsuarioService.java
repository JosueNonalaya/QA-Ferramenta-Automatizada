package br.com.autoaudit.service;

import br.com.autoaudit.dao.UsuarioDAO;
import br.com.autoaudit.model.Usuario;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Salva um novo usuário no banco.
    public void salvar(Usuario usuario) {
        usuarioDAO.salvar(usuario);
    }

    // Busca um usuário pelo seu ID.
    public Usuario buscarPorId(Long id) {
        return usuarioDAO.buscarPorId(id);
    }

    // Retorna todos os usuários cadastrados.
    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    // Atualiza os dados de um usuário existente.
    public void atualizar(Usuario usuario) {
        usuarioDAO.atualizar(usuario);
    }

    // Exclui um usuário pelo ID.
    public void excluir(Long id) {
        usuarioDAO.excluir(id);
    }
}