package br.com.autoaudit.controller;

import br.com.autoaudit.model.NaoConformidade;
import br.com.autoaudit.model.Responsavel;
import br.com.autoaudit.service.NaoConformidadeService;

import java.util.ArrayList;
import java.util.List;

public class ComunicacaoNCController {

    private final NaoConformidadeService naoConformidadeService =
            new NaoConformidadeService();

    // Retorna todas as não conformidades de uma auditoria.
    public List<NaoConformidade> carregarNaoConformidades(
            Long auditoriaId
    ) {

        List<NaoConformidade> resultado =
                new ArrayList<>();

        for (NaoConformidade nc :
                naoConformidadeService.listarTodos()) {

            if (nc.getAuditoria() != null
                    && nc.getAuditoria().getId()
                    .equals(auditoriaId)) {

                resultado.add(nc);
            }
        }

        return resultado;
    }

    // Retorna o responsável associado à não conformidade.
    public Responsavel obterResponsavel(
            NaoConformidade naoConformidade
    ) {
        return naoConformidade.getResponsavel();
    }

    // Retorna o endereço de e-mail do responsável.
    public String obterEmailResponsavel(
            NaoConformidade naoConformidade
    ) {

        if (naoConformidade.getResponsavel() == null
                || naoConformidade.getResponsavel().getUsuario() == null) {

            return null;
        }

        return naoConformidade
                .getResponsavel()
                .getUsuario()
                .getEmail();
    }

    // Monta o texto que será apresentado antes do envio.
    public String gerarMensagem(
            NaoConformidade naoConformidade
    ) {

        String nomeResponsavel =
                naoConformidade.getResponsavel() != null
                        && naoConformidade.getResponsavel().getUsuario() != null
                        ? naoConformidade.getResponsavel()
                          .getUsuario()
                          .getNome()
                        : "";

        return """
                Olá, %s.

                Foi identificada uma não conformidade durante a auditoria.

                Descrição:
                %s

                Classificação:
                %s

                Status:
                %s

                Data de identificação:
                %s

                Data prevista para resolução:
                %s

                Ação corretiva:
                %s
                """.formatted(
                nomeResponsavel,
                naoConformidade.getDescricao(),
                naoConformidade.getClassificacao().getNome(),
                naoConformidade.getStatus().name(),
                naoConformidade.getDataIdentificacao(),
                naoConformidade.getDataResolucao(),
                naoConformidade.getAcaoCorretiva()
        );
    }
}