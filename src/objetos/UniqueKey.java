package objetos;

import java.util.ArrayList;

public class UniqueKey
  implements Objeto
{
  private String nome;
  private String cabecalho;
  private ArrayList<String> colunas;
  private String tablespace;

  public UniqueKey()
  {
    this.colunas = new ArrayList();
  }

  public void addColuna(String col)
  {
    this.colunas.add(col);
  }

  public String[] getColunas() {
    return (String[])this.colunas.toArray(new String[this.colunas.size()]);
  }

  public String getNome()
  {
    return this.nome;
  }

  public void setNome(String nome)
  {
    this.nome = nome;
  }

  public String getCabecalho()
  {
    return this.cabecalho;
  }

  public void setCabecalho(String cabecalho)
  {
    this.cabecalho = cabecalho;
  }

  public String getTablespace()
  {
    return this.tablespace + "_4K";
  }

  public void setTablespace(String tablespace)
  {
    this.tablespace = tablespace;
  }
}
