package gnu.chu.controles;
/**
 *
 * <p>Título: CLabel</p>
 * <p>Descripción: Clase heredada de JLabel con un formato estandard</p>
 *
 * <p>Copyright: Copyright (c) 2008-2012
 *  <p>  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * </p>
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
 */
import java.awt.*;
import javax.swing.*;

public class CLabel    extends JLabel {


  public CLabel() {
    super();
    if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1)
      setFont(new Font("Dialog", 1, 11));
    else
      setFont(new Font("Dialog", 0, 11));
//       setFont(new Font(getFont().getName(), 0, 10));
    setForeground(Color.black);
  }

  public CLabel(String text, Icon icon, int horizontalAlignment) {
    super(text, icon, horizontalAlignment);
    if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1)
      setFont(new Font("Dialog", 1, 11));
    else
      setFont(new Font("Dialog", 0, 11));
//       setFont(new Font(getFont().getName(), 0, 10));
    setForeground(Color.black);
  }

  public CLabel(java.lang.String text, int horizontalAlignment) {
    super(text, horizontalAlignment);
    if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1)
      setFont(new Font("Dialog", 1, 11));
    else
      setFont(new Font("Dialog", 0, 11));
//       setFont(new Font(getFont().getName(), 0, 10));
    setForeground(Color.black);
  }

  public CLabel(java.lang.String text) {
    super(text);
    if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1)
      setFont(new Font("Dialog", 1, 11));
    else
      setFont(new Font("Dialog", 0, 11));
//       setFont(new Font(getFont().getName(), 0, 10));
    setForeground(Color.black);
  }

  public CLabel(Icon image, int horizontalAlignment) {
    super(image, horizontalAlignment);
    if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1)
      setFont(new Font("Dialog", 1, 11));
    else
      setFont(new Font("Dialog", 0, 11));
//       setFont(new Font(getFont().getName(), 0, 10));
    setForeground(Color.black);
  }

  public CLabel(Icon image) {
    super(image);
    if (System.getProperty("os.name").toUpperCase().indexOf("WIN") != -1)
      setFont(new Font("Dialog", 1, 11));
    else
      setFont(new Font("Dialog", 0, 11));
//       setFont(new Font(getFont().getName(), 0, 10));
    setForeground(Color.black);
  }
  /*
   * Recibe:
   *	@param DatosTabla (Cursor Abierto)
   *	@param String -> Nombre del Campo
   * @param String -> Formato del Campo.
   */

}
