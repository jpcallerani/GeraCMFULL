package util;

import java.util.StringTokenizer;

public class StringUtil {

    /**
     *
     * @param itens
     * @param separador
     * @return
     */
    public static String geraSqlIN(String itens, String separador) {
        StringBuilder sqlIN = new StringBuilder();
        String texto = "";
        if (itens.indexOf(separador) > 0) {
            StringTokenizer st = new StringTokenizer(itens, separador, false);
            while (st.hasMoreTokens()) {
                texto = st.nextToken();
                sqlIN.append("'").append(texto).append("',");
            }
            sqlIN.deleteCharAt(sqlIN.length() - 1);
        } else if (itens.length() > 0) {
            sqlIN.append("'").append(itens).append("'");
        }
        return sqlIN.toString();
    }

    /**
     * Função NVL para retornar outro valor quando o texto passado for nulo ou
     * vazio.
     *
     * @param text Texto a ser verificado.
     * @param nullvalue Valor a ser colocado em caso de nulidade.
     * @return Texto ou valor de nulo.
     */
    public static String nvl(String text, String nullvalue) {
        if (text == null || text.equals("")) {
            return nullvalue;
        } else {
            return text;
        }
    }
}
