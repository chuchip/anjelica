package gnu.chu.controles;

import javax.swing.*;
import gnu.chu.interfaces.CEditable;
import java.awt.event.*;
import java.awt.*;

public class CToggleButton extends JToggleButton implements gnu.chu.interfaces.CEditable
{
  private boolean activado = true;
  private boolean activadoParent = true;

  public CToggleButton()
  {
    super();
  }

  /**
   * Creates an initially unselected toggle button
   * with the specified image but no text.
   *
   * @param icon  the image that the button should display
   */
  public CToggleButton(Icon icon)
  {
    super(icon);
  }

  /**
   * Creates a toggle button with the specified image
   * and selection state, but no text.
   *
   * @param icon  the image that the button should display
   * @param selected  if true, the button is initially selected;
   *                  otherwise, the button is initially unselected
   */
  public CToggleButton(Icon icon, boolean selected)
  {
    super(icon, selected);
  }

  /**
   * Creates an unselected toggle button with the specified text.
   *
   * @param text  the string displayed on the toggle button
   */
  public CToggleButton(String text)
  {
    super(text);
  }

  /**
   * Creates a toggle button with the specified text
   * and selection state.
   *
   * @param text  the string displayed on the toggle button
   * @param selected  if true, the button is initially selected;
   *                  otherwise, the button is initially unselected
   */
  public CToggleButton(String text, boolean selected)
  {
    super(text, selected);
  }

  /**
   * Creates a toggle button where properties are taken from the
   * Action supplied.
   *
   * @since 1.3
   */
  public CToggleButton(Action a)
  {
    super(a);
  }

  /**
   * Creates a toggle button that has the specified text and image,
   * and that is initially unselected.
   *
   * @param text the string displayed on the button
   * @param icon  the image that the button should display
   */
  public CToggleButton(String text, Icon icon)
  {
    super(text, icon);
  }

  /**
   * Creates a toggle button with the specified text, image, and
   * selection state.
   *
   * @param text the text of the toggle button
   * @param icon  the image that the button should display
   * @param selected  if true, the button is initially selected;
   *                  otherwise, the button is initially unselected
   */
  public CToggleButton(String text, Icon icon, boolean selected)
  {
    // Create the model
    super(text, icon, selected);
    iniciar();
  }

  public void resetTexto()
  {} // Pone todos los campos a su valor defecto

  public java.awt.Component getErrorConf()
  {
    return null;
  }

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
   * @todo A hacer...
   * @param edit boolean
   */
  public void setEditableParent(boolean edit)
  {

  }
  public void setEnabledParent(boolean enab)
  {
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

  public void resetCambio()
  {}

  public boolean hasCambio()
  {return false;
  }

  private void iniciar()
  {
    this.addKeyListener(new KeyAdapter()
    {
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
              defaultButton = estatic.getButton(e.getKeyCode(),CToggleButton.this);
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
    setMultiClickThreshhold(200);
  }

  public AbstractButton getDefaultButton()
  {
    return estatic.getButton(KeyEvent.VK_ENTER,this);
  }


}
