package objetos;

public class CheckKey
  implements Objeto
{
  private String nome;
  private String cabecalho;
  private String condicao;

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

  public String getCondicao()
  {
    return this.condicao;
  }

  public void setCondicao(String condicao)
  {
    this.condicao = condicao;
  }
}
