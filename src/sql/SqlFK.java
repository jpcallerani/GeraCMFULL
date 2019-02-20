package sql;

public class SqlFK {

    private static StringBuffer codigo;

    public static String getSql() {
        codigo = new StringBuffer();
        codigo.append("select distinct u.table_name, ");
        codigo.append("u.constraint_name, ");
        codigo.append("u.constraint_type, uc.column_name, ");
        codigo.append("u.r_owner, ");
        codigo.append("u.r_constraint_name, u.delete_rule, ");
        codigo.append("uc.position, ");
        codigo.append("u.DEFERRABLE, u.DEFERRED ");
        codigo.append("from   user_constraints u, user_cons_columns uc ");
        codigo.append("where  u.owner = uc.owner ");
        codigo.append("and    u.constraint_name = uc.constraint_name ");
        codigo.append("and    u.table_name = uc.table_name ");
        codigo.append("and    u.constraint_type = 'R' ");
        codigo.append("and    u.table_name = ?");
        codigo.append("order by u.table_name, u.constraint_name, uc.position ");
        return codigo.toString();
    }

    public static String getSqlRef(String owner, String pkName) {
        codigo = new StringBuffer();
        codigo.append("select c.column_name, c.table_name ");
        codigo.append("from  all_cons_columns c ");
        codigo.append("where c.owner = '" + owner + "' ");
        codigo.append("and   c.constraint_name = '" + pkName + "' ");
        codigo.append("order by position");
        return codigo.toString();
    }
}
