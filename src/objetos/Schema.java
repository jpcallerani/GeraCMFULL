package objetos;

public class Schema {

    private String usuario;
    private String senha;
    private String tns;

    public Schema(String usuario, String senha, String tns) {
        this.usuario = usuario;
        this.senha = senha;
        this.tns = tns;
    }
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTns() {
        return tns;
    }

    public void setTns(String tns) {
        this.tns = tns;
    }
    
    
}
