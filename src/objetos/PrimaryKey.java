package objetos;

import java.util.ArrayList;

public class PrimaryKey
  implements Objeto
{
  private String nome;
  private String tablespace;
  private ArrayList<String> colunas;
  private String cabecalho;
  private String index; 

  public PrimaryKey()
  {
    this.colunas = new ArrayList();
  }

  public boolean exists(String col)
  {
    return this.colunas.contains(col);
  }

  public void addColuna(String col) {
    this.colunas.add(col);
  }

  public void removeColuna(String col) {
    this.colunas.remove(col);
  }

  public String getNome()
  {
    return this.nome;
  }

  public void setNome(String nome)
  {
    this.nome = nome;
  }

  public String[] getColunas()
  {
    String[] cols = new String[this.colunas.size()];
    return (String[])this.colunas.toArray(cols);
  }

  public String getTablespace()
  {
    return this.tablespace + "_4K";
  }

  public void setTablespace(String tablespace)
  {
    this.tablespace = tablespace;
  }

  public String getCabecalho()
  {
    return this.cabecalho;
  }

  public void setCabecalho(String cabecalho)
  {
    this.cabecalho = cabecalho;
  }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}