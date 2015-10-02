package gnu.chu.eventos;

public class GridAdapter implements GridListener
{
    public GridAdapter(){}
    public void focusGained(GridEvent event){}
    public void focusLost(GridEvent event){}
 	public void cambioColumna(GridEvent event)   { }
    public void cambiaLinea(GridEvent event){}
    public void afterCambiaLinea(GridEvent event){}
    public boolean afterInsertaLinea(GridEvent event){return true;}
    public boolean insertaLinea(GridEvent event) {return true;};
    public void afterDeleteLinea(GridEvent event){}
    public void deleteLinea(GridEvent event){}
    public void afterCambiaLineaDis(GridEvent event){}
}
