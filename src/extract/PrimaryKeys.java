package extract;

import java.sql.SQLException;
import objetos.Objeto;
import objetos.PrimaryKey;
import objetos.Tabela;

public class PrimaryKeys extends Extract {

    public PrimaryKeys(Objeto[] objs, String np)
            throws ClassNotFoundException, SQLException {
        super(objs, np);
    }

    public String geraComandoCriacao(Objeto o) {
        StringBuilder cmd = new StringBuilder();

        Tabela t = (Tabela) o;

        PrimaryKey pk = t.getPrimaryKey();

        if (pk != null) {
            cmd.append("\n\n\nprompt ===================================\n")
                    .append("prompt   Tabela ").append(t.getNome()).append("\n")
                    .append("prompt ===================================\n\n");

            cmd.append("alter table ")
                    .append(t.getNome())
                    .append("\n  add constraint ")
                    .append(pk.getNome())
                    .append(" primary key (");

            for (String c : pk.getColunas()) {
                cmd.append(c).append(", ");
            }

            cmd.deleteCharAt(cmd.length() - 2);
            cmd.append(")\n");
            if (!t.isTemporary()) {
                if (pk.getIndex() != null && !pk.getIndex().equalsIgnoreCase("")) {
                    cmd.append("  using index ")
                            .append(pk.getIndex());
                }
            }
            cmd.append(';');
        }
        return cmd.toString();
    }
}
