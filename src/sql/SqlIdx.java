package sql;

public class SqlIdx {

    private static StringBuffer codigo;

    public static String getSql() {
        codigo = new StringBuffer();
        codigo.append("select distinct u.index_name, ");
        codigo.append("u.table_name, ");
        codigo.append("uidd.column_name, u.tablespace_name, uidd.column_position, u.UNIQUENESS ");
        codigo.append("from user_indexes u, ");
        codigo.append("user_ind_columns uidd ");
        codigo.append("where u.index_name = uidd.index_name ");
        codigo.append("and   u.table_name = uidd.table_name ");
        //codigo.append("and   u.uniqueness = 'NONUNIQUE' ");
        //codigo.append("and u.index_name not in (select constraint_name from user_constraints where constraint_type = 'P')");
        codigo.append("and   u.index_type = 'NORMAL' ");
        codigo.append("and   u.table_name = ? ");
        codigo.append("order by u.table_name, u.index_name, uidd.column_position ");
        return codigo.toString();
    }
}
