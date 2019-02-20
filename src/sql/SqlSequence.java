package sql;

public class SqlSequence {

    private static StringBuffer codigo;

    public static String getSql() {
        codigo = new StringBuffer();
        codigo.append("select sequence_name, ");
        codigo.append(" min_value, ");
        codigo.append(" max_value, ");
        codigo.append(" increment_by, ");
        codigo.append("cycle_flag, ");
        codigo.append("order_flag, ");
        codigo.append("cache_size, ");
        codigo.append("last_number ");
        codigo.append(" from user_sequences ");
        return codigo.toString();
    }
}
