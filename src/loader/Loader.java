package loader;

import depara.Owners;
import grant.Grant;
import grant.Grantable;
import grant.Privilegio;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.LoadexLogger;
import objetos.CheckKey;
import objetos.Coluna;
import objetos.ForeignKey;
import objetos.IndexKey;
import objetos.Objeto;
import objetos.PrimaryKey;
import objetos.Sequence;
import objetos.Sinonimo;
import objetos.Tabela;
import objetos.UniqueKey;
import objetos.plsql.ObjetoPlSql;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;
import sql.Database;
import sql.SqlChk;
import sql.SqlFK;
import sql.SqlGrant;
import sql.SqlIdx;
import sql.SqlPK;
import sql.SqlPLSQL;
import sql.SqlSequence;
import sql.SqlSinonimo;
import sql.SqlTabela;

public class Loader {

    private Database db;
    private OracleConnection conn;
    private OracleStatement stm;
    private OracleResultSet rs;
    private OracleStatement stm2;
    private OracleResultSet rs2;
    private ArrayList<Tabela> tabela = null;
    private String tabelas;
    private boolean todasTabelas;
    private String tablespace;
    private boolean usaTablespaceBanco;

    public Loader(Database d, String tabelas, boolean todasTabelas, String tablespace, boolean usaTableSpaceTabela)
            throws ClassNotFoundException, SQLException {
        this.db = d;
        initDB();
        this.tabelas = tabelas;
        this.todasTabelas = todasTabelas;
        this.tablespace = tablespace;
        this.usaTablespaceBanco = usaTableSpaceTabela;
    }

    private boolean initDB()
            throws ClassNotFoundException, SQLException {
        try {
            this.conn = this.db.getConn();
            this.tabela = new ArrayList();
            return true;
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundException("Erro ao conectar no Banco: " + ex.getMessage());
        } catch (SQLException ex) {
            throw new SQLException("Erro ao conectar no Banco: " + ex.getMessage());
        }
    }

