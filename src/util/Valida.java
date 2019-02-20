package util;

public class Valida {

    /**
     * 
     * @param o
     * @param item 
     */
    public static void ValidaNull(Object o, String item) {
        if (o == null) {
            throw new NullPointerException(item + " esta vazio! Verifique as configuracoes!");
        }
        if (o.toString().equals("")) {
            throw new NullPointerException(item + " esta vazio! Verifique as configuracoes!");
        }
    }
}
