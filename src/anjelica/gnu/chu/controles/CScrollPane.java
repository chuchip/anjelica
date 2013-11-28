package gnu.chu.controles;

import javax.swing.*;
import java.awt.*;
import gnu.chu.interfaces.*;
import java.util.*;

public class CScrollPane extends JScrollPane implements CContenedor, CEditable,CQuery
{
  Hashtable htButton = new Hashtable();

  public CScrollPane()
  {
    this(null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
  }

  public CScrollPane(Component CScrollPane, int vsbPolicy, int hsbPolicy)
  {
    super(CScrollPane, vsbPolicy, hsbPolicy);

  }
  public CScrollPane(Component view) {
         super(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
     }

  public void setButton(int tecla, AbstractButton boton)
  {
    htButton.put("" + tecla, boton);
  }

  public AbstractButton getDefButton()
  {
     return getButton(java.awt.event.KeyEvent.VK_ENTER);
  }

  public AbstractButton getEscButton()
  {
    return getButton(java.awt.event.KeyEvent.VK_ESCAPE);
  }

  public AbstractButton getAltButton()
  {
    return getButton(java.awt.event.KeyEvent.VK_F2);
  }

  public void setDefButton(AbstractButton b)
  {
    setButton(java.awt.event.KeyEvent.VK_ENTER, b);
  }

  public void setEscButton(AbstractButton b)
  {
    setButton(java.awt.event.KeyEvent.VK_ESCAPE, b);
  }

  public void setAltButton(AbstractButton b)
  {
    setButton(java.awt.event.KeyEvent.VK_F2, b);
  }

  public void setEnabled(boolean b)
  {
    super.setEnabled(b);
    setEnabledParent(b);
  }

  public void setEnabledParent(boolean enabled)
  {
    Component[] lista = this.getViewport().getComponents();
    int n = this.getComponentCount();
    for (int i = 0; i < n; i++)
    {
      try
      {
        if (lista[i] instanceof CEditable)
          ( (CEditable) lista[i]).setEnabledParent(enabled);
      }
      catch (Exception k)
      {}
    }
  }
  public void setEditableParent(boolean editable)
   {
     Component[] lista = this.getViewport().getComponents();
     int n = this.getComponentCount();
     for (int i = 0; i < n; i++)
     {
       try
       {
         if (lista[i] instanceof CEditable)
           ( (CEditable) lista[i]).setEditableParent(editable);
       }
       catch (Exception k)
       {}
     }
   }

  /**
   * Funcion que me pone en blanco los controles que implementan VEditable
   * a un valor por defecto
   */
  public void resetTexto()
  {
    Component[] lista = this.getViewport().getComponents();
    int n = this.getComponentCount();
    for (int i = 0; i < n; i++)
    {
      try
      {
        if (lista[i] instanceof CEditable)
        {
          ( (gnu.chu.interfaces.CEditable) lista[i]).resetTexto();
        }
      }
      catch (Exception k)
      {}
    }
    return;
  }

  public void resetCambio()
  {
    Component[] lista = this.getViewport().getComponents();
    int n = this.getComponentCount();
    for (int i = 0; i < n; i++)
    {
      try
      {
        if (lista[i] instanceof CEditable)
        {
          ( (CEditable) lista[i]).resetCambio();
        }
      }
      catch (Exception k)
      {}
    }
    return;
  }

  public boolean hasCambio()
  {
    Component[] lista = this.getViewport().getComponents();
    int n = this.getComponentCount();
    for (int i = 0; i < n; i++)
    {
      try
      {
        if (lista[i] instanceof CEditable)
        {
          if ( ( (CEditable) lista[i]).hasCambio())
            return true;
        }
      }
      catch (Exception k)
      {}
    }
    return false;
  }

  /**
   * Funcion que me pone en el modo query especificado todos los
   * componentes que contiene y que implementen VEditable
   **/
  public void setQuery(boolean b)
  {
    Component[] lista = this.getViewport().getComponents();
    int n = this.getComponentCount();
    for (int i = 0; i < n; i++)
    {
      try
      {
        if (lista[i] instanceof CQuery)
        {
          ( (gnu.chu.interfaces.CQuery) lista[i]).setQuery(b);
        }
      }
      catch (Exception k)
      {}
    }
    return;
  }


  /**
   * Correccinn de enabled
   *
   public boolean ift = true;
   public boolean isFocusTraversable() {return ift;}
   public void setFT(boolean b) {
    ift = b;
   }
   public boolean en = this.isEnabled();
   public boolean esEnabled() {
    return en;
   }
   boolean estadoPanel = true;
   public void setEstadoPanel(boolean b) {
    estadoPanel = b;
   }
   public void setEnabled(boolean b) {
    en = b;
    if(estadoPanel) setEnabled2(b);
   }
   public void setEnabled2(boolean b) {
    super.setEnabled(b);
   }*/
  public void setText(String s)
  {}

  public String getText()
  {return "";
  }

  public Component getErrorConf()
  {
    return null;
  }
  public AbstractButton getButton(int tecla)
  {
    return estatic.getButtonPanel(tecla,htButton, this);
  }
  public String getStrQuery() {return "";}
  public boolean getQuery() {return false;}
  public String getColumnaAlias()  {     return "";   }

}
