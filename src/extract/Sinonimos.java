package extract;

import java.sql.SQLException;
import objetos.Objeto;
import objetos.Sinonimo;

public class Sinonimos extends Extract
{
  public Sinonimos(Objeto[] objs, String np)
    throws ClassNotFoundException, SQLException
  {
    super(objs, np);
  }

  public String geraComandoCriacao(Objeto o)
  {
    StringBuilder cmd = new StringBuilder();

    Sinonimo syn = (Sinonimo)o;

    cmd.append("\n\nprompt ===================================\n")
      .append("prompt   Sinonimo ").append(syn.getNome()).append("\n")
      .append("prompt ===================================\n\n");

    cmd.append("create or replace synonym ").append(syn.getNome());
    cmd.append(" for ").append(syn.getTableOwner()).append(".");
    cmd.append(syn.getTableName()).append(';').append('\n');

    return cmd.toString();
  }
}