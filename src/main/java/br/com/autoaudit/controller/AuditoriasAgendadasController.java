package br.com.autoaudit.controller;

import br.com.autoaudit.enums.StatusAuditoria;
import br.com.autoaudit.model.Auditoria;
import br.com.autoaudit.service.AuditoriaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AuditoriasAgendadasController {

    private final AuditoriaService auditoriaService =
            new AuditoriaService();

    // Retorna somente as auditorias atualmente agendadas.
    public List<Auditoria> carregarAuditorias() {
        return auditoriaService.listarTodos()
                .stream()
                .filter(auditoria ->
                        auditoria.getStatus() == StatusAuditoria.AGENDADA)
                .collect(Collectors.toList());
    }

    // Cancela uma auditoria agendada.
    public void cancelar(Long auditoriaId) {
        auditoriaService.cancelar(auditoriaId);
    }

    // Altera a data e hora de uma auditoria.
    public void alterarData(
            Long auditoriaId,
            LocalDateTime novaData
    ) {
        auditoriaService.alterarDataAgendada(
                auditoriaId,
                novaData
        );
    }

    // Inicia uma auditoria que será realizada.
    public void realizar(Long auditoriaId) {
        auditoriaService.iniciar(auditoriaId);
    }

    // Busca uma auditoria específica.
    public Auditoria buscarPorId(Long auditoriaId) {
        return auditoriaService.buscarPorId(auditoriaId);
    }
}