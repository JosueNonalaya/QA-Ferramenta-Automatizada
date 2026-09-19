package br.com.autoaudit.service;

import br.com.autoaudit.dao.ResponsavelDAO;
import br.com.autoaudit.model.Responsavel;

import java.util.List;

public class ResponsavelService {

    private final ResponsavelDAO responsavelDAO =
            new ResponsavelDAO();

    // Salva um novo responsável.
    public void salvar(Responsavel responsavel) {
        responsavelDAO.salvar(responsavel);
    }

    // Busca um responsável pelo ID.
    public Responsavel buscarPorId(Long id) {
        return responsavelDAO.buscarPorId(id);
    }

    // Retorna todos os responsáveis cadastrados.
    public List<Responsavel> listarTodos() {
        return responsavelDAO.listarTodos();
    }

    // Atualiza um responsável existente.
    public void atualizar(Responsavel responsavel) {
        responsavelDAO.atualizar(responsavel);
    }

    // Exclui um responsável pelo ID.
    public void excluir(Long id) {
        responsavelDAO.excluir(id);
    }
}