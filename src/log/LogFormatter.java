package log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter
{
  public String format(LogRecord log)
  {
    SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
    String level = log.getLevel().toString();
    if (level.equals("SEVERE")) {
      level = "ERRO";
    }
    return "[" + sp.format(new Date()) + "] - [" + level + "] -> " + log.getMessage();
  }
}