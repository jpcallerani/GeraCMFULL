/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swinstaladortaxit.Database; 

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

/**
 * Representa uma conexão genérica com a base de dados.
 */
abstract public class TrConnection {

    public TrConnection() {
    }

    
    
    protected String _username;
    protected String _password;
    protected String _tns;    
    
    abstract public Connection Connect() throws SQLException;
    abstract public void Close() throws SQLException;
    abstract public ResultSet Query(String query) throws SQLException;
    
    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public String get_tns() {
        return _tns;
    }

    public void set_tns(String _tns) {
        this._tns = _tns;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }
    
}
