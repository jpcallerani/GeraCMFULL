package extract;

import java.sql.SQLException;
import objetos.Objeto;
import objetos.Sequence;

public class Sequences extends Extract
{
  public Sequences(Objeto[] objs, String np)
    throws ClassNotFoundException, SQLException
  {
    super(objs, np);
  }

  public String geraComandoCriacao(Objeto o)
  {
    StringBuilder cmd = new StringBuilder();

    Sequence seq = (Sequence)o;

    cmd.append("\n\nprompt ===================================\n")
      .append("prompt   Sequence ").append(seq.getNome()).append("\n")
      .append("prompt ===================================\n\n");

    cmd.append("create sequence ").append(seq.getNome()).append('\n');
    cmd.append("minvalue ").append(seq.getMinValue()).append('\n');
    cmd.append("maxvalue ").append(seq.getMaxValue()).append('\n');
    cmd.append("start with ").append(seq.getStartWith()).append('\n');
    cmd.append("increment by ").append(seq.getIncrementBy()).append('\n');

    if (seq.getCache() == 0)
      cmd.append("nocache").append('\n');
    else {
      cmd.append("cache ").append(seq.getCache()).append('\n');
    }

    if (("Y".equals(seq.getCycleFlag())) || ("S".equals(seq.getCycleFlag())))
      cmd.append("cycle").append('\n');
    else {
      cmd.append("nocycle").append('\n');
    }

    if (("Y".equals(seq.getOrderFlag())) || ("S".equals(seq.getOrderFlag())))
      cmd.append("order").append(';').append('\n');
    else {
      cmd.append("noorder").append(';').append('\n');
    }
    cmd.append('\n');

    return cmd.toString();
  }
}