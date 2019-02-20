package objetos;

public class Coluna {

    private int id;
    private String nome;
    private String tipo;
    private int tamanho;
    private String nullable;
    private String valorDefault;
    private String comment;
    private String char_used;
    private int data_scale;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getTamanho() {
        return this.tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public String getNullable() {
        return this.nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public String getValorDefault() {
        return this.valorDefault;
    }

    public void setValorDefault(String valorDefault) {
        if (valorDefault != null) {
            valorDefault = valorDefault.trim();
        }
        this.valorDefault = valorDefault;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getChar_used() {
        return char_used;
    }

    public void setChar_used(String char_used) {
        this.char_used = char_used;
    }

    public int getData_scale() {
        return data_scale;
    }

    public void setData_scale(int data_scale) {
        this.data_scale = data_scale;
    }
}
