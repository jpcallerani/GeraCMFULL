package carga;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormataTipo {

    public static String Formata(Object o, int tipo)
            throws ParseException {
        StringBuilder sb = null;
        o = o == null ? "${NULL}" : o;
        switch (tipo) {
            case 2:
                sb = new StringBuilder(o.toString());
                break;
            case 4:
                sb = new StringBuilder(o.toString());
                break;
            case -5:
                sb = new StringBuilder(o.toString());
                break;
            case 6:
                sb = new StringBuilder(o.toString());
                break;
            case 12:
                sb = getValorFormatado(o.toString());
                break;
            case -9:
                sb = getValorFormatado(o.toString());
                break;
            case 2005:
                sb = new StringBuilder("empty_clob()");
                break;
            case 2004:
                sb = new StringBuilder("empty_blob()");
                break;
            case 1:
                sb = getValorFormatado(o.toString());
                break;
            case 91:
                sb = getValorFormatadoData(o);
                break;
            case 8:
                sb = new StringBuilder(o.toString());
                break;
            case -1:
                sb = getValorFormatado(o.toString());
                break;
            case 93:
                sb = getValorFormatadoDateTime(o);
                break;
            default:
                sb = new StringBuilder(o.toString());
        }
        try {
            return sb.toString();
        } catch (NullPointerException e) {
        }
        return null;
    }

    private static StringBuilder getValorFormatado(String valor) {
        StringBuilder sb = new StringBuilder();
        if (valor.equals("${NULL}")) {
            sb.append("NULL");
        } else {
            sb.append("'").append(valor).append("'");
        }
        return sb;
    }

    /**
     *
     * @param valor
     * @return
     * @throws ParseException
     */
    private static StringBuilder getValorFormatadoDateTime(Object valor) throws ParseException {
        StringBuilder sb = new StringBuilder();
        if (valor.equals("NULL")) {
            sb.append("NULL");
        } else {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date data = f.parse(valor.toString());
            String dbFormat = "";
            Calendar c = Calendar.getInstance();
            c.setTime(data);
            if ((c.get(11) == 0) && (c.get(12) == 0)) {
                f = new SimpleDateFormat("yyyy-MM-dd");
                dbFormat = "'YYYY-MM-DD'";
            } else {
                f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dbFormat = "'YYYY-MM-DD HH24:MI:SS'";
            }
            sb.append("TO_DATE('").append(f.format(data)).append("', ").append(dbFormat).append(")");
        }
        return sb;
    }

    /**
     *
     * @param valor
     * @return
     * @throws ParseException
     */
    private static StringBuilder getValorFormatadoData(Object valor) throws ParseException {
        StringBuilder sb = new StringBuilder();
        if (valor.equals("NULL")) {
            sb.append("NULL");
        } else {
            SimpleDateFormat f;
            if (valor.toString().contains("00:00")) {
                f = new SimpleDateFormat("yyyy-MM-dd");
            } else if (valor.toString().contains(":")) {
                f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } else {
                f = new SimpleDateFormat("yyyy-MM-dd");
            }
            Date data = f.parse(valor.toString());
            String dbFormat = "";
            Calendar c = Calendar.getInstance();
            c.setTime(data);
            if ((c.get(11) != 0) && (c.get(12) != 0)) {
                f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                dbFormat = "'DD/MM/YYYY HH24:MI:SS'";
            } else {
                f = new SimpleDateFormat("dd/MM/yyyy");
                dbFormat = "'DD/MM/YYYY'";
            }
            sb.append("TO_DATE('").append(f.format(data)).append("', ").append(dbFormat).append(")");
        }
        return sb;
    }
}
