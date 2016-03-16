package gnu.chu.eventos;

public class GridAdapter implements GridListener
{
    public GridAdapter(){}
    @Override
    public void focusGained(GridEvent event){}
    @Override
    public void focusLost(GridEvent event){}
    @Override
 	public void cambioColumna(GridEvent event)   { }
    @Override
    public void cambiaLinea(GridEvent event){}
    @Override
    public void afterCambiaLinea(GridEvent event){}
    @Override
    public boolean afterInsertaLinea(GridEvent event){return true;}
    @Override
    public boolean insertaLinea(GridEvent event) {return true;};
    @Override
    public void afterDeleteLinea(GridEvent event){}
    @Override
    public void deleteLinea(GridEvent event){}
    @Override
    public void afterCambiaLineaDis(GridEvent event){}
}
