package sql;

public class SqlPLSQL {

    private static StringBuffer codigo;
    private static StringBuffer codigo_column;

    public static String getSql(String tipoObjeto) {
        codigo = new StringBuffer();
        if ("VIEW".equals(tipoObjeto)) {
            codigo.append("select uv.view_name, 0, uv.text, nvl(tc.COMMENTS,'') ");
            codigo.append("from user_views uv, user_tab_comments tc ");
            codigo.append("where uv.VIEW_NAME = tc.TABLE_NAME ");
            codigo.append("order by view_name");
        } else {
            codigo.append("select name, line, text ");
            codigo.append("from user_source ");
            codigo.append("where type = ? ");
            if (tipoObjeto.equalsIgnoreCase("TYPE")) {
                codigo.append("and name not like '%SYS_%' ");
            }
            codigo.append("order by name, line");
        }

        return codigo.toString();
    }

    public static String getViewColumns(String objName) {
        codigo_column = new StringBuffer();
        codigo_column.append("select col.column_name, col.comments ");
        codigo_column.append("from user_tab_columns tab, user_col_comments col ");
        codigo_column.append("where col.table_name = '" + objName + "' ");
        codigo_column.append("and tab.table_name = col.table_name ");
        codigo_column.append("and col.column_name = tab.column_name ");
        codigo_column.append("order by tab.column_id ");

        return codigo_column.toString();
    }
}
