package extract;

import java.sql.SQLException;
import objetos.CheckKey;
import objetos.Objeto;
import objetos.Tabela;

public class CheckKeys extends Extract
{
  public CheckKeys(Objeto[] objs, String np)
    throws ClassNotFoundException, SQLException
  {
    super(objs, np);
  }

  public String geraComandoCriacao(Objeto o)
  {
    StringBuilder cmd = new StringBuilder();

    Tabela t = (Tabela)o;

    cmd.append("\n\n\nprompt ===================================\n")
      .append("prompt   Tabela ").append(t.getNome()).append("\n")
      .append("prompt ===================================\n");

    for (CheckKey c : t.getCheckKeys()) {
      if (cmd.length() > 0) {
        cmd.append("\n\n");
      }
      cmd.append("alter table ").append(t.getNome()).append("\n");
      cmd.append("  add constraint ").append(c.getNome()).append(" check ");
      cmd.append("(").append(c.getCondicao()).append(");");
    }

    return cmd.toString();
  }
}