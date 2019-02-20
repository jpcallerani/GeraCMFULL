package grant;

import java.util.ArrayList;
import java.util.List;

public abstract class Grantable
{
  private List<Grant> grants;

  public Grantable()
  {
    this.grants = new ArrayList();
  }

  public void addGrant(Grant g)
  {
    this.grants.add(g);
  }

  public void clearGrants() {
    this.grants.clear();
  }

  public Grant[] getGrants() {
    return (Grant[])this.grants.toArray(new Grant[this.grants.size()]);
  }
}