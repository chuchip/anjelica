
package gnu.chu.interfaces;
/**
 * 
 * <p>Título: VirtualGrid</p>
 * <p>Descripción: Interface usado para especificar si una celda en particular
 * dentro de un grid debe ser tratada a traves de la clase miCellRender.
 * 
 * </p>
 * <p>Copyright: Copyright (c) 2005-2012
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
 * 
 * 
 */
public interface VirtualGrid
{
  public boolean getColorGrid(int row, int col, Object valor, boolean selecionado, String nombreGrid);
}
