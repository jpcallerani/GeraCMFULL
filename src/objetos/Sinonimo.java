package objetos;

public class Sinonimo
  implements Objeto
{
  private String nome;
  private String cabecalho;
  private String tableOwner;
  private String tableName;

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

  public String getTableOwner()
  {
    return this.tableOwner;
  }

  public void setTableOwner(String tableOwner)
  {
    this.tableOwner = tableOwner;
  }

  public String getTableName()
  {
    return this.tableName;
  }

  public void setTableName(String tableName)
  {
    this.tableName = tableName;
  }
}