package carga;

import depara.Dados;
import depara.Owners;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetos.Coluna;
import objetos.Tabela;

public class CargaTabela {

    private static final int ACTION_INSERT = 1;
    private static final int ACTION_UPDATE = 2;
    private static final int LINE_SIZE = 1500;
    private Tabela tabela;
    private Dados dadosDePara;
    private Owners ownersDePara;
    private boolean clob;

    /**
     *
     * @param t
     * @param d
     * @param o
     */
    public CargaTabela(Tabela t, Dados d, Owners o) {
        this.tabela = t;
        this.dadosDePara = d;
        this.ownersDePara = o;
        this.clob = false;
    }

    /**
     *
     * @return
     */
    public boolean temCLOB() {
        return this.clob;
    }

    /**
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public String geraUpdateClob(ResultSet rs) throws SQLException {
        String sql = "";
        StringBuilder cmd = new StringBuilder();
        StringBuilder declare = new StringBuilder();
        StringBuilder setValores = new StringBuilder();
        StringBuilder setUpdate = new StringBuilder();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            if ((rs.getMetaData().getColumnType(i) != 2005)
                    || (rs.getClob(i) == null)
                    || (rs.getClob(i).length() <= 0L)) {
                continue;
            }
            Clob valor = rs.getClob(i);
            String v = valor.getSubString(1L, (int) valor.length());
            try {
                v = formataValor(v, rs.getMetaData().getColumnName(i), 2005, 2);
            } catch (ParseException ex) {
                v = null;
            }
            String nomeVariavel = "";
            if (("v_cl_" + rs.getMetaData().getColumnName(i)).length() > 25) {
                nomeVariavel = ("v_cl_" + rs.getMetaData().getColumnName(i)).substring(0, 25);
            } else {
                nomeVariavel = "v_cl_" + rs.getMetaData().getColumnName(i);
            }
            declare.append(nomeVariavel);
            declare.append(" clob := empty_clob();\n");
            setValores.append(" ").append(nomeVariavel);
            setValores.append(" := '").append(v).append("';\n");
            setUpdate.append(" ").append(rs.getMetaData().getColumnName(i));
            setUpdate.append(" = ").append(nomeVariavel).append(",\n");
        }

        if (declare.length() > 0) {
            if (setUpdate.length() > 0) {
                setUpdate.delete(setUpdate.length() - 2, setUpdate.length());
            }
            cmd.append("declare\n");
            cmd.append(declare);
            cmd.append("begin\n");
            cmd.append(setValores);
            cmd.append("update ").append(this.tabela.getNome()).append("\n");
            cmd.append("set ").append(setUpdate).append("\n");
            cmd.append(geraWhere(this.tabela)).append(";\nend;\n");

            sql = cmd.toString();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                if (rs.getObject(i) == null) {
                    sql = sql.replaceAll("\\$\\{" + rs.getMetaData().getColumnName(i) + "\\}", "NULL");
                } else {
                    try {
                        String valor = formataValor(rs.getString(i), rs.getMetaData().getColumnName(i), rs.getMetaData().getColumnType(i), 1);

                        sql = sql.replaceAll("\\$\\{" + rs.getMetaData().getColumnName(i) + "\\}", this.dadosDePara.getDePara(rs.getMetaData().getColumnName(i) + "|" + valor, valor));
                    } catch (ParseException ex) {
                        Logger.getLogger(CargaTabela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return sql;
    }

    /**
     *
     * @param pValor
     * @param columnName
     * @param type
     * @param action
     * @return
     * @throws ParseException
     */
    private String formataValor(String pValor, String columnName, int type, int action) throws ParseException {
        String oValor = pValor.toString().replace("'", "''");
        if ((type == 12) || (type == -1) || (type == 1) || (type == 2005)) {
            oValor = this.ownersDePara.getDeParaMixed(oValor.toString());
        }

        if (type == 2005) {
            if (action == 1) {
                oValor = FormataTipo.Formata(oValor, type);
            }

            oValor = oValor
                    .replaceAll("(?<!\\&)\\&(?!\\&)", "' || CHR(38) || '")
                    .replaceAll("\n", "' || CHR(10) ||\n '")
                    .replaceAll("\r", "' || CHR(13) || '");
            oValor = oValor.replaceAll("\\&\\&", "' || CHR(38) || CHR(38) || '");
        } else {
            oValor = FormataTipo.Formata(oValor, type);
            oValor = oValor
                    .replace("\\", "\\\\")
                    .replace("{", "\\{")
                    .replace("}", "\\}")
                    .replace("(", "\\(")
                    .replace(")", "\\)")
                    .replace("$", "\\$")
                    .replace("[", "\\[")
                    .replace("]", "\\]")
                    .replace("*", "\\*")
                    .replace("%", "\\%")
                    .replace("'", "\\'")
                    .replaceAll("(?<!\\&)\\&(?!\\&)", "' || CHR(38) || '")
                    .replaceAll("\n", "' || CHR(10) ||\n '")
                    .replaceAll("\r", "' || CHR(13) || '");
        }

        return oValor;
    }

