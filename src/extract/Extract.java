package extract;

import util.FileAppend;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import objetos.Objeto;
import util.FileUtil;

public abstract class Extract {

    protected StringBuilder script = null;
    protected Objeto[] objs;
    private String nomePasta;
    private String nomeArquivo;
    private String subPasta;

    public Extract(Objeto[] objs, String np)
            throws ClassNotFoundException, SQLException {
        init();
        this.objs = objs;
        this.nomePasta = np;
        this.nomeArquivo = np;
    }

    private void init()
            throws ClassNotFoundException, SQLException {
        this.script = new StringBuilder();
    }

    public void processa(boolean pArquivoPorTabela, File pathArquivos) throws ClassNotFoundException, SQLException, IOException {
        int count = 0;

        File path = null;
        if (getSubPasta() != null) {
            path = new File(pathArquivos.getAbsolutePath() + "\\" + getSubPasta() + "\\" + getNomePasta());
        } else {
            path = new File(pathArquivos.getAbsolutePath() + "\\" + getNomePasta());
        }

        FileUtil.criaDiretorio(path.getAbsolutePath());

        FileAppend fa = null;
        if (!pArquivoPorTabela) {
            fa = new FileAppend(new File(path.getAbsolutePath() + "\\" + getNomeArquivo() + ".sql"));
        }

        for (Objeto o : this.objs) {
            this.script = new StringBuilder();

            this.script.append(geraComandoCriacao(o));

            if (this.script.length() > 0) {
                if (pArquivoPorTabela) {
                    FileUtil.geraArquivo(path.getAbsolutePath(), o.getNome(), this.script, false);

                    this.script = new StringBuilder();
                } else {
                    try {
                        fa.append(this.script.toString());
                    } catch (IOException ex) {
                        Logger.getLogger(Tabelas.class.getName()).log(Level.SEVERE, null, ex);
                        throw new IOException("Erro criacao arquivo sql (append): " + ex.getMessage());
                    }
                }
            }

            count++;
        }

        if (!pArquivoPorTabela) {
            try {
                fa.close();
            } catch (IOException ex) {
                Logger.getLogger(Tabelas.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException("Erro fechamento arquivo sql (flush+close): " + ex.getMessage());
            }
        }
    }

    public String geraComandoCriacao(Objeto o) {
        return "";
    }

    public String getNomePasta() {
        return this.nomePasta;
    }

    public void setNomePasta(String nomePasta) {
        this.nomePasta = nomePasta;
    }

    public String getNomeArquivo() {
        return this.nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getSubPasta() {
        return this.subPasta;
    }

    public Extract setSubPasta(String subPasta) {
        this.subPasta = subPasta;
        return this;
    }
}
