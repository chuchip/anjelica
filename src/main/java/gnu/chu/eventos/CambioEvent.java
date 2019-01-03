package gnu.chu.eventos;

import java.util.EventObject;

public class CambioEvent extends EventObject
{
  protected boolean cambio;


  public CambioEvent(Object source)
  {
    super(source);
  }

}
