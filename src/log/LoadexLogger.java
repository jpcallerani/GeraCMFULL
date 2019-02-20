package log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoadexLogger
{
  private static final int HEADER_LOGGED = 0;
  private static final int HEADER_NOT_LOGGED = 0;
  public static Logger log = Logger.getLogger("br.com.loadex");
  private static FileHandler fhLog = null;
  private static ConsoleHandler chLog = null;
  private static int headerStatus = 0;

  public static void init() throws SecurityException, IOException {
    LogManager.getLogManager().reset();
    String tt = "";
    SimpleDateFormat sp = new SimpleDateFormat("yyyyMMddHHmmss");
    tt = sp.format(new Date());

    fhLog = new FileHandler("log_" + tt + ".log");
    fhLog.setFormatter(new LogFormatter());
    fhLog.setEncoding("ISO-8859-1");

    chLog = new ConsoleHandler();
    chLog.setFormatter(new LogFormatter());
    chLog.setEncoding("ISO-8859-1");

    log.addHandler(fhLog);
    log.addHandler(chLog);

    headerStatus = 0;
  }

  public static void logHeader(String versao, String usuario)
  {
    if (headerStatus == 0) {
      log.info("------------------------------------------------------------------------\n");
      log.info("Inicio da Operação - LOADEX - [Aplicativo Versão: " + versao + "]\n");
      log.info("------------------------------------------------------------------------\n");
      try {
        log.info("Nome da máquina de execução: " + InetAddress.getLocalHost().getHostName() + "\n");
      } catch (UnknownHostException e) {
        log.info("Nome da máquina de execução: N/D\n");
      }
      log.info("Usuário: " + System.getProperty("user.name") + "\n");
      log.info("Sistema Operacional: " + System.getProperty("os.name") + " / " + System.getProperty("os.version") + " / " + System.getProperty("sun.os.patch.level") + " / " + System.getProperty("os.arch") + "\n");
      log.info("Java Runtime: " + System.getProperty("java.runtime.version") + " " + System.getProperty("java.home") + "\n");
      log.info("Banco de Dados: " + usuario + "\n");
      log.info("------------------------------------------------------------------------\n");
    }
  }
}