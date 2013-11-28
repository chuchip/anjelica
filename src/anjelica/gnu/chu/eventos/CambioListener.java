package gnu.chu.eventos;

import java.util.EventListener;

public interface CambioListener extends EventListener
{
 	public void cambio(CambioEvent event);
}
