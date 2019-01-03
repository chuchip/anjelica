package gnu.chu.utilidades;
/*
 *<p>Titulo: cgpedven </p>
 * <p>Descripción: Clase para renderizar el grid de Pedidos ventas
 *
 * <p>Copyright: Copyright (c) 2005-2018
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 */
import gnu.chu.anjelica.ventas.PCabPedVentas;
import gnu.chu.controles.Cgrid;
import gnu.chu.interfaces.VirtualGrid;


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
     return !jt.getValString(row,PCabPedVentas.JT_TIPLIN).equals("P");
        //return  fa(col==0 && ((String) valor).startsWith("P"));             
 }
}