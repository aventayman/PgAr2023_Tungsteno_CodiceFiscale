package it.unibs.fp.codiceFiscale;

public class Comune {
    private String nome, codice;

    public Comune(String nome, String codice) {
        this.nome = nome;
        this.codice = codice;
    }

    public String getNome() {
        return nome;
    }

    public String getCodice() {
        return codice;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
}
