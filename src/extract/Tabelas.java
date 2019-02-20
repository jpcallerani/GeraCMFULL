package extract;

import java.sql.SQLException;
import objetos.Coluna;
import objetos.Objeto;
import objetos.Tabela;

public class Tabelas extends Extract {

    public Tabelas(Objeto[] Objs, String np)
            throws ClassNotFoundException, SQLException {
        super(Objs, np);
    }

    /**
     *
     * @param o
     * @return
     */
    public String geraComandoCriacao(Objeto o) {
        StringBuilder cmd = new StringBuilder();
        Tabela t = (Tabela) o;

        cmd.append("\n\n\nprompt ===================================\n")
                .append("prompt   Tabela ").append(t.getNome()).append("\n")
                .append("prompt ===================================\n\n");

        cmd.append("create ")
                .append(t.isTemporary() ? "global temporary" : "")
                .append(" table ")
                .append(t.getNome())
                .append("\n(\n");

        for (Coluna c : t.getColunas()) {
            cmd.append(c.getNome().toUpperCase())
                    .append(" ")
                    .append(formataTamanhoColuna(c.getTipo(), c.getTamanho(), c.getChar_used(), c.getData_scale()))
                    .append(" ")
                    .append(" default " + c.getValorDefault())
                    .append(" ")
                    .append(c.getNullable())
                    .append(",\n");
        }

        cmd.deleteCharAt(cmd.length() - 2);

        cmd.append(") ")
                .append(t.isTemporary() ? t.getOnCommit() : "")
                .append(t.isTemporary() ? "\n" : "\n tablespace ")
                .append(t.isTemporary() ? "" : t.getTablespace())
                .append(";\n\n");

        if ((t.getComentario() != null)
                && (t.getComentario().trim().length() > 0)) {
            cmd.append("--\ncomment on table ")
                    .append(t.getNome())
                    .append("\n")
                    .append(" is ")
                    .append("'")
                    .append(t.getComentario().replace("'", "''"))
                    .append("';")
                    .append("\n");
        }

        for (Coluna c : t.getColunas()) {
            if ((c.getComment() == null)
                    || (c.getComment().trim().length() <= 0)) {
                continue;
            }
            cmd.append("comment on column ")
                    .append(t.getNome())
                    .append(".")
                    .append(c.getNome())
                    .append("\n is '")
                    .append(c.getComment().replace("'", "''"))
                    .append("';\n");
        }

        return cmd.toString();
    }

    /**
     *
     * @param dataType
     * @param tamanho
     * @param charUsed
     * @return
     */
    private String formataTamanhoColuna(String dataType, int tamanho, String charUsed, int data_scale) {
        if (dataType.equalsIgnoreCase("VARCHAR2")) {
            if (charUsed != null && charUsed.equalsIgnoreCase("C")) {
                return "VARCHAR2(" + String.valueOf(tamanho).trim() + " CHAR)";
            } else {
                return "VARCHAR2(" + String.valueOf(tamanho).trim() + ")";
            }
        }
        //
        if (dataType.equalsIgnoreCase("CHAR")) {
            if (charUsed != null && charUsed.equalsIgnoreCase("C")) {
                return "CHAR(" + String.valueOf(tamanho).trim() + " CHAR)";
            } else {
                return "CHAR(" + String.valueOf(tamanho).trim() + ")";
            }
        }
        //
        if (dataType.equalsIgnoreCase("NVARCHAR2")) {
            return "NVARCHAR2(" + String.valueOf(tamanho).trim() + ")";
        }
        //
        if (dataType.equalsIgnoreCase("NUMBER")) {
            if (tamanho > 0) {
                if (data_scale > -1) {
                    return "NUMBER(" + String.valueOf(tamanho).trim() + "," + String.valueOf(data_scale).trim() + ")";
                } else {
                    return "NUMBER(" + String.valueOf(tamanho).trim() + ")";
                }
            } else {
                if (data_scale > -1) {
                    return "INTEGER";
                }
            }
        }
        return dataType;
    }
}
