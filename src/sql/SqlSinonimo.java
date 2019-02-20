package sql;

public class SqlSinonimo {

    private static StringBuffer codigo;

    public static String getSql() {
        codigo = new StringBuffer();
        codigo.append("select synonym_name, ");
        codigo.append("table_owner, ");
        codigo.append("table_name ");
        codigo.append("from user_synonyms");
        return codigo.toString();
    }
}
