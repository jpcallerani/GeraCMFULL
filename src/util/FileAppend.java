package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileAppend {

    private BufferedWriter bw;
    private File arquivo;

    /**
     * 
     * @param a
     * @throws IOException 
     */
    public FileAppend(File a)
            throws IOException {
        this.arquivo = a;
//      this.bw = new BufferedWriter(new FileWriter(getArquivo()));
        this.bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(arquivo), "ISO-8859-1"));
    }

    /**
     * 
     * @param txt
     * @throws IOException 
     */
    public void append(String txt)
            throws IOException {
        if (this.bw != null) {
            this.bw.append(txt);
        }
    }

    /**
     * 
     * @throws IOException 
     */
    public void flush() throws IOException {
        this.bw.flush();
    }

    /**
     * 
     * @throws IOException 
     */
    public void close() throws IOException {
        this.bw.flush();
        this.bw.close();
    }

    /**
     * 
     * @return 
     */
    public File getArquivo() {
        return this.arquivo;
    }

    /**
     * 
     * @param arquivo 
     */
    public void setArquivo(File arquivo) {
        this.arquivo = arquivo;
    }
}
