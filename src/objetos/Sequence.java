package objetos;

import grant.Grantable;

public class Sequence extends Grantable
  implements Objeto
{
  private String nome;
  private String cabecalho;
  private String maxValue;
  private String minValue;
  private String startWith;
  private int incrementBy;
  private int cache;
  private String cycleFlag;
  private String orderFlag;
  private String lastNumber;

  public String getNome()
  {
    return this.nome;
  }

  public void setNome(String nome)
  {
    this.nome = nome;
  }

  public String getCabecalho()
  {
    return this.cabecalho;
  }

  public void setCabecalho(String cabecalho)
  {
    this.cabecalho = cabecalho;
  }

  public String getMaxValue()
  {
    return this.maxValue;
  }

  public void setMaxValue(String maxValue)
  {
    this.maxValue = maxValue;
  }

  public String getMinValue()
  {
    return this.minValue;
  }

  public void setMinValue(String minValue)
  {
    this.minValue = minValue;
  }

  public String getStartWith()
  {
    return this.startWith;
  }

  public void setStartWith(String startWith)
  {
    this.startWith = startWith;
  }

  public int getIncrementBy()
  {
    return this.incrementBy;
  }

  public void setIncrementBy(int incrementBy)
  {
    this.incrementBy = incrementBy;
  }

  public int getCache()
  {
    return this.cache;
  }

  public void setCache(int cache)
  {
    this.cache = cache;
  }

  public String getCycleFlag()
  {
    return this.cycleFlag;
  }

  public void setCycleFlag(String cycleFlag)
  {
    this.cycleFlag = cycleFlag;
  }

  public String getOrderFlag()
  {
    return this.orderFlag;
  }

  public void setOrderFlag(String orderFlag)
  {
    this.orderFlag = orderFlag;
  }

  public String getLastNumber()
  {
    return this.lastNumber;
  }

  public void setLastNumber(String lastNumber)
  {
    this.lastNumber = lastNumber;
  }
}