    /**
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    public String geraInsertValores(ResultSet rs)
            throws SQLException {
        String sql = geraComandoInsert();
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            int type = rs.getMetaData().getColumnType(i);
            if (type == 2005) {
                this.clob = true;
            }
            if (rs.getObject(i) == null) {
                sql = sql.replaceAll("\\$\\{" + rs.getMetaData().getColumnName(i) + "\\}", "NULL");
            } else {
                try {
                    String valor = "";
                    if (rs.getMetaData().getColumnTypeName(i).equalsIgnoreCase("DATE")) {
                        valor = formataValor(rs.getTimestamp(i).toString(), rs.getMetaData().getColumnName(i), type, 1);
                    } else {
                        valor = formataValor(rs.getObject(i).toString(), rs.getMetaData().getColumnName(i), type, 1);
                    }
                    sql = sql.replaceAll("\\$\\{" + rs.getMetaData().getColumnName(i) + "\\}", valor);
                } catch (ParseException ex) {
                    Logger.getLogger(CargaTabela.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    System.err.println("Erro ao montar Insert: " + sql.toString());
                }
            }
        }
        return sql;
    }

    /**
     *
     * @return
     */
    public String geraComandoSelect() {
        StringBuilder cmd = new StringBuilder();

        Tabela t = this.tabela;

        cmd.append("select ");
        for (Coluna c : t.getColunas()) {
            cmd.append(c.getNome()).append(",\n");
        }
        cmd.delete(cmd.length() - 2, cmd.length());
        cmd.append("\nfrom ").append(t.getNome());

        return cmd.toString();
    }

    /**
     *
     * @return
     */
    public String geraComandoInsert() {
        StringBuilder cmd = new StringBuilder();
        int lineSize = 0;

        Tabela t = this.tabela;
        cmd.append("insert into ").append(t.getNome()).append("(");
        lineSize = cmd.length();
        for (Coluna c : t.getColunas()) {
            cmd.append(c.getNome()).append(", ");
            lineSize += c.getNome().length() + ", ".length();
            if (lineSize > 1500) {
                cmd.append('\n');
                lineSize = 0;
            }
        }
        cmd.delete(cmd.length() - 2, cmd.length());

        cmd.append(")\n values (");

        lineSize = " values (".length();

        for (Coluna c : t.getColunas()) {
            cmd.append("${").append(c.getNome()).append("}, ");
            lineSize += ("${" + c.getNome() + "}, ").length();
            if (lineSize > 1500) {
                cmd.append('\n');
                lineSize = 0;
            }
        }
        cmd.delete(cmd.length() - 2, cmd.length());
        cmd.append(");");

        return cmd.toString();
    }

    /**
     *
     * @return
     */
    public String getComandoUpdate() {
        StringBuilder cmd = new StringBuilder();

        Tabela t = this.tabela;
        cmd.append("\n\nupdate ").append(t.getNome()).append("\nset ");

        for (Coluna c : t.getColunas()) {
            if (!t.getPrimaryKey().exists(c.getNome())) {
                cmd.append(c.getNome()).append(" = ${").append(c.getNome()).append("},\n");
            }
        }
        cmd.delete(cmd.length() - 2, cmd.length());

        cmd.append(geraWhere(t));

        cmd.append(";");

        return cmd.toString();
    }

    /**
     *
     * @param t
     * @return
     */
    private String geraWhere(Tabela t) {
        StringBuilder cmd = new StringBuilder();
        if ((t.getPrimaryKey() != null)
                && (t.getPrimaryKey().getColunas().length > 0)) {
            cmd.append("where 1 = 1 ");
            for (String c : t.getPrimaryKey().getColunas()) {
                cmd.append("and ").append(c).append(" = ${").append(c).append("}\n");
            }
        }

        return cmd.toString();
    }
}
