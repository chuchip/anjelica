package gnu.chu.controles;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

/**
 *
 * <p>Título: CButton </p>
 * <p>Descripción: Campo Tipo JButton<br>
 * <p>Copyright: Copyright (c) 2005-2011
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
 * @author chuchiP
 * @version 2.0
 *
 */

public class CButton extends JButton implements gnu.chu.interfaces.CEditable,TableCellRenderer
{
  private boolean activado = true;
  private boolean activadoParent = true;
  private boolean dependePadre=true;
  public CButton()
  {
    super();
    iniciar();
  }

  /**
   * Crea un boton con un dibujo
   *
   * @param i  el dibujo que visualizara el boton
   */
  public CButton(Icon icon)
  {
    super(icon);
    iniciar();

  }

  /**
   * Crea un boton con un texto
   *
   * @param s el texto que visualizara el boton
   */
  public CButton(String s)
  {
    super(s);
    iniciar();

  }

  /**
   * Crea un boton con un dibujo
   *
   * @param s el texto que visualizara el boton
   * @param icon el dibujo que visualizara el boton
   */
  public CButton(String s, Icon icon)
  {
    super(s, icon);
    
    
    iniciar();

  }

  @Override
  public void resetTexto()
  {} // Pone todos los campos a su valor defecto

  public java.awt.Component getErrorConf()
  {
    return null;
  }

    @Override
  public void setEnabled(boolean enab)
  {
    activado = enab;
    if (activado)
    {
      if (activadoParent)
        super.setEnabled(true);
      return;
    }
    super.setEnabled(false);
  }
 /**
     * Indica si depende del padre (normalmente un CPanel) para ponerse enabled/disabled.
     * Normalmente cuando el padre es puesto a disabled el CTextField tambien es puesto y viceversa.
     * 
     * @param dependPadre 
     */
    public void setDependePadre(boolean dependPadre)
    {
      dependePadre=dependPadre;    
    }
    public boolean getDependePadre()
    {
      return dependePadre;
    }
  public void setEnabledParent(boolean enab)
  {
    if (! dependePadre)
      return;
    activadoParent = enab;
    if (!enab)
    {
      if (super.isEnabled())
      {
        super.setEnabled(false);
      }
    }
    else
    {
      if (!super.isEnabled() && activado)
        super.setEnabled(true);
    }
  }
  /**
   * NO Hago nada. Solo para cumplir con el interface Ceditable
   * @param editable boolean
   */
  @Override
  public void setEditableParent(boolean editable)
  {

  }

  @Override
  public void resetCambio(){}
  @Override
  public  boolean hasCambio(){return false;}

  private void iniciar()
  {
    setMargin(new Insets(0, 0, 0, 0));
    this.addKeyListener(new KeyAdapter()
    {
            @Override
      public void keyPressed(KeyEvent e)
      {
        AbstractButton  defaultButton;
        if (! e.isAltDown())
        {
          switch (e.getKeyCode())
          {
            case KeyEvent.VK_ENTER:
              if (e.isControlDown())
              {
                defaultButton = getDefaultButton();
                if (defaultButton != null)
                {
                  defaultButton.requestFocus();
                  defaultButton.doClick();
                }
              }
              break;
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_F2:
            case KeyEvent.VK_F4:
            case KeyEvent.VK_F5:
            case KeyEvent.VK_F6:
            case KeyEvent.VK_F7:
            case KeyEvent.VK_F8:
            case KeyEvent.VK_F9:
            case KeyEvent.VK_F10:
              defaultButton = estatic.getButton(e.getKeyCode(),CButton.this);
              if (defaultButton != null)
              {
                defaultButton.requestFocus();
                defaultButton.doClick();
              }
              break;
          }
        }
      }
    });
    //setMultiClickThreshhold(200);
  }

  public AbstractButton getDefaultButton()
  {
    return estatic.getButton(KeyEvent.VK_ENTER,this);
  }
  public Component getTableCellRendererComponent(JTable table, Object value,
     boolean isSelected, boolean hasFocus, int row, int column) {
   if (isSelected) {
     setForeground(table.getSelectionForeground());
     setBackground(table.getSelectionBackground());
   } else {
     setForeground(table.getForeground());
     setBackground(UIManager.getColor("Button.background"));
   }
   setText((value == null) ? "" : value.toString());
   return this;
 }

}
