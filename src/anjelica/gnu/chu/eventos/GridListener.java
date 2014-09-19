package gnu.chu.eventos;

import java.util.EventListener;

public interface GridListener extends EventListener
{
 	public void cambioColumna(GridEvent event);
    public void cambiaLinea(GridEvent event);
    public void afterCambiaLinea(GridEvent event);
    public boolean afterInsertaLinea(GridEvent event);
    public void afterDeleteLinea(GridEvent event);
    public void deleteLinea(GridEvent event);
    public void afterCambiaLineaDis(GridEvent event);
}
