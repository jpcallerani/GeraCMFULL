package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author jopaulo
 *
 */
@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class IOUtil {

    //
    private Vector pastas;

    //
    public IOUtil() {
        pastas = new Vector();
    }

    //
    public static void limpaPasta(String pasta) {
        //
        try {
            File[] pst = new File(pasta).listFiles();

            for (File f : pst) {
                if (f.isDirectory()) {
                    if ((f.listFiles().length > 0)) {
                        limpaPasta(f.getAbsolutePath());
                    }
                }
                f.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //
    }

    /**
     * Deleta a pasta e todo seu contúdo!
     *
     * @param f
     */
    public static void deletaPasta(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; ++i) {
                deletaPasta(files[i]);
            }
        }
        f.delete();
    }

    /**
     *
     * @param pastaOrigem
     * @param pastaDestino
     * @param filtro
     */
    public void copiaPasta(String pastaOrigem, String pastaDestino, Vector filtro) {
        File[] origem = new File(pastaOrigem).listFiles();

        File destino = new File(pastaDestino);

        for (int i = 0; i < origem.length; i++) {
            if (isAllowed(filtro, origem[i].getName())) {
                if (!new File(pastaDestino).exists()) {
                    new File(pastaDestino).mkdirs();
                }
                if (origem[i].isDirectory()) {
                    new File(pastaDestino + "\\" + origem[i].getName()).mkdir();
                    this.copiaPasta(origem[i].getAbsolutePath(), pastaDestino + "\\" + origem[i].getName(), filtro);
                } else {
                    fazCopia(origem[i].getAbsolutePath(), pastaDestino + "\\" + origem[i].getName());
                    if (!pastas.contains(pastaDestino)) {
                        pastas.add(pastaDestino);
                    }
                }
            }
        }
    }

    /**
     *
     * @param filtro
     * @param filename
     * @return
     */
    private boolean isAllowed(Vector filtro, String filename) {
        for (int i = 0; i < filtro.size(); i++) {
            if (filename.toLowerCase().endsWith(filtro.get(i).toString().toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param versaoAnt
     * @param versaoPasta
     * @return
     */
    private boolean incluiPasta(String versaoAnt, String versaoPasta) {
        int v = -1, va = -1, r = -1, ra = -1, p = -1, pa = -1, b = -1, ba = -1;
        if (versaoPasta.contains("tag_")) {
            versaoPasta = versaoPasta.substring(versaoPasta.indexOf("tag_"));
            if (versaoPasta.contains("\\")) {
                versaoPasta = versaoPasta.substring(0, versaoPasta.indexOf("\\"));
            }
            Scanner vp = new Scanner(versaoPasta);
            vp.useDelimiter("_");
            String txt;
            while (vp.hasNext()) {
                txt = vp.next();
                if (txt.startsWith("V")) {
                    v = Integer.parseInt(txt.substring(1));
                }
                if (txt.startsWith("R")) {
                    r = Integer.parseInt(txt.substring(1));
                }
                if (txt.startsWith("P")) {
                    p = Integer.parseInt(txt.substring(1));
                }
                if (txt.startsWith("B")) {
                    b = Integer.parseInt(txt.substring(1));
                }
            }
            vp = new Scanner(versaoAnt);
            vp.useDelimiter("_");
            while (vp.hasNext()) {
                txt = vp.next();
                if (txt.startsWith("V")) {
                    va = Integer.parseInt(txt.substring(1));
                }
                if (txt.startsWith("R")) {
                    ra = Integer.parseInt(txt.substring(1));
                }
                if (txt.startsWith("P")) {
                    pa = Integer.parseInt(txt.substring(1));
                }
                if (txt.startsWith("B")) {
                    ba = Integer.parseInt(txt.substring(1));
                }
            }
        } else {
            return true;
        }
        //
        if ((v >= 0) && (va >= 0)) {
            if (v < va) {
                return false;
            }
        }
        if ((r >= 0) && (ra >= 0)) {
            if (r < ra) {
                return false;
            }
        }
        if ((p >= 0) && (pa >= 0)) {
            if (p < pa) {
                return false;
            }
        }
        if ((b >= 0) && (ba >= 0)) {
            if (b < ba) {
                return false;
            }
        }
        //
        return true;
    }

    /**
     *
     * @param pasta
     * @param destino
     * @param v
     * @param vAnt
     */
    private void pastaVersoes(String pasta, String destino, Vector v, String vAnt) {
        File[] dirs = new File(pasta).listFiles();
        for (File d : dirs) {
            if (d.isDirectory()) {
                if ((d.getAbsolutePath().contains("\\Versoes\\")) && (incluiPasta(vAnt, d.getAbsolutePath()))) {
                    this.copiaPasta(d.getAbsolutePath(), destino + "\\001_Incrementais", v);
                } else {
                    pastaVersoes(d.getAbsolutePath(), destino, v, vAnt);
                }
            }
        }
    }

    /**
     *
     * @param pasta
     * @param destino
     * @param pastaProcurada
     * @param v
     * @param vAnt
     */
    public void pastaEspecifica(String pasta, String destino, String pastaProcurada, Vector v, String vAnt) {
        if (pastaProcurada.equalsIgnoreCase("head")) {
            pastaVersoes(pasta, destino, v, vAnt);
        } else {
            File[] dirs = new File(pasta).listFiles();

            for (File dir : dirs) {
                if (dir.isDirectory()) {
                    if (dir.getName().equalsIgnoreCase(pastaProcurada)) {
                        this.copiaPasta(dir.getAbsolutePath(), destino + "\\001_Incrementais", v);
                    } else {
                        this.pastaEspecifica(dir.getAbsolutePath(), destino, pastaProcurada, v, vAnt);
                    }
                }
            }
        }
    }

    /**
     *
     * @param pasta
     * @param destino
     * @param v
     */
    public void percorrePastas(String pasta, String destino, Vector v) {
        File[] dirs = new File(pasta).listFiles();

        for (File dir : dirs) {
            if ((dir.isDirectory()) && (!dir.isHidden()) && (this.isAllowed(v, dir.getAbsolutePath()))) {
                if (this.hasDirs(dir.getAbsolutePath(), v)) {
                    this.percorrePastas(dir.getAbsolutePath(), destino, v);
                } else {
                    this.copiaPasta(dir.getAbsolutePath(), destino + "\\" + dir.getName(), v);
                }
            }
        }
    }

    /**
     *
     * @param diretorio
     * @param v
     * @return
     */
    private boolean hasDirs(String diretorio, Vector v) {
        File[] files = new File(diretorio).listFiles();
        for (File f : files) {
            if ((f.isDirectory()) && (this.isAllowed(v, f.getName()))) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param diretorio
     * @param v
     * @return
     */
    private boolean hasFiles(String diretorio, Vector v) {
        File[] files = new File(diretorio).listFiles();
        for (File f : files) {
            if ((f.isFile()) && (this.isAllowed(v, f.getName()))) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param dir
     * @param nomePasta
     * @param v
     * @return
     */
    public boolean temPasta(String dir, String nomePasta, Vector v) {

        File[] files = new File(dir).listFiles();

        for (File f : files) {
            if ((f.isDirectory()) && (f.getName().toUpperCase().equals(nomePasta.toUpperCase()))) {
                if (this.hasFiles(f.getAbsolutePath(), v)) {
                    return true;
                } else {
                    temPasta(f.getAbsolutePath(), nomePasta, v);
                }
            }
        }
        return false;
    }

    /**
     *
     * @param dir
     * @param v
     * @return
     */
    public static List listPasta(String dir, List v) {

        File[] files = new File(dir).listFiles();

        for (File f : files) {
            v.add(f);
        }
        return v;
    }

    /**
     *
     * @param dir
     * @param v
     * @return
     */
    public String listArquivo(String dir, Vector filtro) {

        File[] files = new File(dir).listFiles();

        for (File f : files) {
            if (isAllowed(filtro, f.getPath())) {
                return f.getName();
            }
        }
        return null;
    }

    /**
     * Copia um arquivo de um local para outro
     *
     * @param inFile Arquivo que deseja copiar
     * @param outFile Para onde deseja copiar
     * @return
     */
    public static boolean fazCopia(String inFile, String outFile) {
        InputStream is = null;
        OutputStream os = null;
        byte[] buffer;
        boolean success = true;
        try {
            is = new FileInputStream(inFile);
            os = new FileOutputStream(outFile);
            buffer = new byte[is.available()];
            is.read(buffer);
            os.write(buffer);
        } catch (IOException e) {
            success = false;
        } catch (OutOfMemoryError e) {
            success = false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
            }
        }
        return success;
    }

    /**
     *
     * @return
     */
    public Vector getPastas() {
        return pastas;
    }

    /**
     *
     * @param texto
     * @param caminho
     * @throws IOException
     */
    public static void criaArqTxt(String texto, String caminho) throws IOException {
        File f = new File(caminho);
        if (!f.exists()) {
            f.createNewFile();
        }
        BufferedWriter wr = new BufferedWriter(new FileWriter(f));
        if (caminho.contains("win") && caminho.contains("Instala_")) {
            wr.write("set nls_lang=AMERICAN_AMERICA.WE8ISO8859P1");
            wr.newLine();
        }
        wr.write(texto);
        wr.flush();
        wr.close();
    }

    /**
     * Cria um arquivo zipado
     *
     * @param arquivoZip
     * @param arquivos
     * @throws ZipException
     * @throws IOException
     */
    public void criarZip(File arquivoZip, File[] arquivos) throws ZipException, IOException {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        // setArquivoZipAtual( null );
        try {
            // adiciona a extensão .zip no arquivo, caso não exista
            if (!arquivoZip.getName().toLowerCase().endsWith(".zip")) {
                arquivoZip = new File(arquivoZip.getAbsolutePath() + ".zip");
            }
            if (!arquivoZip.exists()) {
                arquivoZip.createNewFile();
            }

            fos = new FileOutputStream(arquivoZip);
            bos = new BufferedOutputStream(fos, 2048);
            List listaEntradasZip = criarZip(bos, arquivos);
			// setArquivoZipAtual( arquivoZip );
            // return listaEntradasZip;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     *
     * @param os
     * @param arquivos
     * @return
     * @throws ZipException
     * @throws IOException
     */
    private List criarZip(OutputStream os, File[] arquivos) throws ZipException, IOException {
        if (arquivos == null || arquivos.length < 1) {
            throw new ZipException("Adicione ao menos um arquivo ou diretório");
        }
        List listaEntradasZip = new ArrayList();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(os);
            for (int i = 0; i < arquivos.length; i++) {
                String caminhoInicial = arquivos[i].getParent();
                List novasEntradas = adicionarArquivoNoZip(zos, arquivos[i], caminhoInicial);
                if (novasEntradas != null) {
                    listaEntradasZip.addAll(novasEntradas);
                }
            }
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (Exception e) {
                }
            }
        }
        return listaEntradasZip;
    }

    /**
     * Adicionar arquivo no ZIP
     */
    private List adicionarArquivoNoZip(ZipOutputStream zos, File arquivo, String caminhoInicial) throws IOException {
        List listaEntradasZip = new ArrayList();
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        byte buffer[] = new byte[2048];
        try {
            // diretórios não são adicionados
            if (arquivo.isDirectory()) {
                // recursivamente adiciona os arquivos dos diretórios abaixo
                File[] arquivos = arquivo.listFiles();
                for (int i = 0; i < arquivos.length; i++) {
                    List novasEntradas = adicionarArquivoNoZip(zos, arquivos[i], caminhoInicial);
                    if (novasEntradas != null) {
                        listaEntradasZip.addAll(novasEntradas);
                    }
                }
                return listaEntradasZip;
            }
            String caminhoEntradaZip = null;
            int idx = arquivo.getAbsolutePath().indexOf(caminhoInicial);
            if (idx >= 0) {
				// calcula os diretórios a partir do diretório inicial
                // isso serve para não colocar uma entrada com o caminho
                // completo
                caminhoEntradaZip = arquivo.getAbsolutePath().substring(idx + caminhoInicial.length() + 1);
            }
            ZipEntry entrada = new ZipEntry(caminhoEntradaZip);
            zos.putNextEntry(entrada);
            zos.setMethod(ZipOutputStream.DEFLATED);
            fis = new FileInputStream(arquivo);
            bis = new BufferedInputStream(fis, 2048);
            int bytesLidos = 0;
            while ((bytesLidos = bis.read(buffer, 0, 2048)) != -1) {
                zos.write(buffer, 0, bytesLidos);
            }
            listaEntradasZip.add(entrada);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception e) {
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                }
            }
        }
        return listaEntradasZip;
    }

    /**
     *
     * @param nome
     * @param conteudo
     * @throws IOException
     */
    public static void criaArquivo(String nome, String conteudo) throws IOException {
        File f = new File(nome);
        if (!f.exists()) {
            f.createNewFile();
        }
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f), "ISO-8859-1"));
        w.write(conteudo);
        w.close();
        w = null;
    }
}
