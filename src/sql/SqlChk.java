package sql;

public class SqlChk {

    private static StringBuffer codigo;

    public static String getSql() {
        codigo = new StringBuffer();
        codigo.append("select u.constraint_name, ");
        codigo.append("u.search_condition ");
        codigo.append("from   user_constraints u ");
        codigo.append("where  u.constraint_type = 'C' and u.generated = 'USER NAME' ");
        codigo.append("and    u.table_name = ? ");
        codigo.append("order by u.table_name, u.constraint_name");
        return codigo.toString();
    }
}
