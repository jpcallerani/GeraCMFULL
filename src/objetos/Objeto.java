package objetos;

public abstract interface Objeto
{
  public static final String TABLESPACE_SIZE = "_4K";

  public abstract void setNome(String paramString);

  public abstract String getNome();

  public abstract void setCabecalho(String paramString);

  public abstract String getCabecalho();
}