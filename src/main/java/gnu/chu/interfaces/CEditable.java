package gnu.chu.interfaces;
/**
 *
 * <p>Título: CEditable </p>
 * <p>Descripción: Interfaz a implementar todos los objetos que deban ser
 * editables<br>
 * <p>Copyright: Copyright (c) 2005-2009
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los terminos de la Licencia Pública General de GNU seg�n es publicada por
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
 **/
public interface CEditable
{
  public void resetTexto();// Pone todos los campos a su valor defecto
  public void setText(String texto);
  public String getText();
  public java.awt.Component getErrorConf();
  public void setEnabledParent(boolean enab);
  public void setEditableParent(boolean edit);
  public void resetCambio();
  public  boolean hasCambio();
}
