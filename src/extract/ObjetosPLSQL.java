package extract;

import java.sql.SQLException;
import objetos.Objeto;
import objetos.plsql.ObjetoPlSql;

public class ObjetosPLSQL extends Extract {

    public ObjetosPLSQL(Objeto[] objs, String np)
            throws ClassNotFoundException, SQLException {
        super(objs, np);
    }

    /**
     *
     * @param o
     * @return
     */
    public String geraComandoCriacao(Objeto o) {
        StringBuilder cmd = new StringBuilder();

        ObjetoPlSql plsql = (ObjetoPlSql) o;

        String src = "";
        char[] cSrc = plsql.getSource().toCharArray();

        for (int i = cSrc.length - 1; i > 0; i--) {
            if ((cSrc[(i - 1)] != ' ') && (cSrc[(i - 1)] != '\n') && (cSrc[(i - 1)] != '\t')) {
                src = plsql.getSource().substring(0, i);
                break;
            }
        }

        cmd.append("\n\nprompt ===================================\n")
                .append("prompt   " + plsql.getTipo() + "  ").append(plsql.getNome()).append("\n")
                .append("prompt ===================================\n\n");

        cmd.append('\n').append(src).append("\n/\n");

        if (plsql.getTipo().equalsIgnoreCase("VIEW") && plsql.getComment() != null) {
            cmd.append("comment on table ");
            cmd.append(plsql.getNome());
            cmd.append(" is ");
            cmd.append(" '");
            cmd.append(plsql.getComment());
            cmd.append("';\n");
        }
        
        if (plsql.getTipo().equalsIgnoreCase("VIEW") && !plsql.getComment_columns().equals("")) {
            cmd.append(plsql.getComment_columns());
        }

        if (plsql.getTipo().equalsIgnoreCase("JAVA SOURCE")) {
            cmd.append("SET DEFINE ON");
        }

        return cmd.toString();
    }
}
