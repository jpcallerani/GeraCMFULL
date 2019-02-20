package grant;

import java.util.ArrayList;
import java.util.List;
import objetos.Objeto;

public class Grant
{
  private List<Privilegio> privilegios;
  private String grantee;
  private Objeto objeto;

  public Grant()
  {
    this.privilegios = new ArrayList();
  }

  public Grant addPrivilegio(Privilegio p)
  {
    this.privilegios.add(p);

    return this;
  }

  public Grant removePrivilegio(Privilegio p) {
    this.privilegios.remove(p);

    return this;
  }

  public List<Privilegio> getPrivilegios() {
    return this.privilegios;
  }

  public void setPrivilegios(List<Privilegio> privilegios) {
    this.privilegios = privilegios;
  }

  public String getGrantee() {
    return this.grantee;
  }

  public void setGrantee(String grantee) {
    this.grantee = grantee;
  }

  public Objeto getObjeto() {
    return this.objeto;
  }

  public void setObjeto(Objeto objeto) {
    this.objeto = objeto;
  }
}
