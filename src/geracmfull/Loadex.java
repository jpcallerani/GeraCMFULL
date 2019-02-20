package geracmfull;

import carga.Carga;
import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;
import depara.Dados;
import depara.Owners;
import extract.CheckKeys;
import extract.Extract;
import extract.ForeignKeys;
import extract.Grants;
import extract.IndexKeys;
import extract.ObjetosPLSQL;
import extract.PrimaryKeys;
import extract.Sequences;
import extract.Sinonimos;
import extract.Tabelas;
import extract.UniqueKeys;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import loader.Loader;
import log.LoadexLogger;
import objetos.Sequence;
import objetos.Sinonimo;
import objetos.Tabela;
import objetos.plsql.ObjetoPlSql;
import org.apache.commons.io.FileUtils;
import sql.Database;
import util.ComandoErro;
import util.FileUtil;
import util.IOUtil;
import util.StringUtil;
import util.Valida;

public class Loadex {

    public static final String VERSAO = "1.4.2 build 0";
    public static final String NOTABLESPACE = "SEMTABLESPACE";
    private String strConn;
    private String pathArquivos;
    private String strTabelas;
    private String strDBUser;
    private String tablespace;
    private String pathOwners;
    private String pathDados;
    private String pathConfig;
    private String pathTratamentoErro;
    private boolean trataErro;
    private boolean arquivoPorTabela;
    private boolean geraCarga;
    private boolean geraEstrutura;
    private boolean geraPLSQL;
    private StringBuilder comandoErro;
    private File path;
    private Database db;
    private ArrayList<Extract> ex;
    private Owners owners;
    private Dados dadosDePara;
    private String produto;

