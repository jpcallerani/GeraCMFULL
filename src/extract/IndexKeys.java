package extract;

import java.sql.SQLException;
import objetos.IndexKey;
import objetos.Objeto;
import objetos.Tabela;

public class IndexKeys extends Extract {

    public IndexKeys(Objeto[] objs, String np)
            throws ClassNotFoundException, SQLException {
        super(objs, np);
    }

    public String geraComandoCriacao(Objeto o) {
        StringBuilder cmd = new StringBuilder();

        Tabela t = (Tabela) o;

        if (t.getIndexKeys().length > 0) {
            cmd.append("\n\n\nprompt ===================================\n")
                    .append("prompt   Tabela ").append(t.getNome()).append("\n")
                    .append("prompt ===================================\n\n");
        }

        IndexKey[] ik = t.getIndexKeys();

        for (IndexKey u : ik) {
            if (cmd.length() > 0) {
                cmd.append("\n\n");
            }

            if (u.getUNIQUESS().equalsIgnoreCase("UNIQUE")) {
                cmd.append("create unique index ").append(u.getNome());
            } else {
                cmd.append("create index ").append(u.getNome());
            }
            cmd.append(" on ");
            cmd.append(t.getNome());
            cmd.append(" (");

            for (String c : u.getColunas()) {
                cmd.append(c).append(", ");
            }

            cmd.deleteCharAt(cmd.length() - 1).deleteCharAt(cmd.length() - 1);

            cmd.append(")")
                    .append(t.isTemporary() ? " " : "\n tablespace ")
                    .append(t.isTemporary() ? "" : u.getTablespace())
                    .append(';');
        }
        return cmd.toString();
    }
}
