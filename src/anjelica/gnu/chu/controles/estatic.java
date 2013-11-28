package gnu.chu.controles;
/**
 *
 * <p>Título: estatic </p>
 * <p>Descripción: Clase que devuelve el boton que se debera pulsar ante 
 * un evento de teclas.</p>
 * <p>Copyright: Copyright (c) 2005-2012
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,ed
 *  pero SIN NINGUNA GARANTIA, incluso sin la garantía MERCANTIL implícita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Véase la Licencia Pública General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * 
 * @version  1.0
 *
 */

import java.awt.Component;
import java.awt.Container;
import javax.swing.AbstractButton;

public abstract class estatic
{
  public static AbstractButton getButton(int tecla,Component padre)
  {
    AbstractButton boton = null;
    try
    {
      Container c1 = padre.getParent();
      if (c1 instanceof gnu.chu.interfaces.CContenedor)
      {
        boton = ( (gnu.chu.interfaces.CContenedor) c1).getButton(tecla);
        return boton;
      }
    }
    catch (Exception k)
    {}
    return null;
  }
  public static AbstractButton getButtonPanel(int tecla,java.util.Hashtable htButton,Component padre)
    {
      AbstractButton bot = (AbstractButton) htButton.get("" + tecla);
      if (bot == null)
      {
        try
        {
          Container c1 = padre.getParent();
          if ( c1 instanceof gnu.chu.interfaces.CContenedor )
           bot = ( (gnu.chu.interfaces.CContenedor) c1).getButton(tecla);
         else
           bot=null;

        }
        catch (Exception k)
        {
          bot = null;
        }
      }
      return bot;
   }

}