    /**
     *
     * @throws SecurityException
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void executa()
            throws SecurityException, IOException, SQLException, ClassNotFoundException {
        LoadexLogger.init();
        init();
        LoadexLogger.logHeader("1.4.2 build 0", this.db.getConn().getMetaData().getURL());
        LoadexLogger.log.info("\n");
        LoadexLogger.log.info("\n");
        LoadexLogger.log.info("Loadex V1.4.2 build 0\n");
        LoadexLogger.log.info("\n");
        LoadexLogger.log.info("\n");
        LoadexLogger.log.info("Inicializando...\n");

        Loader loader = null;
        try {
            loader = new Loader(this.db, this.strTabelas, isArquivoPorTabela(), this.tablespace.toUpperCase(), this.tablespace == "SEMTABLESPACE");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.path = FileUtil.criaDiretorio(this.path.getAbsolutePath() + "\\" + this.strDBUser.toUpperCase());

            ObjetoPlSql[] functions;

            ObjetoPlSql[] procedures;

            ObjetoPlSql[] packageSpecs;

            ObjetoPlSql[] packageBodies;

            ObjetoPlSql[] triggers;

            ObjetoPlSql[] types;

            ObjetoPlSql[] java;

            ObjetoPlSql[] views;

            Tabela[] tbls = null;
            this.ex = new ArrayList();

            if ((isGeraEstrutura()) || (this.geraCarga)) {
                LoadexLogger.log.info("Carregando tabelas...\n");
                tbls = loader.carregaDados();
                LoadexLogger.log.info("Carregando IDXs...\n");
                tbls = loader.carregaDadosIDX(tbls);
                LoadexLogger.log.info("Carregando PKs...\n");
                tbls = loader.carregaDadosPK(tbls);
                LoadexLogger.log.info("Carregando CHKs...\n");
                tbls = loader.carregaDadosCK(tbls);
                LoadexLogger.log.info("Carregando UNQs...\n");
                tbls = loader.carregaDadosUK(tbls);
                LoadexLogger.log.info("Carregando FKs...\n");
                tbls = loader.carregaDadosFK(tbls, this.owners);
            }

            if (isGeraEstrutura()) {
                this.ex.add(new Tabelas(tbls, "Tabelas"));

                this.ex.add(new IndexKeys(tbls, "IDX"));

                this.ex.add(new PrimaryKeys(tbls, "PKs"));

                this.ex.add(new CheckKeys(tbls, "CHKs"));

                this.ex.add(new UniqueKeys(tbls, "UNQs"));

                this.ex.add(new ForeignKeys(tbls, "FKs"));

                LoadexLogger.log.info("Carregando Sequences...\n");
                Sequence[] sequences = loader.carregaSequences();
                this.ex.add(new Sequences(sequences, "SEQUENCES"));

                LoadexLogger.log.info("Carregando Sinonimos...\n");
                Sinonimo[] sinonimos = loader.carregaSinonimos(this.owners);
                this.ex.add(new Sinonimos(sinonimos, "SYNONYMS"));

                LoadexLogger.log.info("Carregando Grants...\n");
                LoadexLogger.log.info("  - Tabelas..\n");
                tbls = (Tabela[]) loader.carregaGrants(tbls, this.owners);
                LoadexLogger.log.info("  - Sequences..\n");
                sequences = (Sequence[]) loader.carregaGrants(sequences, this.owners);
                this.ex.add(new Grants(tbls, "GRANTS_TABELAS", this.owners));
                this.ex.add(new Grants(sequences, "GRANTS_SEQUENCES", this.owners));
            }

            if (isGeraPLSQL()) {
                LoadexLogger.log.info("Carregando Functions...\n");
                functions = loader.carregaPLSQL("FUNCTION", owners);
                functions = (ObjetoPlSql[]) loader.carregaGrants(functions, this.owners);
                //
                LoadexLogger.log.info("Carregando Procedures...\n");
                procedures = loader.carregaPLSQL("PROCEDURE", owners);
                procedures = (ObjetoPlSql[]) loader.carregaGrants(procedures, this.owners);
                //
                LoadexLogger.log.info("Carregando Packages...\n");
                packageSpecs = loader.carregaPLSQL("PACKAGE", owners);
                packageSpecs = (ObjetoPlSql[]) loader.carregaGrants(packageSpecs, this.owners);
                //
                LoadexLogger.log.info("Carregando Package Bodies...\n");
                packageBodies = loader.carregaPLSQL("PACKAGE BODY", owners);
                //
                LoadexLogger.log.info("Carregando Triggers...\n");
                triggers = loader.carregaPLSQL("TRIGGER", owners);
                triggers = (ObjetoPlSql[]) loader.carregaGrants(triggers, this.owners);
                //
                LoadexLogger.log.info("Carregando Types...\n");
                types = loader.carregaPLSQL("TYPE", owners);
                types = (ObjetoPlSql[]) loader.carregaGrants(types, this.owners);
                //
                LoadexLogger.log.info("Carregando Java Source...\n");
                java = loader.carregaPLSQL("JAVA SOURCE", owners);
                java = (ObjetoPlSql[]) loader.carregaGrants(java, this.owners);
                //
                LoadexLogger.log.info("Carregando Views...\n");
                views = loader.carregaPLSQL("VIEW", owners);
                views = (ObjetoPlSql[]) loader.carregaGrants(views, this.owners);
                //
                this.ex.add(new ObjetosPLSQL(functions, "Function").setSubPasta("PLSQL"));
                this.ex.add(new ObjetosPLSQL(procedures, "Procedure").setSubPasta("PLSQL"));
                this.ex.add(new ObjetosPLSQL(packageSpecs, "Package").setSubPasta("PLSQL"));
                this.ex.add(new ObjetosPLSQL(packageBodies, "PackageBody").setSubPasta("PLSQL"));
                this.ex.add(new ObjetosPLSQL(triggers, "Trigger").setSubPasta("PLSQL"));
                this.ex.add(new ObjetosPLSQL(types, "Type").setSubPasta("PLSQL"));
                this.ex.add(new ObjetosPLSQL(java, "Java").setSubPasta("PLSQL"));
                this.ex.add(new ObjetosPLSQL(views, "View").setSubPasta("PLSQL"));

                this.ex.add(new Grants(functions, "GRANTS_FUNCTIONS", this.owners).setSubPasta("PLSQL"));
                this.ex.add(new Grants(procedures, "GRANTS_PROCEDURES", this.owners).setSubPasta("PLSQL"));
                this.ex.add(new Grants(packageSpecs, "GRANTS_PACKAGES", this.owners).setSubPasta("PLSQL"));
                this.ex.add(new Grants(triggers, "GRANTS_TRIGGERS", this.owners).setSubPasta("PLSQL"));
                this.ex.add(new Grants(types, "GRANTS_TYPES", this.owners).setSubPasta("PLSQL"));
                this.ex.add(new Grants(java, "GRANTS_JAVA", this.owners).setSubPasta("PLSQL"));
                this.ex.add(new Grants(views, "GRANTS_VIEWS", this.owners).setSubPasta("PLSQL"));
            }

            if ((isGeraEstrutura()) || (isGeraPLSQL())) {
                LoadexLogger.log.info("Gerando arquivos...\n");
                int i = 1;
                for (Extract e : this.ex) {
                    try {
                        LoadexLogger.log.info("  -" + e.getNomePasta() + "...\n");
                        e.setNomePasta(String.format("%02d", new Object[]{Integer.valueOf(i)}) + "_" + e.getNomePasta());
                        e.processa(isArquivoPorTabela(), this.path);
                        i++;
                    } catch (IOException ex) {
                        Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            if (isGeraCarga()) {
                LoadexLogger.log.info("\n");
                LoadexLogger.log.info("Gerando carga de dados. Esta operacao pode levar alguns minutos.\n");
                Carga carga = new Carga(this.db, this.dadosDePara, this.owners, isTrataErro(), this.comandoErro);
                File fSetup = new File(this.path.getAbsolutePath() + "\\Setup");
                if (fSetup.exists()) {
                    FileUtil.limpaDiretorio(fSetup.getAbsolutePath());
                }
                LoadexLogger.log.info("0%");
                int test;
                Tabela[] arrayOfTabela1 = tbls;
                int localIOException1 = arrayOfTabela1.length;
                for (test = 0; test < localIOException1; test++) {
                    Tabela t = arrayOfTabela1[test];
                    carga.geraCargaTabela(t, this.path.getAbsolutePath() + "\\Setup");
                }
            }

            LoadexLogger.log.info("\n");
            LoadexLogger.log.info(" Criando Ordem instalação. \n");
            LoadexLogger.log.info("\n");
            this.montaOrdemInstalacao();

            LoadexLogger.log.info("\n");
            LoadexLogger.log.info("*************************************\n");
            LoadexLogger.log.info("* PROCESSAMENTO FINALIZADO          *\n");
            LoadexLogger.log.info("*************************************\n");
            LoadexLogger.log.info("\n");
        } catch (IOException ex) {
            Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    private void initConfig() {
        try {
            Config conf = new Config(getPathConfig());
            setStrConn(conf.getConfig("CONNECTION", ""));
            Valida.ValidaNull(getStrConn(), "CONNECTION");
            setPathArquivos(conf.getConfig("PATH", ""));
            Valida.ValidaNull(getPathArquivos(), "PATH");
            setArquivoPorTabela(conf.getConfig("UM_ARQUIVO_POR_TABELA", "0").equals("1"));
            setStrTabelas(conf.getConfig("TABELAS", ""));
            setTablespace(conf.getConfig("TABLESPACE", ""));
            setPathOwners(conf.getConfig("OWNERS", ""));
            setPathDados(conf.getConfig("DADOS", ""));
            setTrataErro(conf.getConfig("TRATA_ERRO", "0").equals("1"));
            setPathTratamentoErro(conf.getConfig("COMANDOS_ERRO", ""));
            setProduto(conf.getConfig("PRODUTO", ""));
        } catch (FileNotFoundException ex) {
            System.err.println("Arquivo de Configuracao " + getPathConfig() + " nao encontrado!");
            Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     */
    private void montaOrdemInstalacao() throws SQLException, IOException {
        StringBuffer lista = new StringBuffer();
        String barra = "\\";

        lista.append("SPOOL script_").append(getProduto().toLowerCase()).append("_criacao.log\n");
        //
        lista.append("\n@\".").append(barra).append("define.sql\"\n\n");
        //
        lista.append("\nConn ")
                .append(owners.getDePara(db.getDbUsername(), false))
                .append("/").append(owners.getDePara(db.getDbUsername(), false).replace("USER", "PASS"))
                .append("@&&TNS;\n\n");
        //
        lista.append("prompt ==========================================\n");
        lista.append("prompt Carregando caracterset.sql\n");
        lista.append("prompt ==========================================\n");
        lista.append("@@\".").append(barra).append("caracterset.sql\"\n\n");
        lista.append("\nprompt ==========================================\n");
        //
        // Tabelas
        //
        Collection<File> arquivo = new ArrayList<File>();
        String caminho = path.getPath() + "\\01_Tabelas";
        try {
            arquivo = FileUtils.listFiles(new File(caminho), new String[]{"sql"}, true);
            for (File arquivo1 : arquivo) {
                if (arquivo1.getName().toLowerCase().endsWith(".sql") && !arquivo1.getName().contains("caracterset") && !arquivo1.getName().contains("sinonimo_base_generica")) {
                    String pastaAnterior = arquivo1.toString().replace(path + barra, "");
                    lista.append("prompt ==========================================\n" + "prompt Carregando ")
                            .append(arquivo1.getName())
                            .append("\n" + "prompt ==========================================\n");
                    lista.append("@@\".")
                            .append(barra)
                            .append("")
                            .append(pastaAnterior)
                            .append("\"\n\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (this.geraCarga) {
                    //
            // Carga
            //
            arquivo = new ArrayList<File>();
            caminho = path.getPath() + "\\Setup";
            try {
                arquivo = FileUtils.listFiles(new File(caminho), new String[]{"sql"}, true);
                for (File arquivo1 : arquivo) {
                    if (arquivo1.getName().toLowerCase().endsWith(".sql") && !arquivo1.getName().contains("caracterset") && !arquivo1.getName().contains("sinonimo_base_generica")) {
                        String pastaAnterior = arquivo1.toString().replace(path + barra, "");
                        lista.append("prompt ==========================================\n" + "prompt Carregando ")
                                .append(arquivo1.getName())
                                .append("\n" + "prompt ==========================================\n");
                        lista.append("@@\".")
                                .append(barra)
                                .append("")
                                .append(pastaAnterior)
                                .append("\"\n\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //
        // Index
        //
        arquivo = new ArrayList<File>();
        caminho = path.getPath() + "\\02_IDX";
        try {
            arquivo = FileUtils.listFiles(new File(caminho), new String[]{"sql"}, true);
            for (File arquivo1 : arquivo) {
                if (arquivo1.getName().toLowerCase().endsWith(".sql") && !arquivo1.getName().contains("caracterset") && !arquivo1.getName().contains("sinonimo_base_generica")) {
                    String pastaAnterior = arquivo1.toString().replace(path + barra, "");
                    lista.append("prompt ==========================================\n" + "prompt Carregando ")
                            .append(arquivo1.getName())
                            .append("\n" + "prompt ==========================================\n");
                    lista.append("@@\".")
                            .append(barra)
                            .append("")
                            .append(pastaAnterior)
                            .append("\"\n\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        // PK
        //
        arquivo = new ArrayList<File>();
        caminho = path.getPath() + "\\03_PKs";
        try {
            arquivo = FileUtils.listFiles(new File(caminho), new String[]{"sql"}, true);
            for (File arquivo1 : arquivo) {
                if (arquivo1.getName().toLowerCase().endsWith(".sql") && !arquivo1.getName().contains("caracterset") && !arquivo1.getName().contains("sinonimo_base_generica")) {
                    String pastaAnterior = arquivo1.toString().replace(path + barra, "");
                    lista.append("prompt ==========================================\n" + "prompt Carregando ")
                            .append(arquivo1.getName())
                            .append("\n" + "prompt ==========================================\n");
                    lista.append("@@\".")
                            .append(barra)
                            .append("")
                            .append(pastaAnterior)
                            .append("\"\n\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        // CHK
        //
        arquivo = new ArrayList<File>();
        caminho = path.getPath() + "\\04_CHKs";
        try {
            arquivo = FileUtils.listFiles(new File(caminho), new String[]{"sql"}, true);
            for (File arquivo1 : arquivo) {
                if (arquivo1.getName().toLowerCase().endsWith(".sql") && !arquivo1.getName().contains("caracterset") && !arquivo1.getName().contains("sinonimo_base_generica")) {
                    String pastaAnterior = arquivo1.toString().replace(path + barra, "");
                    lista.append("prompt ==========================================\n" + "prompt Carregando ")
                            .append(arquivo1.getName())
                            .append("\n" + "prompt ==========================================\n");
                    lista.append("@@\".")
                            .append(barra)
                            .append("")
                            .append(pastaAnterior)
                            .append("\"\n\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        // UNQs
        //
        arquivo = new ArrayList<File>();
        caminho = path.getPath() + "\\05_UNQs";
        try {
            arquivo = FileUtils.listFiles(new File(caminho), new String[]{"sql"}, true);
            for (File arquivo1 : arquivo) {
                if (arquivo1.getName().toLowerCase().endsWith(".sql") && !arquivo1.getName().contains("caracterset") && !arquivo1.getName().contains("sinonimo_base_generica")) {
                    String pastaAnterior = arquivo1.toString().replace(path + barra, "");
                    lista.append("prompt ==========================================\n" + "prompt Carregando ")
                            .append(arquivo1.getName())
                            .append("\n" + "prompt ==========================================\n");
                    lista.append("@@\".")
                            .append(barra)
                            .append("")
                            .append(pastaAnterior)
                            .append("\"\n\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        // FKs
        //
        arquivo = new ArrayList<File>();
        caminho = path.getPath() + "\\06_FKs";
        try {
            arquivo = FileUtils.listFiles(new File(caminho), new String[]{"sql"}, true);
            for (File arquivo1 : arquivo) {
                if (arquivo1.getName().toLowerCase().endsWith(".sql") && !arquivo1.getName().contains("caracterset") && !arquivo1.getName().contains("sinonimo_base_generica")) {
                    String pastaAnterior = arquivo1.toString().replace(path + barra, "");
                    lista.append("prompt ==========================================\n" + "prompt Carregando ")
                            .append(arquivo1.getName())
                            .append("\n" + "prompt ==========================================\n");
                    lista.append("@@\".")
                            .append(barra)
                            .append("")
                            .append(pastaAnterior)
                            .append("\"\n\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        // SEQUENCES
        //
        arquivo = new ArrayList<File>();
        caminho = path.getPath() + "\\07_SEQUENCES";
        try {
            arquivo = FileUtils.listFiles(new File(caminho), new String[]{"sql"}, true);
            for (File arquivo1 : arquivo) {
                if (arquivo1.getName().toLowerCase().endsWith(".sql") && !arquivo1.getName().contains("caracterset") && !arquivo1.getName().contains("sinonimo_base_generica")) {
                    String pastaAnterior = arquivo1.toString().replace(path + barra, "");
                    lista.append("prompt ==========================================\n" + "prompt Carregando ")
                            .append(arquivo1.getName())
                            .append("\n" + "prompt ==========================================\n");
                    lista.append("@@\".")
                            .append(barra)
                            .append("")
                            .append(pastaAnterior)
                            .append("\"\n\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        // SYNONYMS
        //
        arquivo = new ArrayList<File>();
        caminho = path.getPath() + "\\08_SYNONYMS";
        try {
            arquivo = FileUtils.listFiles(new File(caminho), new String[]{"sql"}, true);
            for (File arquivo1 : arquivo) {
                if (arquivo1.getName().toLowerCase().endsWith(".sql") && !arquivo1.getName().contains("caracterset") && !arquivo1.getName().contains("sinonimo_base_generica")) {
                    String pastaAnterior = arquivo1.toString().replace(path + barra, "");
                    lista.append("prompt ==========================================\n" + "prompt Carregando ")
                            .append(arquivo1.getName())
                            .append("\n" + "prompt ==========================================\n");
                    lista.append("@@\".")
                            .append(barra)
                            .append("")
                            .append(pastaAnterior)
                            .append("\"\n\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        // GRANTS TABELAS
        //
        arquivo = new ArrayList<File>();
        caminho = path.getPath() + "\\09_GRANTS_TABELAS";
        try {
            arquivo = FileUtils.listFiles(new File(caminho), new String[]{"sql"}, true);
            for (File arquivo1 : arquivo) {
                if (arquivo1.getName().toLowerCase().endsWith(".sql") && !arquivo1.getName().contains("caracterset") && !arquivo1.getName().contains("sinonimo_base_generica")) {
                    String pastaAnterior = arquivo1.toString().replace(path + barra, "");
                    lista.append("prompt ==========================================\n" + "prompt Carregando ")
                            .append(arquivo1.getName())
                            .append("\n" + "prompt ==========================================\n");
                    lista.append("@@\".")
                            .append(barra)
                            .append("")
                            .append(pastaAnterior)
                            .append("\"\n\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        // GRANTS SEQUENCES
        //
        arquivo = new ArrayList<File>();
        caminho = path.getPath() + "\\10_GRANTS_SEQUENCES";
        try {
            arquivo = FileUtils.listFiles(new File(caminho), new String[]{"sql"}, true);
            for (File arquivo1 : arquivo) {
                if (arquivo1.getName().toLowerCase().endsWith(".sql") && !arquivo1.getName().contains("caracterset") && !arquivo1.getName().contains("sinonimo_base_generica")) {
                    String pastaAnterior = arquivo1.toString().replace(path + barra, "");
                    lista.append("prompt ==========================================\n" + "prompt Carregando ")
                            .append(arquivo1.getName())
                            .append("\n" + "prompt ==========================================\n");
                    lista.append("@@\".")
                            .append(barra)
                            .append("")
                            .append(pastaAnterior)
                            .append("\"\n\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        // PLSQL
        //
        caminho = path.getPath() + "\\PLSQL";
        try {
            arquivo = FileUtils.listFiles(new File(caminho), new String[]{"sql"}, true);
            for (File arquivo1 : arquivo) {
                if (arquivo1.getName().toLowerCase().endsWith(".sql") && !arquivo1.getName().contains("caracterset") && !arquivo1.getName().contains("sinonimo_base_generica")) {
                    String pastaAnterior = arquivo1.toString().replace(path + barra, "");
                    lista.append("prompt ==========================================\n" + "prompt Carregando ")
                            .append(arquivo1.getName())
                            .append("\n" + "prompt ==========================================\n");
                    lista.append("@@\".")
                            .append(barra)
                            .append("")
                            .append(pastaAnterior)
                            .append("\"\n\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        lista.append("\n@\".").append(barra).append("compilatudo.sql\"\n\n");
        //
        lista.append("\nSPOOL OFF\nEXIT");
        //
        IOUtil.criaArqTxt(lista.toString(), path + "\\ordem_instalacao_win.sql");
        IOUtil.criaArqTxt("sqlplus /NOLOG @\"." + barra + "ordem_instalacao_win.sql\"", path + "\\Instala_win.bat");
        //
        // Copia os arquivos para pasta.
        //
        IOUtil.fazCopia(".\\Arquivos\\caracterset.sql", path + "\\caracterset.sql");
        IOUtil.fazCopia(".\\Arquivos\\compilatudo.sql", path + "\\compilatudo.sql");

    }

    /**
     *
     */
    private void init() {
        initConfig();

        initDB();

        if (this.strTabelas != null) {
            if (this.strTabelas.length() > 0) {
                if (!this.strTabelas.trim().endsWith(";")) {
                    this.strTabelas += ';';
                }
                this.strTabelas = StringUtil.geraSqlIN(this.strTabelas, ";");
            } else {
                this.strTabelas = null;
            }
        }

        if (this.tablespace == null) {
            this.tablespace = "SEMTABLESPACE";
        }

        this.path = new File(getPathArquivos());
        try {
            this.owners = new Owners(getPathOwners());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            this.dadosDePara = new Dados(getPathDados());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (getPathTratamentoErro() != null) {
            try {
                this.comandoErro = ComandoErro.getComandoErro(getPathTratamentoErro());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Loadex.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean initDB() {
        try {
            this.db = new Database(getStrConn());
            this.db.getConn();
            this.strDBUser = this.db.getDbUsername();
            return true;
        } catch (ClassNotFoundException ex) {
            System.err.println();
            System.err.println("Erro ao conectar no Banco: " + ex.getMessage());
            System.err.println();
        } catch (SQLException ex) {
            System.err.println();
            System.err.println("Erro ao conectar no Banco: " + ex.getMessage());
            System.err.println();
        }
        return false;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        JSAP js = new JSAP();
        try {
            FlaggedOption opConfig = new FlaggedOption("config")
                    .setStringParser(JSAP.STRING_PARSER)
                    .setAllowMultipleDeclarations(false)
                    .setLongFlag("config")
                    .setShortFlag('c')
                    .setRequired(false);
            opConfig.setHelp("Caminho absoluto do arquivo de configuracao");
            js.registerParameter(opConfig);

            Switch swcarga = new Switch("geracarga")
                    .setShortFlag('g')
                    .setLongFlag("gera-carga");
            swcarga.setHelp("Gera a carga de dados (Inserts) das tabelas selecionadas.");
            js.registerParameter(swcarga);

            Switch swestrutura = new Switch("geraestrutura")
                    .setShortFlag('e')
                    .setLongFlag("gera-estrutura");
            swestrutura.setHelp("Gera a estrutura das tabelas selecionadas.");
            js.registerParameter(swestrutura);

            Switch swplsql = new Switch("geraplsql")
                    .setShortFlag('p')
                    .setLongFlag("gera-plsql");
            swplsql.setHelp("Gera a estrutura de todos objetos PLSQL.");
            js.registerParameter(swplsql);

            Switch swhelp = new Switch("help")
                    .setShortFlag('h')
                    .setLongFlag(JSAP.NO_LONGFLAG);
            swhelp.setHelp("Mostra ajuda");
            js.registerParameter(swhelp);

            JSAPResult res = js.parse(args);
            if ((!res.success()) || ((!res.getBoolean("geracarga")) && (!res.getBoolean("geraestrutura")) && (!res.getBoolean("geraplsql")))) {
                if (res.contains("help")) {
                    LoadexLogger.log.info("\n");
                    LoadexLogger.log.info("Uso: java " + Loadex.class.getName());
                    LoadexLogger.log.info("                " + js.getUsage());
                    LoadexLogger.log.info("\n");
                    LoadexLogger.log.info(js.getHelp());
                    System.exit(0);
                }

                System.err.println();

                Iterator errs = res.getErrorMessageIterator();
                while (errs.hasNext()) {
                    System.err.println("Erro: " + errs.next());
                }

                System.err.println();
                System.err.println("Uso: java " + Loadex.class.getName());
                System.err.println("                " + js.getUsage());
                System.err.println();
                System.err.println(js.getHelp());
                System.exit(1);
            }

            Loadex lx = new Loadex();
            lx.setPathConfig(res.getString("config"));
            lx.setGeraCarga(res.getBoolean("geracarga"));
            lx.setGeraEstrutura(res.getBoolean("geraestrutura"));
            lx.setGeraPLSQL(res.getBoolean("geraplsql"));
            lx.executa();
            // Monta ordem instalação.

            //
        } catch (JSAPException ex) {
            LoadexLogger.log.log(Level.SEVERE, null, ex);
        } catch (SecurityException e) {
            LoadexLogger.log.log(Level.SEVERE, null, e);
        } catch (IOException e) {
            LoadexLogger.log.log(Level.SEVERE, null, e);
        } catch (SQLException e) {
            LoadexLogger.log.log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException e) {
            LoadexLogger.log.log(Level.SEVERE, null, e);
        }
    }

    public String getStrConn() {
        return this.strConn;
    }

    public void setStrConn(String strConn) {
        this.strConn = strConn;
    }

    public String getPathArquivos() {
        return this.pathArquivos;
    }

    public void setPathArquivos(String pathArquivos) {
        this.pathArquivos = pathArquivos;
    }

    public String getStrTabelas() {
        return this.strTabelas;
    }

    public void setStrTabelas(String strTabelas) {
        this.strTabelas = strTabelas;
    }

    public String getTablespace() {
        return this.tablespace;
    }

    public void setTablespace(String tablespace) {
        this.tablespace = tablespace;
    }

    public boolean isArquivoPorTabela() {
        return this.arquivoPorTabela;
    }

    public void setArquivoPorTabela(boolean arquivoPorTabela) {
        this.arquivoPorTabela = arquivoPorTabela;
    }

    public String getPathOwners() {
        return this.pathOwners;
    }

    public void setPathOwners(String pathOwners) {
        this.pathOwners = pathOwners;
    }

    public boolean isGeraCarga() {
        return this.geraCarga;
    }

    public void setGeraCarga(boolean geraCarga) {
        this.geraCarga = geraCarga;
    }

    public Dados getDadosDePara() {
        return this.dadosDePara;
    }

    public void setDadosDePara(Dados dadosDePara) {
        this.dadosDePara = dadosDePara;
    }

    public String getPathDados() {
        return this.pathDados;
    }

    public void setPathDados(String pathDados) {
        this.pathDados = pathDados;
    }

    public boolean isGeraEstrutura() {
        return this.geraEstrutura;
    }

    public void setGeraEstrutura(boolean geraEstrutura) {
        this.geraEstrutura = geraEstrutura;
    }

    public String getPathConfig() {
        return this.pathConfig;
    }

    public void setPathConfig(String pathConfig) {
        this.pathConfig = pathConfig;
    }

    public String getPathTratamentoErro() {
        return this.pathTratamentoErro;
    }

    public void setPathTratamentoErro(String pathTratamentoErro) {
        this.pathTratamentoErro = pathTratamentoErro;
    }

    public boolean isTrataErro() {
        return this.trataErro;
    }

    public void setTrataErro(boolean trataErro) {
        this.trataErro = trataErro;
    }

    public boolean isGeraPLSQL() {
        return this.geraPLSQL;
    }

    public void setGeraPLSQL(boolean geraPLSQL) {
        this.geraPLSQL = geraPLSQL;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }
}
