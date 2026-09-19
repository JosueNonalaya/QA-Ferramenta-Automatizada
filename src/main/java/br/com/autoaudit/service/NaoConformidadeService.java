package br.com.autoaudit.service;

import br.com.autoaudit.dao.NaoConformidadeDAO;
import br.com.autoaudit.model.NaoConformidade;

import java.util.List;

public class NaoConformidadeService {

    private final NaoConformidadeDAO naoConformidadeDAO =
            new NaoConformidadeDAO();

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
}