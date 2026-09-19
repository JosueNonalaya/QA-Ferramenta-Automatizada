package br.com.autoaudit.controller;

import br.com.autoaudit.enums.StatusAuditoria;
import br.com.autoaudit.model.Auditoria;
import br.com.autoaudit.model.Checklist;
import br.com.autoaudit.model.ClassificacaoNC;
import br.com.autoaudit.model.Documento;
import br.com.autoaudit.model.PerguntaChecklist;
import br.com.autoaudit.model.Responsavel;
import br.com.autoaudit.service.AuditoriaService;
import br.com.autoaudit.service.ChecklistService;
import br.com.autoaudit.service.ClassificacaoNCService;
import br.com.autoaudit.service.DocumentoService;
import br.com.autoaudit.service.PerguntaChecklistService;
import br.com.autoaudit.service.ResponsavelService;

import java.time.LocalDateTime;

public class NovaAuditoriaController {

    private static final int TOTAL_ETAPAS = 5;

    private int etapaAtual = 1;

    private final AuditoriaService auditoriaService =
            new AuditoriaService();

    private final ChecklistService checklistService =
            new ChecklistService();

    private final ClassificacaoNCService classificacaoNCService =
            new ClassificacaoNCService();

    private final DocumentoService documentoService =
            new DocumentoService();

    private final PerguntaChecklistService perguntaChecklistService =
            new PerguntaChecklistService();

    private final ResponsavelService responsavelService =
            new ResponsavelService();

    private final Auditoria auditoria;
    private final Checklist checklist;

    // Cria o estado inicial da nova auditoria.
    public NovaAuditoriaController() {
        auditoria = new Auditoria();
        auditoria.setStatus(StatusAuditoria.AGENDADA);

        checklist = new Checklist();

        auditoria.setChecklist(checklist);
    }

    // Retorna a etapa atual da criação da auditoria.
    public int getEtapaAtual() {
        return etapaAtual;
    }

    // Retorna o total de etapas do processo.
    public int getTotalEtapas() {
        return TOTAL_ETAPAS;
    }

    // Retorna o progresso atual para ser usado na barra da interface.
    public double getProgresso() {
        return (double) etapaAtual / TOTAL_ETAPAS;
    }

    // Avança para a próxima etapa.
    public void avancarEtapa() {
        if (etapaAtual < TOTAL_ETAPAS) {
            etapaAtual++;
        }
    }

    // Retorna para a etapa anterior.
    public void voltarEtapa() {
        if (etapaAtual > 1) {
            etapaAtual--;
        }
    }

    // Define o nome do projeto analisado.
    public void definirNomeProjeto(String nomeProjeto) {
        auditoria.setNomeProjeto(nomeProjeto);
    }

    // Adiciona um responsável à equipe da auditoria.
    public void adicionarResponsavel(Responsavel responsavel) {

        if (responsavel.getId() == null) {
            responsavelService.salvar(responsavel);
        }

        auditoria.adicionarResponsavel(responsavel);
    }

    // Adiciona um documento à auditoria.
    public void adicionarDocumento(Documento documento) {

        if (documento.getId() == null) {
            documentoService.salvar(documento);
        }

        auditoria.adicionarDocumento(documento);
    }

    // Adiciona uma pergunta ao checklist.
    public void adicionarPergunta(PerguntaChecklist pergunta) {
        pergunta.setChecklist(checklist);
        checklist.adicionarPergunta(pergunta);
    }

    // Adiciona uma classificação de não conformidade.
    public void adicionarClassificacao(ClassificacaoNC classificacao) {
        classificacaoNCService.salvar(classificacao);
    }

    // Define o percentual mínimo de aderência aceitável.
    public void definirPercentualAderenciaAceitavel(
            double percentual
    ) {
        auditoria.definirPercentualAderenciaAceitavel(percentual);
    }

    // Define a data e hora da auditoria.
    public void definirDataHoraAgendada(
            LocalDateTime dataHora
    ) {
        auditoria.agendar(dataHora);
    }

    // Retorna a auditoria que está sendo criada.
    public Auditoria getAuditoria() {
        return auditoria;
    }

    // Retorna o checklist que está sendo criado.
    public Checklist getChecklist() {
        return checklist;
    }

    // Salva todos os dados da nova auditoria no banco.
    public Auditoria salvarAuditoria() {

        validarDadosObrigatorios();

        auditoriaService.salvar(auditoria);

        checklistService.salvar(checklist);

        for (PerguntaChecklist pergunta :
                checklist.getPerguntas()) {

            perguntaChecklistService.salvar(pergunta);
        }

        return auditoria;
    }

    // Verifica os dados mínimos necessários para salvar a auditoria.
    private void validarDadosObrigatorios() {

        if (auditoria.getNomeProjeto() == null
                || auditoria.getNomeProjeto().isBlank()) {

            throw new IllegalArgumentException(
                    "O nome do projeto deve ser informado."
            );
        }

        if (auditoria.getDataHoraAgendada() == null) {

            throw new IllegalArgumentException(
                    "A data e hora da auditoria devem ser informadas."
            );
        }

        if (checklist.getPerguntas().isEmpty()) {

            throw new IllegalArgumentException(
                    "O checklist deve possuir pelo menos uma pergunta."
            );
        }
    }
}