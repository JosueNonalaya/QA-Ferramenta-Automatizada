package br.com.autoaudit.model;

import br.com.autoaudit.enums.OpcaoResposta;

public class RespostaChecklist {

    private Long id;
    private OpcaoResposta opcao;
    private PerguntaChecklist pergunta;

    public RespostaChecklist() {
    }

    public RespostaChecklist(OpcaoResposta opcao, PerguntaChecklist pergunta) {
        this.opcao = opcao;
        this.pergunta = pergunta;
    }

    //GETTER's e SETTER's
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OpcaoResposta getOpcao() {
        return opcao;
    }

    public void setOpcao(OpcaoResposta opcao) {
        this.opcao = opcao;
    }

    public PerguntaChecklist getPergunta() {
        return pergunta;
    }

    public void setPergunta(PerguntaChecklist pergunta) {
        this.pergunta = pergunta;
    }
}