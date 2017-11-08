
package gnu.chu.utilidades;

import gnu.chu.controles.Cgrid;
import gnu.chu.interfaces.VirtualGrid;

/**
 *
 * @author cpuente
 */
public class cgpedven implements VirtualGrid
{
    Cgrid jt;
    
    public cgpedven(Cgrid jt)
    {
        this.jt=jt;
    }
 @Override
 public boolean getColorGrid(int row, int col, Object valor, boolean selecionado, String nombreGrid)
 {
     return jt.getValString(row,0).equals("P");
        //return  fa(col==0 && ((String) valor).startsWith("P"));             
 }
}