    /**
     *
     * @param tipoObjeto
     * @param owners
     * @return
     */
    public ObjetoPlSql[] carregaPLSQL(String tipoObjeto, Owners owners) {
        List plsql = new ArrayList();
        OraclePreparedStatement pstm = null;
        OraclePreparedStatement pstm_column = null;
        try {
            pstm = (OraclePreparedStatement) this.conn.prepareStatement(SqlPLSQL.getSql(tipoObjeto));

            if (!"VIEW".equals(tipoObjeto)) {
                pstm.setString(1, tipoObjeto);
            }

            this.rs = ((OracleResultSet) pstm.executeQuery());
            if (this.rs != null) {
                ObjetoPlSql oPlsql = null;
                StringBuilder source = new StringBuilder();
                while (this.rs.next()) {
                    if (!"VIEW".equals(tipoObjeto)) {
                        if ((oPlsql != null)
                                && (!oPlsql.getNome().equals(this.rs.getString(1)))) {
                            source.append('\n');
                            oPlsql.setSource(source.toString());
                            plsql.add(oPlsql);
                            oPlsql = null;
                            source = new StringBuilder();
                        }

                        if (oPlsql == null) {
                            oPlsql = new ObjetoPlSql();
                            oPlsql.setNome(this.rs.getString(1));
                            oPlsql.setTipo(tipoObjeto);
                        }
                        if (this.rs.getInt(2) == 1) {
                            if (tipoObjeto.equalsIgnoreCase("JAVA SOURCE")) {
                                source.append("SET DEFINE OFF");
                                source.append('\n');
                                source.append("create or replace and compile java source named ").append(oPlsql.getNome()).append(" as\n");
                            } else {
                                source.append("create or replace ");
                            }
                        }
                        source.append(this.rs.getString(3));
                    } else {

                        String view_name = this.rs.getString(1);
                        String view_text = this.rs.getString(3);
                        StringBuffer sb_comments = new StringBuffer();

                        pstm_column = (OraclePreparedStatement) this.conn.prepareStatement(SqlPLSQL.getViewColumns(view_name));

                        ResultSet rs_column = ((OracleResultSet) pstm_column.executeQuery());

                        String column = "";
                        while (rs_column.next()) {
                            column += "\"" + rs_column.getString("COLUMN_NAME") + "\",\n";
                        if (rs_column.getString("COMMENTS") != null) {
                          sb_comments.append("comment on column ")
                                    .append(view_name)
                                    .append(".")
                                    .append(rs_column.getString("COLUMN_NAME"))
                                    .append(" is '")
                                    .append(rs_column.getString("COMMENTS"))
                                    .append("';\n");
                            }

                        }

                        column = column.substring(0, column.length() - 2);

                        source = new StringBuilder();
                        oPlsql = new ObjetoPlSql();
                        oPlsql.setNome(view_name);
                        oPlsql.setTipo(tipoObjeto);
                        source.append("create or replace force view ").append(oPlsql.getNome()).append(" (" + column + ") ").append(" as\n");
                        source.append(view_text).append('\n');
                        if (view_name.contains("V_IO")) {
                            for (Object object : owners.getOwners()) {
                                if (source.toString().contains(object.toString())) {
                                    source = new StringBuilder(source.toString().replaceAll(object.toString(), owners.getDePara(object.toString(), true)));
                                    break;
                                }
                            }
                        }
                        oPlsql.setComment(this.rs.getString(4));
                        oPlsql.setComment_columns(sb_comments.toString());
                        oPlsql.setSource(source.toString());
                        view_text = "";
                        view_name = "";
                        plsql.add(oPlsql);
                    }
                }
                if (oPlsql != null) {
                    source.append('\n');
                    oPlsql.setSource(source.toString());
                    plsql.add(oPlsql);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
            LoadexLogger.log.log(Level.SEVERE, null, ex);
            try {
                if (this.rs != null) {
                    this.rs.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (this.rs != null) {
                    this.rs.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return (ObjetoPlSql[]) plsql.toArray(new ObjetoPlSql[plsql.size()]);
    }

    /**
     *
     * @param tabelas
     * @return
     */
    public Tabela[] carregaDadosIDX(Tabela[] tabelas) {
        try {
            OraclePreparedStatement pstm = (OraclePreparedStatement) this.conn.prepareStatement(SqlIdx.getSql());
            for (Tabela t : tabelas) {
                pstm.setString(1, t.getNome());
                this.rs = ((OracleResultSet) pstm.executeQuery());
                if (this.rs != null) {
                    IndexKey idx = null;
                    while (this.rs.next()) {
                        if ((idx != null)
                                && (!idx.getNome().equals(this.rs.getString(1)))) {
                            t.addIndexKey(idx);
                            idx = null;
                        }

                        if (idx == null) {
                            idx = new IndexKey();
                            idx.setNome(this.rs.getString(1));
                            idx.setTablespace(this.tablespace + "_INDEX");
                        }
                        idx.addColuna(this.rs.getString(3));
                        idx.setUNIQUESS(this.rs.getString(6));
                    }
                    if (idx != null) {
                        t.addIndexKey(idx);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabelas;
    }

    /**
     *
     * @param tabelas
     * @return
     */
    public Tabela[] carregaDadosUK(Tabela[] tabelas) {
        try {
            OraclePreparedStatement pstm = (OraclePreparedStatement) this.conn.prepareStatement(SqlPK.getSql());
            for (Tabela t : tabelas) {
                pstm.setString(1, t.getNome());
                pstm.setString(2, "U");
                this.rs = ((OracleResultSet) pstm.executeQuery());
                if (this.rs != null) {
                    UniqueKey uk = null;
                    while (this.rs.next()) {
                        if ((uk != null)
                                && (!uk.getNome().equals(this.rs.getString(2)))) {
                            t.addUniqueKey(uk);
                            uk = null;
                        }

                        if (uk == null) {
                            uk = new UniqueKey();
                            uk.setNome(this.rs.getString(2));
                            if (this.rs.getString(6) != null) {
                                uk.setTablespace(this.tablespace + "_INDEX");
                            }
                        }
                        uk.addColuna(this.rs.getString(4));
                    }
                    if (uk != null) {
                        t.addUniqueKey(uk);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabelas;
    }

    /**
     *
     * @param tabelas
     * @return
     */
    public Tabela[] carregaDadosCK(Tabela[] tabelas) {
        try {
            OraclePreparedStatement pstm = (OraclePreparedStatement) this.conn.prepareStatement(SqlChk.getSql());
            for (Tabela t : tabelas) {
                pstm.setString(1, t.getNome());
                this.rs = ((OracleResultSet) pstm.executeQuery());
                if (this.rs != null) {
                    CheckKey ck = null;
                    while (this.rs.next()) {
                        ck = new CheckKey();
                        ck.setNome(this.rs.getString(1));
                        ck.setCondicao(this.rs.getString(2));
                        t.addCheckKey(ck);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabelas;
    }

    /**
     *
     * @param tabelas
     * @param owners
     * @return
     */
    public Tabela[] carregaDadosFK(Tabela[] tabelas, Owners owners) {
        try {
            OraclePreparedStatement pstm = (OraclePreparedStatement) this.conn.prepareStatement(SqlFK.getSql());
            for (Tabela t : tabelas) {
                pstm.setString(1, t.getNome());
                this.rs = ((OracleResultSet) pstm.executeQuery());
                if (this.rs != null) {
                    ForeignKey fk = null;
                    while (this.rs.next()) {
                        if (this.rs.getInt(8) == 1) {
                            if (fk != null) {
                                t.addForeignKey(fk);
                            }
                            fk = new ForeignKey();
                            fk.setNome(this.rs.getString(2));
                            fk.setReferenceOwner(this.rs.getString(5));
                            fk.setReferenceConstraintName(this.rs.getString(6));
                            fk.setDeleteRule(this.rs.getString(7));
                            fk.setDeferrable(this.rs.getString(9).equals("DEFERRABLE"));
                            fk.setDeferred(this.rs.getString(10).equals("DEFERRED"));

                            this.stm2 = ((OracleStatement) this.conn.createStatement(1004, 1007));
                            this.rs2 = ((OracleResultSet) this.stm.executeQuery(SqlFK.getSqlRef(fk.getReferenceOwner(), fk.getReferenceConstraintName())));

                            if (this.rs2 != null) {
                                while (this.rs2.next()) {
                                    fk.setReferenceTable(this.rs2.getString(2));
                                    fk.addReferenceCols(this.rs2.getString(1));
                                }
                                this.rs2.close();
                            }
                            fk.setReferenceOwner(owners.getDePara(fk.getReferenceOwner()));
                        }
                        fk.addColuna(this.rs.getString(4));
                    }
                    if (fk != null) {
                        t.addForeignKey(fk);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabelas;
    }

    /**
     *
     * @param tabelas
     * @return
     */
    public Tabela[] carregaDadosPK(Tabela[] tabelas) {
        try {
            OraclePreparedStatement pstm = (OraclePreparedStatement) this.conn.prepareStatement(SqlPK.getSql());
            for (Tabela t : tabelas) {
                pstm.setString(1, t.getNome());
                pstm.setString(2, "P");
                this.rs = ((OracleResultSet) pstm.executeQuery());
                if (this.rs != null) {
                    PrimaryKey pk = null;
                    while (this.rs.next()) {
                        if (pk == null) {
                            pk = new PrimaryKey();
                            pk.setNome(this.rs.getString(2));
                            pk.setIndex(this.rs.getString(8));
                            pk.setTablespace(this.tablespace + "_INDEX");
                        }
                        pk.addColuna(this.rs.getString(4));
                    }
                    t.setPrimaryKey(pk);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabelas;
    }

    /**
     *
     * @param ts
     * @param owners
     * @return
     */
    public Objeto[] carregaGrants(Objeto[] ts, Owners owners) {
        ResultSet rsIn = null;
        OraclePreparedStatement pstm = null;
        Privilegio priv;
        try {
            pstm = (OraclePreparedStatement) this.conn.prepareStatement(SqlGrant.getSql());
            for (Objeto tbl : ts) {
                ((Grantable) tbl).clearGrants();
                pstm.setString(1, tbl.getNome());
                rsIn = (OracleResultSet) pstm.executeQuery();
                if (rsIn != null) {
                    Grant g = null;
                    priv = null;
                    while (rsIn.next()) {
                        if ((g != null)
                                && (!rsIn.getString(1).equals(g.getGrantee()))) {
                            ((Grantable) tbl).addGrant(g);
                            g = null;
                        }

                        if (g == null) {
                            g = new Grant();
                            g.setGrantee(rsIn.getString(1));
                            g.setObjeto(tbl);
                        }

                        priv = new Privilegio();
                        priv.setNome(rsIn.getString(3));
                        priv.setGrantable(rsIn.getString(4).equals("YES"));
                        priv.setHierarchy(rsIn.getString(5).equals("YES"));
                        g.addPrivilegio(priv);
                    }

                    if (g != null) {
                        ((Grantable) tbl).addGrant(g);
                        g = null;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
            try {
                if (rsIn != null) {
                    rsIn.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, e);
            }
        } finally {
            try {
                if (rsIn != null) {
                    rsIn.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        /*for (Objeto tbl : ts) {
         Integer localPrivilegio1 = (e = ((Grantable) tbl).getGrants().length);   
         for (priv = 0; priv < localPrivilegio1; priv++) {
         Grant grant = e[priv];
         grant.setGrantee(owners.getDePara(grant.getGrantee(), false));
         }
         }*/
        return ts;
    }

    /**
     *
     * @return
     */
    public Tabela[] carregaDados() {
        if (this.tabela.isEmpty()) {
            try {
                this.stm = ((OracleStatement) this.conn.createStatement(1004, 1007));
                this.rs = ((OracleResultSet) this.stm.executeQuery(SqlTabela.getSql(getTabelas())));
                if (this.rs != null) {
                    Tabela t = null;
                    while (this.rs.next()) {
                        if ((t != null)
                                && (!t.getNome().equals(this.rs.getString(1)))) {
                            this.tabela.add(t);
                            t = null;
                        }

                        if (t == null) {
                            t = new Tabela();
                            t.setNome(this.rs.getString(1));
                            t.setTablespace(this.tablespace + "_DATA");
                            t.setTemporary(this.rs.getString(5).equals("Y"));
                            if (t.isTemporary()) {
                                if (this.rs.getString(6).equals("SYS$TRANSACTION")) {
                                    t.setOnCommit("on commit preserve rows");
                                } else if (this.rs.getString(6).equals("SYS$SESSION")) {
                                    t.setOnCommit("on commit delete rows");
                                }
                            }
                            t.setComentario(this.rs.getString(11));
                        }
                        Coluna c = new Coluna();
                        c.setId(this.rs.getInt(8));
                        c.setNome(this.rs.getString(2));
                        c.setNullable(this.rs.getString(4));
                        c.setTipo(this.rs.getString(3));
                        if (c.getTipo().equalsIgnoreCase("NUMBER")) {
                            c.setTamanho(this.rs.getInt(13));
                        } else {
                            c.setTamanho(this.rs.getInt(9));
                        }
                        c.setChar_used(this.rs.getString(14));
                        c.setData_scale(this.rs.getInt(15));
                        c.setValorDefault(this.rs.getString(7));
                        c.setComment(this.rs.getString(12));
                        t.addColuna(c);
                    }
                    if (t != null) {
                        this.tabela.add(t);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return (Tabela[]) this.tabela.toArray(new Tabela[this.tabela.size()]);
    }

    /**
     *
     * @return
     */
    public Sequence[] carregaSequences() {
        List seqs = new ArrayList();
        try {
            this.stm = ((OracleStatement) this.conn.createStatement(1004, 1007));
            this.rs = ((OracleResultSet) this.stm.executeQuery(SqlSequence.getSql()));

            if (this.rs != null) {
                Sequence seq = null;
                while (this.rs.next()) {
                    seq = new Sequence();
                    seq.setNome(this.rs.getString(1));
                    seq.setMinValue(this.rs.getString(2));
                    seq.setMaxValue(this.rs.getString(3));
                    seq.setIncrementBy(this.rs.getInt(4));
                    seq.setCycleFlag(this.rs.getString(5));
                    seq.setOrderFlag(this.rs.getString(6));
                    seq.setCache(this.rs.getInt(7));
                    seq.setLastNumber(this.rs.getString(8));
                    seq.setStartWith(seq.getLastNumber());
                    seqs.add(seq);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (Sequence[]) seqs.toArray(new Sequence[seqs.size()]);
    }

    public Sinonimo[] carregaSinonimos(Owners owners) {
        List syns = new ArrayList();
        try {
            this.stm = ((OracleStatement) this.conn.createStatement(1004, 1007));
            this.rs = ((OracleResultSet) this.stm.executeQuery(SqlSinonimo.getSql()));

            if (this.rs != null) {
                Sinonimo syn = null;
                while (this.rs.next()) {
                    syn = new Sinonimo();
                    syn.setNome(this.rs.getString(1));
                    syn.setTableOwner(owners.getDePara(this.rs.getString(2)));
                    syn.setTableName(this.rs.getString(3));
                    syns.add(syn);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (Sinonimo[]) syns.toArray(new Sinonimo[syns.size()]);
    }

    public String getTabelas() {
        return this.tabelas;
    }

    public void setTabelas(String tabelas) {
        this.tabelas = tabelas;
    }

    public boolean isTodasTabelas() {
        return this.todasTabelas;
    }

    public void setTodasTabelas(boolean todasTabelas) {
        this.todasTabelas = todasTabelas;
    }

    public String getTablespace() {
        return this.tablespace;
    }

    public void setTablespace(String tablespace) {
        this.tablespace = tablespace;
    }

    public boolean isUsaTablespaceBanco() {
        return this.usaTablespaceBanco;
    }

    public void setUsaTablespaceBanco(boolean usaTablespaceBanco) {
        this.usaTablespaceBanco = usaTablespaceBanco;
    }
}
