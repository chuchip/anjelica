package gnu.chu.controles;

import java.awt.*;
import java.awt.event.*;
import gnu.chu.sql.*;
import javax.swing.*;
import gnu.chu.interfaces.*;

/**
 *
 * <p>TÍtulo: CCheckBox </p>
 * <p>Descripción: Campo Tipo CheckBox<br>
 * <p>Copyright: Copyright (c) 2005-2016
 * Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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

public class CCheckBox  extends JCheckBox   implements CEditable, CQuery
{
  private boolean dependePadre=true;
  boolean pulsado=false;
  String chSelect = "true";
  String chNoSelect = "false";
  private boolean copia;
  CGridEditable gridEdit=null;
  private CComboBox query = new CComboBox();

  public CCheckBox()
  {
    super();
    activar();
  }
  public CCheckBox(Icon icon)
  {
    super(icon);
    activar();
  }

  public CCheckBox(Icon icon, boolean selected)
  {
    super(icon, selected);
    activar();
  }

  public CCheckBox(String text)
  {
    super(text);
    activar();
  }
  /**
   * Constructor Presenta un Texto predeterminado
   *  @param String -> texto a presentar
   *  @param boolean -> True presenta el Objeto seleccionado
   */
  public CCheckBox(String text, boolean selected)
  {
    super(text, selected);
    activar();
  }

  public CCheckBox(String sel, String nosel)
  {
    super();
    setchSelect(sel);
    setchNoSelect(nosel);

    activar();
  }
  /**
   * Constructor Presenta un Texto predeterminado
   *  @param String -> texto a presentar
   *  @param Icon -> Icono a presentar
   */
  public CCheckBox(String text, Icon icon)
  {
    super(text, icon);
    activar();
  }
  /**
   * Constructor Presenta un Texto predeterminado
   *  @param String -> texto a presentar
   *  @param Icon -> Icono a presentar
   *  @param boolean -> True presenta el Objeto seleccionado
   */
  public CCheckBox(String text, Icon icon, boolean selected)
  {
    super(text, icon, selected);
    activar();
  }

  /**
   * Constructor
   *	@param DatosTabla -> Cursor Abierto
   *	@param String -> Nombre del Campo
   *  @param String -> Valor que se considera Selected.
   *  @param String -> Valor que se considera NOSelected.
   */
  public CCheckBox(DatosTabla d, String campo, String sel, String nosel)
  {
    super();
    query.addItem("Si", sel);
    query.addItem("No", nosel);
    activar();
  }

  void activar()
  {
    query.addItem("Si", chSelect);
    query.addItem("No", chNoSelect);
    setMultiClickThreshhold(200);
    this.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        pulsado = true;
      }
    });
  }
  /**
   * Cambia los valores que se considera Selected y NoSelected
	 *  @param String -> Valor que se considera Selected.
	 *  @param String -> Valor que se considera NOSelected.
   */

  public void setchSelects(String selected,String noSelected)
  {
    chSelect=selected;
    chNoSelect=noSelected;
    query.removeAllItems();
    query.addItem("Si", selected);
    query.addItem("No", noSelected);
  }
  /**
   * Cambia el valor que se considera Selected
	 *  @param String -> Valor que se considera Selected.
   */
  public void setchSelect(String selected)
  {
    chSelect = selected;
    String oldValor = "S";
    try
    {
      oldValor = query.getValor(1);
    }
    catch (Exception j)
    {}
    query.removeAllItems();
    query.addItem("Si", selected);
    query.addItem("No", oldValor);
  }

  public String getStringSelect()
  {
    return chSelect;
  }
  public char getCharSelect()
  {
      if (chSelect.equals("true"))
          return 'S';
      return chSelect.charAt(0);
  }
  public char getCharNoSelect()
  {
      if (chNoSelect.equals("false"))
          return 'N';
      return chNoSelect.charAt(0);
  }
  public void setchNoSelect(String noSelected)
  {
    chNoSelect = noSelected;
    String oldValor = "N";
    try
    {
      oldValor = query.getValor(0);
    }
    catch (Exception j)
    {}
    query.removeAllItems();
    query.addItem("Si", oldValor);
    query.addItem("No", noSelected);
  }



  /**
   * Retorna el caracter que se considera noSelected
   */
  public String getStringNoSelect()
  {
    return chNoSelect;
  }





  public Component getErrorConf()
  {
    return null;
  }

  /**
   * Devuelve como texto si esta selecionada o no el CheckBox
   * Si esta Selecionada, devuelve el Texto considerado como selected.
   * Si no esta Selecionda, devuelve "false"
   */
  public String getSelecion()
  {
    if (isSelected())
    {
      return chSelect;
    }
    else
    {
      return chNoSelect;
    }
  }
  public char getSelecionChar()
  {
      
      if (isSelected())
          return chSelect.charAt(0);
      else
          return chNoSelect.charAt(0);
  }
  public void setSelecion(String s)
  {
    if (s==null)
        return;
    if (s.compareTo(chSelect) == 0)
      setSelected(true);
    else
      setSelected(false);
  }

  /**
   * Rutina que comprueba si el campo ha cambiado desde la ultima ejecuci�n de
   * resetCambio.
   * @return true -> Ha cambiado.
   *         false-> NO ha cambiado.
   */
    @Override
  public boolean hasCambio()
  {
    return (!copia == isSelected());
  }

  /**
   * Iguala la variable 'copia' a lo que haya actualmente en el VTextField
   * <p>
   * Si llamamos inmediatamente a la funcion hasCambio, esta devolveria true.
   *
   */
  public void resetCambio()
  {
    copia = isSelected();
  }

  /**
   * Retorna el valor Anterior
   */
  public String getValorOld()
  {
    return ( (copia) ? chSelect : chNoSelect);
  };

  /**
   * Retorna el valor Actual
   */
  public String getValorAct()
  {
    return getSelecion();
  };

  private boolean activado = true;
  private boolean activadoParent = true;

  public void setEnabled(boolean enab)
  {
    query.setEnabled(enab);
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
    * NO Hago nada. Solo para cumplir con el interface Ceditable
    * @param editable boolean
    */
   public void setEditableParent(boolean editable)
   {

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
    query.setEnabled(enab);
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

  public void resetTexto()
  {
    setSelected(false);
    pulsado=false;
  }

  public void setQuery(boolean b)
  {
    query.setQuery(b);
  }

//  public boolean isFocusTraversable()
//  {
//    return!getQuery();
//  }

  public boolean getQuery()
  {
    return query.getQuery();
  }

  @Override
  public String getStrQuery()
  {
    return query.getStrQuery();
  }

  @Override
  protected void processKeyEvent(KeyEvent ke)
  {
    if (ke.getID() == KeyEvent.KEY_PRESSED)
    {
      int pulsado = ke.getKeyCode();
      switch (ke.getKeyCode())
      {
        case KeyEvent.VK_TAB:
          procesaTab(ke);
          super.processKeyEvent(ke);
          break;
        case KeyEvent.VK_ENTER:
          procesaEnter(ke);
          super.processKeyEvent(ke);
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
          AbstractButton defaultButton = getButton(ke.getKeyCode());
          if (defaultButton != null)
          {
            defaultButton.requestFocus();
            defaultButton.doClick();
          }
          break;

        default:
          super.processKeyEvent(ke);
      }
    }
    else
    {
      super.processKeyEvent(ke);
    }
  }

  public AbstractButton getButton(int tecla)
  {
    Component yo = this;
    AbstractButton boton = null;
    if (gridEdit != null)
    {
      yo = gridEdit;
      boton = gridEdit.getButton(tecla);
      if (boton != null)
        return boton;
    }
    return estatic.getButton(tecla,this);
  }

  protected void procesaTab(KeyEvent ke)
  {
    if (gridEdit != null)
      gridEdit.procesaTab(ke);
  }

  protected void procesaEnter(KeyEvent ke)
  {
    if (gridEdit != null)
      gridEdit.procesaEnter(ke);
  }

  public void setGridEditable(CGridEditable gridEdit)
  {
    this.gridEdit = gridEdit;
  }

  @Override
  public String getColumnaAlias()
  {
    return "";
  }
  public void setPulsado(boolean pulsado)
  {
    this.pulsado=pulsado;
  }
  public void resetPulsado()
  {
    this.pulsado=false;
  }
  public boolean isPulsado()
 {
   return this.pulsado;
 }

}
