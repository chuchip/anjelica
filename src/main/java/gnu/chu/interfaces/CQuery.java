package gnu.chu.interfaces;

/**
 * Implementa si un control tiene posibilidad de hacer Query
 * @author gnu.chu P
 * @version 1.0
 */

public interface CQuery
{
  public void setQuery(boolean b); // Activa/desactiva todos los campos a Query

  public boolean getQuery();

  public String getStrQuery(); //Retorna la cadena StrQuery
  public String getColumnaAlias();
}
