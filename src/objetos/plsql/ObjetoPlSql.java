package objetos.plsql;

import grant.Grantable;
import objetos.Objeto;

public class ObjetoPlSql extends Grantable
        implements Objeto {

    private String nome;
    private String source;
    private String comment;
    private String comment_columns;
    private String tipo;

    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public void setCabecalho(String cabecalho) {
    }

    @Override
    public String getCabecalho() {
        return null;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_columns() {
        return comment_columns;
    }

    public void setComment_columns(String comment_columns) {
        this.comment_columns = comment_columns;
    }
}
