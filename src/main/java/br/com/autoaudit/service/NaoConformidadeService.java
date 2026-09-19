package br.com.autoaudit.service;

import br.com.autoaudit.dao.NaoConformidadeDAO;
import br.com.autoaudit.model.NaoConformidade;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import java.util.List;

public class NaoConformidadeService {

    private final NaoConformidadeDAO naoConformidadeDAO =
            new NaoConformidadeDAO();

    private final Set<LocalDate> feriados = new HashSet<>();


    // Salva uma nova não conformidade.
    public void salvar(NaoConformidade naoConformidade) {
        naoConformidadeDAO.salvar(naoConformidade);
    }

    // Busca uma não conformidade pelo ID.
    public NaoConformidade buscarPorId(Long id) {
        return naoConformidadeDAO.buscarPorId(id);
    }

    // Retorna todas as não conformidades.
    public List<NaoConformidade> listarTodos() {
        return naoConformidadeDAO.listarTodos();
    }

    // Atualiza uma não conformidade existente.
    public void atualizar(NaoConformidade naoConformidade) {
        naoConformidadeDAO.atualizar(naoConformidade);
    }

    // Exclui uma não conformidade pelo ID.
    public void excluir(Long id) {
        naoConformidadeDAO.excluir(id);
    }

    // Adiciona um feriado à lista usada no cálculo dos prazos.
    public void adicionarFeriado(LocalDate feriado) {
        feriados.add(feriado);
    }

    // Remove um feriado da lista.
    public void removerFeriado(LocalDate feriado) {
        feriados.remove(feriado);
    }

    // Limpa todos os feriados cadastrados.
    public void limparFeriados() {
        feriados.clear();
    }

    // Calcula a data de resolução considerando somente dias úteis.
    public LocalDate calcularDataResolucao(
            LocalDate dataIdentificacao,
            int prazoHoras
    ) {

        if (dataIdentificacao == null) {
            throw new IllegalArgumentException(
                    "A data de identificação deve ser informada."
            );
        }

        if (prazoHoras < 0) {
            throw new IllegalArgumentException(
                    "O prazo não pode ser negativo."
            );
        }

        // Converte o prazo em horas para dias de prazo.
        int diasPrazo = (int) Math.ceil(prazoHoras / 24.0);

        LocalDate data = dataIdentificacao;

        int diasContados = 0;

        while (diasContados < diasPrazo) {

            data = data.plusDays(1);

            if (ehDiaUtil(data)) {
                diasContados++;
            }
        }

        return data;
    }

    // Verifica se uma data pode ser considerada dia útil.
    private boolean ehDiaUtil(LocalDate data) {

        DayOfWeek diaSemana = data.getDayOfWeek();

        // Sábado e domingo não são considerados.
        if (diaSemana == DayOfWeek.SATURDAY
                || diaSemana == DayOfWeek.SUNDAY) {
            return false;
        }

        // Feriados também não são considerados.
        return !feriados.contains(data);
    }

    // Calcula e preenche automaticamente a data de resolução da NC.
    public void definirDataResolucaoAutomatica(
            NaoConformidade naoConformidade
    ) {

        if (naoConformidade.getDataIdentificacao() == null) {
            throw new IllegalArgumentException(
                    "A data de identificação deve ser informada."
            );
        }

        if (naoConformidade.getClassificacao() == null) {
            throw new IllegalArgumentException(
                    "A classificação da NC deve ser informada."
            );
        }

        int prazoHoras =
                naoConformidade
                        .getClassificacao()
                        .getPrazoHoras();

        LocalDate dataResolucao =
                calcularDataResolucao(
                        naoConformidade.getDataIdentificacao(),
                        prazoHoras
                );

        naoConformidade.setDataResolucao(
                dataResolucao
        );
    }
}