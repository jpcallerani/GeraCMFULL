package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ComandoErro {

    public static StringBuilder getComandoErro(String path)
            throws FileNotFoundException, IOException {
        StringBuilder cmd = new StringBuilder();
        if (path != null) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(path)));
                String txt = "";
                boolean comentario = false;
                while ((txt = br.readLine()) != null) {
                    if (txt.contains("/*")) {
                        comentario = true;
                        if (txt.indexOf("/*") > 0) {
                            txt = txt.substring(0, txt.indexOf("/*"));
                            cmd.append(txt).append("\n");
                        } else {
                            txt = null;
                        }
                    }
                    if ((txt != null)
                            && (txt.contains("*/"))) {
                        comentario = false;
                        if (txt.substring(txt.indexOf("*/"), txt.length()).length() > 0) {
                            txt = txt.substring(txt.indexOf("*/") + 2, txt.length());
                        } else {
                            txt = null;
                        }
                    }

                    if ((txt != null) && (!comentario)) {
                        cmd.append(txt).append("\n");
                    }
                }
            } catch (IOException ex) {
                throw new IOException("Arquivo " + path + " nao encontrado!");
            }
        }

        return cmd;
    }
}
