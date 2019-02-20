package depara;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import util.StringUtil;

public class Owners {

    private Properties prop;
    private String path;
    private Set<Object> owners = null;

    public Owners(String path)
            throws FileNotFoundException, IOException {
        this.path = path;
        init();
    }

    /**
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void init()
            throws FileNotFoundException, IOException {
        if ((this.path != null)
                && (new File(this.path).exists())) {
            this.prop = new Properties();
            this.prop.load(new FileInputStream(this.path));
            this.owners = this.prop.keySet();
        }
    }

    /**
     * 
     * @param de
     * @return 
     */
    public String getDePara(String de) {
        if (this.prop == null) {
            return de;
        }
        return StringUtil.nvl(this.prop.getProperty(de), "&&USUARIO_SOMBRA_USER") + ".";
    }

    /**
     * 
     * @param de
     * @param addDot
     * @return 
     */
    public String getDePara(String de, boolean addDot) {
        if (this.prop == null) {
            return de;
        }
        String ret = this.prop.getProperty(de) == null ? de : this.prop.getProperty(de);
        if (addDot) {
            ret = ret + ".";
        }
        return ret;
    }

    /**
     * 
     * @param txt
     * @return 
     */
    public String getDeParaMixed(String txt) {
        if (this.prop == null) {
            return txt;
        }
        String result = txt;
        boolean addDot = false;
        Iterator it = this.owners.iterator();
        while (it.hasNext()) {
            String searchStr = it.next().toString();
            addDot = false;
            if (txt.toUpperCase().contains(searchStr.toUpperCase())) {
                String nextChar = null;
                try {
                    nextChar = txt.substring(txt.toUpperCase().indexOf(searchStr.toUpperCase()) + searchStr.length(), txt.toUpperCase().indexOf(searchStr.toUpperCase()) + searchStr.length() + 1);
                } catch (StringIndexOutOfBoundsException ex) {
                    nextChar = null;
                }
                if ((nextChar != null)
                        && (".".equals(nextChar))) {
                    addDot = true;
                }

                result = txt.replaceAll("(?i)\\b" + searchStr + "\\b", getDePara(searchStr, addDot));

                if (result.length() == getDePara(searchStr, addDot).length()) {
                    break;
                }
            }
        }
        return result;
    }

    public Set<Object> getOwners() {
        return owners;
    }
}
