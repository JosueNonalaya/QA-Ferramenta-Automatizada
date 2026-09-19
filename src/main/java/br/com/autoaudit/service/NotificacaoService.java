package br.com.autoaudit.service;

import br.com.autoaudit.dao.NotificacaoDAO;
import br.com.autoaudit.model.Notificacao;

import java.util.List;

public class NotificacaoService {

    private final NotificacaoDAO notificacaoDAO =
            new NotificacaoDAO();

    // Salva uma nova notificação.
    public void salvar(Notificacao notificacao) {
        notificacaoDAO.salvar(notificacao);
    }

    // Busca uma notificação pelo ID.
    public Notificacao buscarPorId(Long id) {
        return notificacaoDAO.buscarPorId(id);
    }

    // Retorna todas as notificações.
    public List<Notificacao> listarTodos() {
        return notificacaoDAO.listarTodos();
    }

    // Atualiza uma notificação existente.
    public void atualizar(Notificacao notificacao) {
        notificacaoDAO.atualizar(notificacao);
    }

    // Exclui uma notificação pelo ID.
    public void excluir(Long id) {
        notificacaoDAO.excluir(id);
    }
}