package objetos;

import grant.Grantable;
import java.util.ArrayList;
import java.util.List;

public class Tabela extends Grantable
  implements Objeto
{
  public static final String PRESERVE_ROWS = "on commit delete rows";
  public static final String DELETE_ROWS = "on commit preserve rows";
  private String nome;
  private String tablespace;
  private boolean temporary;
  private String onCommit;
  private String comentario;
  private String cabecalho;
  private List<Coluna> colunas;
  private PrimaryKey primaryKey;
  private List<ForeignKey> foreignKeys;
  private List<CheckKey> checkKeys;
  private List<UniqueKey> uniqueKeys;
  private List<IndexKey> indexKeys;

  public Tabela()
  {
    this.colunas = new ArrayList();
    this.foreignKeys = new ArrayList();
    this.checkKeys = new ArrayList();
    this.uniqueKeys = new ArrayList();
    this.indexKeys = new ArrayList();
  }

  public IndexKey[] getIndexKeys()
  {
    return (IndexKey[])this.indexKeys.toArray(new IndexKey[this.indexKeys.size()]);
  }

  public void addIndexKey(IndexKey i) {
    this.indexKeys.add(i);
  }

  public UniqueKey[] getUniqueKeys() {
    return (UniqueKey[])this.uniqueKeys.toArray(new UniqueKey[this.uniqueKeys.size()]);
  }

  public void addUniqueKey(UniqueKey uk) {
    this.uniqueKeys.add(uk);
  }

  public CheckKey[] getCheckKeys() {
    return (CheckKey[])this.checkKeys.toArray(new CheckKey[this.checkKeys.size()]);
  }

  public void addCheckKey(CheckKey ck) {
    this.checkKeys.add(ck);
  }

  public void addForeignKey(ForeignKey fk) {
    this.foreignKeys.add(fk);
  }

  public Coluna[] getColunas() {
    Coluna[] col = new Coluna[this.colunas.size()];
    return (Coluna[])this.colunas.toArray(col);
  }

  public void addColuna(Coluna c) {
    this.colunas.add(c);
  }

  public void removeColuna(Coluna c) {
    this.colunas.remove(c);
  }

  public String getNome()
  {
    return this.nome;
  }

  public void setNome(String nome)
  {
    this.nome = nome;
  }

  public String getTablespace()
  {
    return this.tablespace + "_4K";
  }

  public void setTablespace(String tablespace)
  {
    this.tablespace = tablespace;
  }

  public boolean isTemporary()
  {
    return this.temporary;
  }

  public void setTemporary(boolean temporary)
  {
    this.temporary = temporary;
  }

  public String getOnCommit()
  {
    return this.onCommit;
  }

  public void setOnCommit(String onCommit)
  {
    this.onCommit = onCommit;
  }

  public String getComentario()
  {
    return this.comentario;
  }

  public void setComentario(String comentario)
  {
    this.comentario = comentario;
  }

  public PrimaryKey getPrimaryKey()
  {
    return this.primaryKey;
  }

  public void setPrimaryKey(PrimaryKey primaryKey)
  {
    this.primaryKey = primaryKey;
  }

  public ForeignKey[] getForeignKeys()
  {
    return (ForeignKey[])this.foreignKeys.toArray(new ForeignKey[this.foreignKeys.size()]);
  }

  public String getCabecalho()
  {
    return this.cabecalho;
  }

  public void setCabecalho(String cabecalho)
  {
    this.cabecalho = cabecalho;
  }
}