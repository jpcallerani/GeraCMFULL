package extract;

import java.sql.SQLException;
import objetos.ForeignKey;
import objetos.Objeto;
import objetos.Tabela;

public class ForeignKeys extends Extract
{
  public ForeignKeys(Objeto[] objs, String np)
    throws ClassNotFoundException, SQLException
  {
    super(objs, np);
  }

  public String geraComandoCriacao(Objeto o)
  {
    StringBuilder cmd = new StringBuilder();

    Tabela t = (Tabela)o;

    if (t.getForeignKeys().length > 0) {
      cmd.append("\n\n\nprompt ===================================\n")
        .append("prompt   Tabela ").append(t.getNome()).append("\n")
        .append("prompt ===================================\n");
    }

    for (ForeignKey fk : t.getForeignKeys())
    {
      if (cmd.length() > 0) {
        cmd.append("\n");
      }

      cmd.append("begin\n--\n");

      cmd.append("\texecute immediate '");
      cmd.append(geraComandoCriacaoFK(t, fk));
      cmd.append("';\n--\n");

      cmd.append("exception\n\twhen OTHERS then\n");
      cmd.append("\t\t-- Cria a FK desabilitada\n");
      cmd.append("\t\texecute immediate '");
      cmd.append(geraComandoCriacaoFK(t, fk));
      cmd.append(" disable';\nend;\n/\n");
    }

    return cmd.toString();
  }

  private String geraComandoCriacaoFK(Tabela t, ForeignKey fk) {
    StringBuilder cmd = new StringBuilder();

    cmd.append("alter table ")
      .append(t.getNome())
      .append("\n\t\tadd constraint ")
      .append(fk.getNome())
      .append(" foreign key (");

    for (String c : fk.getColunas()) {
      cmd.append(c).append(", ");
    }

    cmd.deleteCharAt(cmd.length() - 1).deleteCharAt(cmd.length() - 1);

    cmd.append(")\n\t\t")
      .append("references ")
      .append(fk.getReferenceOwner())
      .append(".")
      .append(fk.getReferenceTable())
      .append("(");

    for (String c : fk.getReferenceCols()) {
      cmd.append(c).append(", ");
    }
    cmd.deleteCharAt(cmd.length() - 1).deleteCharAt(cmd.length() - 1);
    cmd.append(")")
      .append(fk.getDeleteRule()).append(" ")
      .append(fk.isDeferrable() ? " deferrable" : "")
      .append(fk.isDeferred() ? " initially deferred" : "");

    return cmd.toString();
  }
}