package gnu.chu.eventos;
/*
<p>Título: GridEvent</p>
 * <p>Descripción: Clase con eventos para el CGrideditable.
 * </p>
 * <p>Copyright: Copyright (c) 2005-2011
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU segun es publicada por
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
 * @author chuchi P.
 * @version 3.0
 */
import java.util.EventObject;

public class GridEvent extends EventObject
{
  private int colError=-1;
  private boolean puedeBorrarLinea=true;
  private int col,linea,colNueva;
  private boolean focusGained=false;
  private boolean focusLost=false;
  public boolean getPuedeBorrarLinea()
  {
      return puedeBorrarLinea;
  }
  
  /**
   * Establecer a  true si el grid ha ganado foco (o sea se acaba de entrar al grid)
   * @param focoGanado 
   */ 
  public void setFocusGained(boolean focoGanado)
  {
      focusGained=focoGanado;
  }
  /**
   * Devolvera true si el grid ha ganado foco (o sea se acaba de entrar al grid)
   * 
   * @return  true si ha ganado el foco alguno de los campos y no viene de un campo del grid
   */ 
  public boolean getFocusGained()
  {
      return focusGained;
  }
  /**
   * Establecer a  true si el grid ha ganado foco (o sea se acaba de entrar al grid)
   * @param focoGanado 
   */ 
  public void setFocusLost(boolean focoPerdido)
  {
      focusLost=focoPerdido;
  }
  /**
   * Devolvera true si el grid ha ganado foco (o sea se acaba de entrar al grid)
   * 
   * @return  true si ha ganado el foco alguno de los campos y no viene de un campo del grid
   */ 
  public boolean getFocusLost()
  {
      return focusLost;
  }
  /**
   * Poner despues de haber llamado a deleteLinea para que se pueda o no
   * cambiar de linea.
   * Por defecto este valor = true;
   * @param puedeBorrarLinea
   */
  public void setPuedeBorrarLinea(boolean puedeBorrarLinea)
  {
      this.puedeBorrarLinea=puedeBorrarLinea;
  }
  public void setColumna(int columna)
  {
      this.col=columna;
  }

  public void setLinea(int linea)
  {
      this.linea=linea;
  }
  public void setColNueva(int colNueva)
  {
      this.colNueva=colNueva;
  }
  public int getLinea()
  {
      return linea;
  }
  public int getColumna()
  {
      return col;
  }
  public int getColNueva()
  {
      return colNueva;
  }
  public int getColError()
  {
      return colError;
  }
  /**
   * LLamar despues de un evento de cambio linea, para establecer si 
   * puede cambiar de linea (-1) o hay un error en la columna mandada.
   * @param colError  Si es -1, no hay error. >=0 Columna con error.
   */
  public void setColError(int colError)
  {
      this.colError=colError;
  }
  public GridEvent(Object source)
  {
    super(source);
  }

}
