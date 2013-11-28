package gnu.chu.controles;

/**
 *
 * <p>Título: CSPinner </p>
 * <p>Descripción: Clase  que sustituye a JSpinner, e implementa el interface CEditable</p>
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
import gnu.chu.anjelica.ventas.pdalbara;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;


public class CSpinner extends JSpinner implements gnu.chu.interfaces.CEditable
{
  private boolean activado = true;
  private boolean activadoParent = true;
  
  public  CSpinner()
  {
      super();
      iniciar();
  }
  public  CSpinner(SpinnerModel model)
  {
      super(model);
      iniciar();
  }
  private void iniciar()
  {
     ((JSpinner.DefaultEditor)this.getEditor()).getTextField().addKeyListener(new KeyAdapter()
     {
            @Override
       public void keyPressed(KeyEvent e)
       {
         AbstractButton defaultButton;
         if (!e.isAltDown())
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
               defaultButton = getButton(e.getKeyCode());
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
  }
  public AbstractButton getDefaultButton()
   {
     return getButton(KeyEvent.VK_ENTER);
   }

   public AbstractButton getButton(int tecla)
   {
     return estatic.getButton(tecla,this);
   }
    @Override
  public void resetTexto()
  { // No hace nada
  }
    @Override
  public void setText(String texto)
  {
      if (getModel() instanceof SpinnerNumberModel )
      {
          try {
              setValue(new Double(texto.trim()));
          } catch (Exception k){
               Logger.getLogger(CSpinner.class.getName()).log(Level.SEVERE, 
                   "CSpinner: Error al pasar texto: "+texto+" a numero", k);
          }
      }
      else
        setValue(texto);
  }
    @Override
  public String getText()
  {
      return getValue().toString();
  }
    @Override
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

    @Override
  public void setEnabledParent(boolean enab)
  {
    activadoParent = enab;
    if (!enab)
    {
      if (super.isEnabled())
        super.setEnabled(false);
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

  public void resetCambio(){}
  public  boolean hasCambio(){return false;}
}
