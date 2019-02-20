package util;

import extract.Tabelas;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtil {

    /**
     *
     * @param dir
     */
    public static void limpaDiretorio(String dir) {
        File dirs = new File(dir);
        if ((dirs.isDirectory()) && (dirs.listFiles().length > 0)) {
            File[] fs = dirs.listFiles();
            for (File f : fs) {
                limpaDiretorio(f.getAbsolutePath());
            }
        }
        dirs.delete();
    }

    /**
     *
     * @param dir
     * @return
     */
    public static File criaDiretorio(String dir) {
        File dirs = new File(dir);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        return dirs;
    }

    /**
     *
     * @param arq
     * @return
     * @throws IOException
     */
    public static File criaArquivo(String arq) throws IOException {
        File sc = new File(arq);
        sc.createNewFile();
        return sc;
    }

    /**
     *
     * @param pPathArquivos
     * @param pNomeObj
     * @param script
     * @param bAppend
     * @throws IOException
     */
    public static void geraArquivo(String pPathArquivos, String pNomeObj, StringBuilder script, boolean bAppend)
            throws IOException {
        try {
            File dirs = criaDiretorio(pPathArquivos);
            File sc = new File(dirs.getAbsolutePath() + "\\" + pNomeObj + ".sql");
            if ((sc.exists()) && (bAppend)) {
                sc = new File(dirs.getAbsolutePath() + "\\" + pNomeObj + ".sql");
            } else {
                sc = criaArquivo(dirs.getAbsolutePath() + "\\" + pNomeObj + ".sql");
            }
//            BufferedWriter bw = new BufferedWriter(new FileWriter(sc, bAppend));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sc, bAppend), "ISO-8859-1"));
            bw.write(script.toString());
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Tabelas.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("Erro criacao arquivo " + pPathArquivos + "\\" + pNomeObj + ".sql: " + ex.getMessage());
        }
        BufferedWriter bw;
        File sc;
    }

    /**
     *
     * @param pPathArquivos
     * @param pNomeObj
     * @return
     */
    public static long tamanhoArquivo(String pPathArquivos, String pNomeObj) {
        return new File(pPathArquivos + "\\" + pNomeObj + ".sql").length();
    }
}
