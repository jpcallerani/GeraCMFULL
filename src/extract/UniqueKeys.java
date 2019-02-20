package extract;

import java.sql.SQLException;
import objetos.Objeto;
import objetos.Tabela;
import objetos.UniqueKey;

public class UniqueKeys extends Extract
{
  public UniqueKeys(Objeto[] objs, String np)
    throws ClassNotFoundException, SQLException
  {
    super(objs, np);
  }

  public String geraComandoCriacao(Objeto o)
  {
    StringBuilder cmd = new StringBuilder();

    Tabela t = (Tabela)o;

    if (t.getUniqueKeys().length > 0) {
      cmd.append("\n\n\nprompt ===================================\n")
        .append("prompt   Tabela ").append(t.getNome()).append("\n")
        .append("prompt ===================================\n\n");
    }
    UniqueKey[] uk = t.getUniqueKeys();

    for (UniqueKey u : uk)
    {
      if (cmd.length() > 0) {
        cmd.append("\n\n");
      }

      cmd.append("alter table ")
        .append(t.getNome())
        .append("\n  add constraint ")
        .append(u.getNome())
        .append(" unique (");

      for (String c : u.getColunas()) {
        cmd.append(c).append(", ");
      }

      cmd.deleteCharAt(cmd.length() - 1).deleteCharAt(cmd.length() - 1);

      cmd.append(")");

      if ((!t.isTemporary()) && (u.getTablespace() != null)) {
        cmd.append("\n  using index \n  tablespace ").append(u.getTablespace());
      }
      cmd.append(';');
    }
    return cmd.toString();
  }
}