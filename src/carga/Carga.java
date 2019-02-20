package carga;

import depara.Dados;
import depara.Owners;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetos.Tabela;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;
import sql.Database;
import util.FileUtil;

public class Carga {

    private Database db;
    private OracleConnection conn;
    private OracleStatement stm;
    private OracleResultSet rs;
    private Dados dadosDePara;
    private Owners ownersDePara;
    private boolean trataErro;
    private StringBuilder comandoErro;

    public Carga(Database b, Dados d, Owners o, boolean trataErro, StringBuilder comandoErro)
            throws SQLException, ClassNotFoundException {
        this.db = b;
        this.conn = this.db.getConn();
        this.dadosDePara = d;
        this.ownersDePara = o;
        this.trataErro = trataErro;
        this.comandoErro = comandoErro;
    }

    public String geraCargaTabela(Tabela t, String Caminho)
            throws IOException {
        StringBuilder cmd = new StringBuilder();
        CargaTabela ct = new CargaTabela(t, this.dadosDePara, this.ownersDePara);
        int qtdeRegistros = 0;
        try {
            this.stm = ((OracleStatement) this.conn.createStatement(1004, 1007));
            OraclePreparedStatement pstm = (OraclePreparedStatement) this.conn.prepareStatement(ct.geraComandoSelect());

            ResultSet rs = pstm.executeQuery();

            if (rs != null) {
                qtdeRegistros = 0;

                if (t.getNome().equalsIgnoreCase("INT_SYSTEM_VARIABLE")) {
                    cmd.append("set define off \n\n");
                }

                cmd.append("prompt ===================================\n");
                cmd.append("prompt  Carga tabela ").append(t.getNome()).append("\n");
                cmd.append("prompt ===================================\n");
                int count = 0;
                int seqFile = 1;
                String nomeArquivo = t.getNome();
                while (rs.next()) {
                    if (qtdeRegistros == 0) {
                        System.out.print("\nTabela " + t.getNome() + "... ");
                    }
                    qtdeRegistros++;
                    cmd.append("begin\n");
                    cmd.append(ct.geraInsertValores(rs)).append("\n");
                    cmd.append(ct.geraUpdateClob(rs)).append("\n");
                    if (this.trataErro) {
                        cmd.append("exception\n  when DUP_VAL_ON_INDEX then\n");
                        if (this.comandoErro.toString().equals("")) {
                            cmd.append("null;");
                        } else {
                            String ce = this.comandoErro.toString();
                            ce = ce.replaceAll("\\{TABELA\\}", t.getNome());
                            cmd.append(ce).append("\n");
                        }
                    }
                    cmd.append("end;\n/\n\n");
                    if ((count % 500 == 0) && (count > 1)) {
                        cmd.append("commit;\n\n");
                    }
                    count++;

                    if (cmd.length() >= 500000) {
                        FileUtil.geraArquivo(Caminho, nomeArquivo, cmd, true);
                        cmd = new StringBuilder();
                    }

                    if (FileUtil.tamanhoArquivo(Caminho, nomeArquivo) >= Long.parseLong("8388608")) {
                        cmd.append("commit;\n\n");
                        FileUtil.geraArquivo(Caminho, nomeArquivo, cmd, true);
                        cmd = new StringBuilder();
                        nomeArquivo = t.getNome() + "_" + seqFile;

                        System.out.println();
                        System.out.println("** Tabela " + t.getNome() + " parece ter grande quantidade de dados.");
                        System.out.println("O arquivo será dividido. (" + seqFile + "º arquivo)");
                        System.out.println();

                        seqFile++;
                    }
                }
                cmd.append("commit;\n\n");

                if (t.getNome().equalsIgnoreCase("INT_SYSTEM_VARIABLE")) {
                    cmd.append("set define on \n");
                }

                if (qtdeRegistros > 0) {
                    System.out.print(qtdeRegistros + " registros.");
                    FileUtil.geraArquivo(Caminho, t.getNome(), cmd, true);
                }

                cmd = new StringBuilder();

                pstm.close();

                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Carga.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cmd.toString();
    }
}
