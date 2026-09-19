package br.com.autoaudit.service;

import br.com.autoaudit.dao.ClassificacaoNCDAO;
import br.com.autoaudit.model.ClassificacaoNC;

import java.util.List;

public class ClassificacaoNCService {

    private final ClassificacaoNCDAO classificacaoNCDAO =
            new ClassificacaoNCDAO();

    // Salva uma classificação de não conformidade.
    public void salvar(ClassificacaoNC classificacao) {
        classificacaoNCDAO.salvar(classificacao);
    }

    // Busca uma classificação pelo ID.
    public ClassificacaoNC buscarPorId(Long id) {
        return classificacaoNCDAO.buscarPorId(id);
    }

    // Retorna todas as classificações cadastradas.
    public List<ClassificacaoNC> listarTodos() {
        return classificacaoNCDAO.listarTodos();
    }

    // Atualiza uma classificação existente.
    public void atualizar(ClassificacaoNC classificacao) {
        classificacaoNCDAO.atualizar(classificacao);
    }

    // Exclui uma classificação pelo ID.
    public void excluir(Long id) {
        classificacaoNCDAO.excluir(id);
    }
}