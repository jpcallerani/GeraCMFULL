package loader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import oracle.jdbc.OracleDriver;
import swinstaladortaxit.Database.TrConnection;

/**
 * Classe de conexão com o banco de dados Oracle.
 */
public class TrOracleConnection extends TrConnection {

    private Connection _conn;
    private String _tag;
    private String _version;
    private ResultSet v_resultset_result;
    private PreparedStatement v_prepared_statement_st;

    public TrOracleConnection() {
    }

    public String get_tag() {
        return _tag;
    }

    public String get_version() {
        return _version;
    }

    private void set_version() throws SQLException {
        CallableStatement v_callablestatement_cst;

        v_callablestatement_cst = this._conn.prepareCall("begin dbms_utility.db_version(?,?); end;");
        v_callablestatement_cst.registerOutParameter(1, java.sql.Types.VARCHAR);
        v_callablestatement_cst.registerOutParameter(2, java.sql.Types.VARCHAR);
        v_callablestatement_cst.executeQuery();

        this._version = v_callablestatement_cst.getString(1);

        v_callablestatement_cst.close();
    }

    private void set_tag() throws SQLException {
        String v_string_oracle;

        v_resultset_result = this.Query("select * from V$VERSION where BANNER like '%Oracle%' and rownum=1");

        while (v_resultset_result.next()) {
            v_string_oracle = v_resultset_result.getString("BANNER").toUpperCase();

            if (v_string_oracle.contains("11G")) {
                this._tag = "11g";
            }
            if (v_string_oracle.contains("10G")) {
                this._tag = "10g";
            }
            if (v_string_oracle.contains("9I")) {
                this._tag = "9i";
            }
            if (v_string_oracle.contains("8I")) {
                this._tag = "8i";
            }
        }
        v_resultset_result.close();
    }

