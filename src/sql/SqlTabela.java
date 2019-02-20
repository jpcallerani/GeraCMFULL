package sql;

public class SqlTabela {

    private static StringBuffer codigo;

    public static String getSql(String tabelas) {
        codigo = new StringBuffer();
        codigo.append("SELECT u.table_name, ");
        codigo.append("ut.column_name, ");
        codigo.append("decode(ut.data_type,'NUMBER',ut.data_type,'LONG',ut.data_type,'DATE',ut.data_type,'CLOB',ut.data_type,ut.data_type), ");
        codigo.append("decode(ut.nullable,'Y','NULL','NOT NULL'), ");
        codigo.append("u.temporary, ");
        codigo.append("u.duration, ");
        codigo.append("ut.data_default, ");
        codigo.append("ut.column_id, ");
        codigo.append("decode(ut.data_type,'CHAR',ut.char_length,'VARCHAR2',ut.char_length,'NVARCHAR2',ut.char_length,ut.data_length), ");
        codigo.append("u.tablespace_name, ");
        codigo.append("nvl(tc.COMMENTS, ' '), ");
        codigo.append("nvl(ucc.COMMENTS, ' '), ");
        codigo.append("nvl(ut.DATA_PRECISION, 0), ");
        codigo.append("ut.CHAR_USED, ");
        codigo.append("nvl(ut.DATA_SCALE,-1) ");
        codigo.append("FROM   user_all_tables  u, ");
        codigo.append("user_tab_columns ut, ");
        codigo.append("user_tab_comments tc, ");
        codigo.append("user_col_comments ucc ");
        codigo.append("WHERE  u.table_name = ut.table_name ");
        codigo.append("AND    u.table_name = tc.TABLE_NAME ");
        codigo.append("AND    tc.TABLE_TYPE = 'TABLE' ");
        codigo.append("AND    ucc.TABLE_NAME = ut.TABLE_NAME ");
        codigo.append("AND    ucc.COLUMN_NAME = ut.COLUMN_NAME ");
        if (tabelas != null) {
            codigo.append("and u.table_name in (" + tabelas + ") ");  
        }
        codigo.append("order by u.table_name, ut.COLUMN_ID ");

        return codigo.toString();
    }
}
