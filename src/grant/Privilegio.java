
package grant;

public class Privilegio
{
  private String nome;
  private boolean grantable;
  private boolean hierarchy;

  public String getNome()
  {
    return this.nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public boolean isGrantable() {
    return this.grantable;
  }

  public void setGrantable(boolean grantable) {
    this.grantable = grantable;
  }

  public boolean isHierarchy() {
    return this.hierarchy;
  }

  public void setHierarchy(boolean hierarchy) {
    this.hierarchy = hierarchy;
  }
}