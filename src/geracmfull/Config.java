package geracmfull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config
{
  private Properties prop;
  private String path;

  public Config(String path)
    throws FileNotFoundException, IOException
  {
    this.path = path;
    init();
  }

  private void init()
    throws FileNotFoundException, IOException
  {
    if ((this.path != null) && 
      (new File(this.path).exists())) {
      this.prop = new Properties();
      this.prop.load(new FileInputStream(this.path));
    }
  }

  public String getConfig(String de, String valorDef)
  {
    if (this.prop == null) {
      return valorDef;
    }
    return this.prop.getProperty(de, valorDef);
  }
}
