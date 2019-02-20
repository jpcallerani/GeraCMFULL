package extract;

import depara.Owners;
import grant.Grant;
import grant.Grantable;
import grant.Privilegio;
import java.sql.SQLException;
import objetos.Objeto;

public class Grants extends Extract {

    private Owners ownersDePara;

    public Grants(Objeto[] Objs, String np, Owners o)
            throws ClassNotFoundException, SQLException {
        super(Objs, np);
        this.ownersDePara = o;
    }

    public String geraComandoCriacao(Objeto o) {
        StringBuilder cmd = new StringBuilder();

        if (((Grantable) o).getGrants().length > 0) {
            cmd.append("prompt ===================================\n")
                    .append("prompt   Grants para o objeto ").append(o.getNome()).append("\n")
                    .append("prompt ===================================\n\n");

            for (Grant g : ((Grantable) o).getGrants()) {
                StringBuilder sbGrantOption = new StringBuilder();
                StringBuilder sbNoGrantOption = new StringBuilder();
                for (Privilegio p : g.getPrivilegios()) {
                    if (p.isGrantable()) {
                        sbGrantOption.append(p.getNome()).append(',');
                    } else {
                        sbNoGrantOption.append(p.getNome()).append(',');
                    }
                }
                if (sbGrantOption.length() > 0) {
                    cmd.append("grant ").append(sbGrantOption.toString().substring(0, sbGrantOption.toString().length() - 1));
                    cmd.append(" on ").append(o.getNome()).append(" to ").append(this.ownersDePara.getDePara(g.getGrantee(), false)).append(" with grant option;\n");
                }
                if (sbNoGrantOption.length() > 0) {
                    cmd.append("grant ").append(sbNoGrantOption.toString().substring(0, sbNoGrantOption.toString().length() - 1));
                    cmd.append(" on ").append(o.getNome()).append(" to ").append(this.ownersDePara.getDePara(g.getGrantee(), false)).append(";\n");
                }
            }
            cmd.append("\n\n");
        }

        return cmd.toString();
    }
}
