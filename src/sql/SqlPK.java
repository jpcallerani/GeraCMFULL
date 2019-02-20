package sql;

public class SqlPK {
    
    private static StringBuffer codigo;
    
    public static String getSql() {
        codigo = new StringBuffer();
        codigo.append("select distinct u.table_name, ");
        codigo.append("        u.constraint_name, ");
        codigo.append("        u.constraint_type, ");
        codigo.append("        ucc.column_name, ");
        codigo.append("        ucc.position, ");
        codigo.append("        ui.tablespace_name, ");
        codigo.append("        u.owner, ");
        codigo.append("       ui.index_name ");
        codigo.append("from user_constraints  u, ");
        codigo.append("user_cons_columns ucc, ");
        codigo.append("user_ind_columns  uic, ");
        codigo.append("user_indexes      ui ");
        codigo.append("where u.index_name = ui.index_name(+) ");
        codigo.append("and u.table_name = ? ");
        codigo.append("and u.constraint_type = ? ");
        codigo.append("and u.constraint_name = ucc.constraint_name ");
        codigo.append("and u.table_name = ucc.table_name ");
        codigo.append("and uic.column_name(+) = ucc.column_name ");
        codigo.append("and ucc.owner = u.owner ");
        codigo.append("order by u.constraint_name, ucc.position ");
        
        return codigo.toString();
    }
}
