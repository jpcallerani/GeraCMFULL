package objetos;

import java.util.ArrayList;

public class ForeignKey
  implements Objeto
{
  private String nome;
  private String referenceOwner;
  private String referenceConstraintName;
  private String referenceTable;
  private String deleteRule;
  private boolean deferrable;
  private boolean deferred;
  private ArrayList<String> colunas;
  private ArrayList<String> referenceCols;
  private String cabecalho;

  public ForeignKey()
  {
    this.colunas = new ArrayList();
    this.referenceCols = new ArrayList();
  }

  public String[] getReferenceCols()
  {
    return (String[])this.referenceCols.toArray(new String[this.referenceCols.size()]);
  }
  public void addReferenceCols(String col) {
    this.referenceCols.add(col);
  }

  public void addColuna(String col) {
    this.colunas.add(col);
  }

  public String getNome()
  {
    return this.nome;
  }

  public void setNome(String nome)
  {
    this.nome = nome;
  }

  public String getReferenceOwner()
  {
    return this.referenceOwner;
  }

  public void setReferenceOwner(String referenceOwner)
  {
    this.referenceOwner = referenceOwner;
  }

  public String getReferenceConstraintName()
  {
    return this.referenceConstraintName;
  }

  public void setReferenceConstraintName(String referenceConstraintName)
  {
    this.referenceConstraintName = referenceConstraintName;
  }

  public String getDeleteRule()
  {
    if (this.deleteRule != null) {
      if (this.deleteRule.equals("SET NULL"))
        return "on delete set null";
      if (this.deleteRule.equals("CASCADE"))
        return "on delete cascade";
    }
    return "";
  }

  public void setDeleteRule(String deleteRule)
  {
    this.deleteRule = deleteRule;
  }

  public boolean isDeferrable()
  {
    return this.deferrable;
  }

  public void setDeferrable(boolean deferrable)
  {
    this.deferrable = deferrable;
  }

  public String[] getColunas()
  {
    return (String[])this.colunas.toArray(new String[this.colunas.size()]);
  }

  public String getReferenceTable()
  {
    return this.referenceTable;
  }

  public void setReferenceTable(String referenceTable)
  {
    this.referenceTable = referenceTable;
  }

  public boolean isDeferred()
  {
    return this.deferred;
  }

  public void setDeferred(boolean deferred)
  {
    this.deferred = deferred;
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