    /**
     *
     */
    public void CloseResultSet() {
        try {
            if (this.v_resultset_result != null) {
                this.v_resultset_result.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return @throws SQLException
     */
    @Override
    public Connection Connect() throws SQLException {

        Properties v_properties_prop;

        v_properties_prop = new Properties();
        v_properties_prop.put("user", this._username);
        v_properties_prop.put("password", this._password);

        DriverManager.registerDriver(new OracleDriver());
        _conn = DriverManager.getConnection(getURL(), v_properties_prop);

        this.set_tag();
        this.set_version();

        return _conn;

    }

    /**
     *
     * @return @throws SQLException
     */
    public Connection ConnectAux() throws SQLException {

        Properties v_properties_prop;

        v_properties_prop = new Properties();
        v_properties_prop.put("user", this._username);
        v_properties_prop.put("password", this._password);

        DriverManager.registerDriver(new OracleDriver());

        _conn = DriverManager.getConnection(getURL(), v_properties_prop);

        return _conn;

    }

    /**
     * Fecha conexão com Oracle
     *
     */
    public void CloseStatement() {
        try {
            if (v_prepared_statement_st != null) {
                this.v_prepared_statement_st.close();
                v_prepared_statement_st = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fecha conexão com o banco.
     */
    @Override
    public void Close() {
        try {
            if (this._conn != null && !this._conn.isClosed()) {
                this._conn.rollback();
                this._conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executa SELECT na base.
     *
     * @param query
     * @return
     * @throws SQLException
     */
    @Override
    public ResultSet Query(String query) throws SQLException {

        v_prepared_statement_st = this._conn.prepareStatement(query);
        v_resultset_result = v_prepared_statement_st.executeQuery();
        return v_resultset_result;
    }

    /**
     * Executa update na base.
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public int Update(String query) throws SQLException {

        v_prepared_statement_st = this._conn.prepareStatement(query);
        int i = v_prepared_statement_st.executeUpdate();

        return i;
    }

    /**
     * Executa Insert na base.
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public int Insert(String query) throws SQLException {

        v_prepared_statement_st = this._conn.prepareStatement(query);
        int i = v_prepared_statement_st.executeUpdate();

        return i;
    }

    /**
     *
     * @param query
     * @return
     * @throws SQLException
     */
    public int Object(String query) throws SQLException {
        String v_string_object;
        String[] v_string_linha;

        // retira quebras de linhas e comentarios para poder compilar
        v_string_linha = query.split("[\n\r]");
        v_string_object = "";
        for (int i = 0; i < (v_string_linha.length - 1); i++) {
            if (!v_string_linha[i].contains("--") && !v_string_linha[i].equals("")) {
                v_string_object = v_string_object + " " + v_string_linha[i];
            }
        }

        v_prepared_statement_st = this._conn.prepareStatement(v_string_object);
        v_prepared_statement_st.setEscapeProcessing(false);

        int i = v_prepared_statement_st.executeUpdate();

        return i;
    }

    /**
     *
     * @param query
     * @param parameters
     * @throws SQLException
     */
    public void ExecStoredProcedure(String query, ArrayList parameters) throws SQLException {

        int index = 0;
        OracleStoredProcedureParameter v_oraclestparam_param;
        CallableStatement v_callablestatement_st;

        System.out.println();
        System.out.println("DB Stored Call: " + query);

        if (parameters == null) {
            v_callablestatement_st = this._conn.prepareCall(query);
            v_callablestatement_st.execute();
        } else {
            Iterator e = parameters.iterator();
            v_callablestatement_st = this._conn.prepareCall(query);

            while (e.hasNext()) {
                index++;

                v_oraclestparam_param = (OracleStoredProcedureParameter) e.next();

                if (v_oraclestparam_param._type == Types.VARCHAR) {
                    v_callablestatement_st.setString(index, (String) v_oraclestparam_param.getValue());
                } else if (v_oraclestparam_param._type == Types.NUMERIC) {
                    v_callablestatement_st.setInt(index, (Integer) v_oraclestparam_param.getValue());
                }

                System.out.println("Param" + index + ": " + v_oraclestparam_param.getValue());
            }

            v_callablestatement_st.execute();
        }
    }

    /**
     * Executa Stored Procedure
     */
    public class OracleStoredProcedureParameter {

        public int _type;
        public String _varchar_value;
        public int _number_value;

        public Object getValue() {
            if (_type == Types.VARCHAR) {
                return _varchar_value;
            } else if (_type == Types.NUMERIC) {
                return _number_value;
            }

            return null;
        }
    }

    /**
     * Retorna os parametros da Stored Procedure
     *
     * @param value
     * @return
     */
    public OracleStoredProcedureParameter getStoredParameter(String value) {
        OracleStoredProcedureParameter param;

        param = new OracleStoredProcedureParameter();
        param._type = Types.VARCHAR;
        param._varchar_value = value;

        return param;
    }

    /**
     * Retorna os parametros da Stored Procedure
     *
     * @param value
     * @return
     */
    public OracleStoredProcedureParameter getStoredParameter(int value) {
        OracleStoredProcedureParameter param;

        param = new OracleStoredProcedureParameter();
        param._type = Types.NUMERIC;
        param._number_value = value;

        return param;
    }

    /**
     * Retorna a instance na qual será conectado.
     *
     * @return
     */
    public String getURL() {
        try {
            String v_s_jdbc_url = "";
            String v_s_host = "";
            String v_s_tnsping;
            Process p = Runtime.getRuntime().exec("tnsping " + this._tns);
            InputStream stdoutStream = new BufferedInputStream(p.getInputStream());

            StringBuilder buffer = new StringBuilder();
            for (;;) {
                int c = stdoutStream.read();
                if (c == -1) {
                    break;
                }
                buffer.append((char) c);
            }
            v_s_tnsping = buffer.toString().toUpperCase();

            stdoutStream.close();

            v_s_tnsping = v_s_tnsping.replaceAll(" ", "");

            if (v_s_tnsping.contains("(DESCRIPTION")) {
                try {
                    v_s_host = v_s_tnsping.substring(v_s_tnsping.indexOf("(DESCRIPTION"), v_s_tnsping.indexOf("OK"));
                } catch (Exception e) {
                    e.printStackTrace();
                    v_s_host = "";
                }
            }

            if (v_s_host.equals("")) {
                return v_s_host;
            } else {
                return v_s_jdbc_url = "jdbc:oracle:thin:@" + v_s_host;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return _username;
    }

    /**
     *
     * @param _username
     */
    public void setUsername(String _username) {
        this._username = _username;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return _password;
    }

    /**
     *
     * @param _password
     */
    public void setPassword(String _password) {
        this._password = _password;
    }

    /**
     *
     * @return
     */
    public String getTns() {
        return _tns;
    }

    /**
     *
     * @param _tns
     */
    public void setTns(String _tns) {
        this._tns = _tns;
    }

}
