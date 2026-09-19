package br.com.autoaudit.service;

import br.com.autoaudit.dao.AuditoriaDAO;
import br.com.autoaudit.model.Auditoria;

import java.time.LocalDateTime;
import java.util.List;

public class AuditoriaService {

    private final AuditoriaDAO auditoriaDAO = new AuditoriaDAO();

    // Salva uma nova auditoria no banco.
    public void salvar(Auditoria auditoria) {
        auditoriaDAO.salvar(auditoria);
    }

    // Busca uma auditoria pelo ID.
    public Auditoria buscarPorId(Long id) {
        return auditoriaDAO.buscarPorId(id);
    }

    // Retorna todas as auditorias cadastradas.
    public List<Auditoria> listarTodos() {
        return auditoriaDAO.listarTodos();
    }

    // Atualiza os dados de uma auditoria existente.
    public void atualizar(Auditoria auditoria) {
        auditoriaDAO.atualizar(auditoria);
    }

    // Exclui uma auditoria pelo ID.
    public void excluir(Long id) {
        auditoriaDAO.excluir(id);
    }

    // Agenda uma auditoria para uma determinada data e hora.
    public void agendar(Long auditoriaId, LocalDateTime dataHora) {

        Auditoria auditoria = buscarPorId(auditoriaId);

        if (auditoria == null) {
            throw new IllegalArgumentException(
                    "Auditoria não encontrada."
            );
        }

        auditoria.agendar(dataHora);
        atualizar(auditoria);
    }

    // Altera a data e hora de uma auditoria já agendada.
    public void alterarDataAgendada(
            Long auditoriaId,
            LocalDateTime novaData
    ) {

        Auditoria auditoria = buscarPorId(auditoriaId);

        if (auditoria == null) {
            throw new IllegalArgumentException(
                    "Auditoria não encontrada."
            );
        }

        auditoria.alterarDataAgendada(novaData);
        atualizar(auditoria);
    }

    // Cancela uma auditoria.
    public void cancelar(Long auditoriaId) {

        Auditoria auditoria = buscarPorId(auditoriaId);

        if (auditoria == null) {
            throw new IllegalArgumentException(
                    "Auditoria não encontrada."
            );
        }

        auditoria.cancelar();
        atualizar(auditoria);
    }

    // Inicia a execução da auditoria.
    public void iniciar(Long auditoriaId) {

        Auditoria auditoria = buscarPorId(auditoriaId);

        if (auditoria == null) {
            throw new IllegalArgumentException(
                    "Auditoria não encontrada."
            );
        }

        auditoria.iniciar();
        atualizar(auditoria);
    }

    // Pausa uma auditoria que está em andamento.
    public void pausar(Long auditoriaId) {

        Auditoria auditoria = buscarPorId(auditoriaId);

        if (auditoria == null) {
            throw new IllegalArgumentException(
                    "Auditoria não encontrada."
            );
        }

        auditoria.pausar();
        atualizar(auditoria);
    }

    // Finaliza uma auditoria.
    public void finalizar(Long auditoriaId) {

        Auditoria auditoria = buscarPorId(auditoriaId);

        if (auditoria == null) {
            throw new IllegalArgumentException(
                    "Auditoria não encontrada."
            );
        }

        auditoria.finalizar();
        atualizar(auditoria);
    }

    // Adiciona um responsável a uma auditoria.
    public void adicionarResponsavel(
            Long auditoriaId,
            Long responsavelId
    ) {
        auditoriaDAO.adicionarResponsavel(
                auditoriaId,
                responsavelId
        );
    }

    // Adiciona um documento a uma auditoria.
    public void adicionarDocumento(
            Long auditoriaId,
            Long documentoId
    ) {
        auditoriaDAO.adicionarDocumento(
                auditoriaId,
                documentoId
        );
    }
}