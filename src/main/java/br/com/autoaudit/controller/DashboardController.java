package br.com.autoaudit.controller;

import br.com.autoaudit.enums.StatusAuditoria;
import br.com.autoaudit.model.Auditoria;
import br.com.autoaudit.model.Notificacao;
import br.com.autoaudit.model.Usuario;
import br.com.autoaudit.service.AuditoriaService;
import br.com.autoaudit.service.NotificacaoService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    private final AuditoriaService auditoriaService =
            new AuditoriaService();

    private final NotificacaoService notificacaoService =
            new NotificacaoService();

    private Usuario usuarioLogado;

    // Define o usuário atualmente autenticado no sistema.
    public void definirUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    // Retorna o usuário atualmente autenticado.
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    // Carrega as auditorias ainda agendadas,
    // ordenadas da mais próxima para a mais distante.
    public List<Auditoria> carregarAuditoriasARealizar() {
        return auditoriaService.listarTodos()
                .stream()
                .filter(auditoria ->
                        auditoria.getStatus() == StatusAuditoria.AGENDADA)
                .sorted(
                        Comparator.comparing(
                                Auditoria::getDataHoraAgendada,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        )
                )
                .collect(Collectors.toList());
    }

    // Carrega as auditorias já finalizadas para o histórico.
    public List<Auditoria> carregarHistorico() {
        return auditoriaService.listarTodos()
                .stream()
                .filter(auditoria ->
                        auditoria.getStatus() == StatusAuditoria.FINALIZADA)
                .sorted(
                        Comparator.comparing(
                                Auditoria::getFim,
                                Comparator.nullsLast(Comparator.reverseOrder())
                        )
                )
                .collect(Collectors.toList());
    }

    // Carrega as notificações do sistema.
    public List<Notificacao> carregarNotificacoes() {
        return notificacaoService.listarTodos();
    }
}