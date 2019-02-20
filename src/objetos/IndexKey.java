package objetos;

import java.util.ArrayList;

public class IndexKey
        implements Objeto {

    private String nome;
    private String cabecalho;
    private String tablespace;
    private ArrayList<String> cols;
    private String UNIQUESS;

    public IndexKey() {
        this.cols = new ArrayList();
    }

    public void addColuna(String c) {
        this.cols.add(c);
    }

    public String[] getColunas() {
        return (String[]) this.cols.toArray(new String[this.cols.size()]);
    }

    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getCabecalho() {
        return this.cabecalho;
    }

    @Override
    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public String getTablespace() {
        return this.tablespace + "_4K";
    }

    public void setTablespace(String tablespace) {
        this.tablespace = tablespace;
    }

    public String getUNIQUESS() {
        return UNIQUESS;
    }

    public void setUNIQUESS(String UNIQUESS) {
        this.UNIQUESS = UNIQUESS;
    }
}
