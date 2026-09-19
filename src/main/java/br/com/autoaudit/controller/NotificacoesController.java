package br.com.autoaudit.controller;

import br.com.autoaudit.model.Notificacao;
import br.com.autoaudit.service.NotificacaoService;

import java.util.List;

public class NotificacoesController {

    private final NotificacaoService notificacaoService =
            new NotificacaoService();

    // Carrega todas as notificações do sistema.
    public List<Notificacao> carregarNotificacoes() {
        return notificacaoService.listarTodos();
    }

    // Busca uma notificação específica.
    public Notificacao buscarPorId(Long id) {
        return notificacaoService.buscarPorId(id);
    }
}