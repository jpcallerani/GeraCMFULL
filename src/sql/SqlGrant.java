package sql;

public class SqlGrant
{
  private static StringBuffer codigo;

  public static String getSql()
  {
    codigo = new StringBuffer();
    codigo.append("select distinct GRANTEE, ");
    codigo.append("null, ");
    codigo.append("PRIVILEGE, ");
    codigo.append("GRANTABLE, ");
    codigo.append("HIERARCHY ");
    codigo.append("from   USER_TAB_PRIVS ");
    codigo.append("where  TABLE_NAME = ? ");
    codigo.append("and grantee <> SYS_CONTEXT('USERENV','SESSION_USER') ");
    codigo.append("order  by GRANTEE, PRIVILEGE ");

    return codigo.toString();
  }
}