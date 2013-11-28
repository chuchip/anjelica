/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CInternalFrame.java
 *
 * Created on 20-dic-2008, 20:04:17
 */

package gnu.chu.controles;

import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import gnu.chu.interfaces.*;
import gnu.chu.utilidades.Iconos;
/**
 *
 * @author cpuente
 */
public class CInternalFrame extends javax.swing.JInternalFrame {

    Component focOwer=null;
    boolean primera=true;
    boolean swFirst=true;
    boolean swMax,swMin,swClo;
    CInternalFrame foco=null;
    /**
     * Constructor
     *
     * Crea un VInternalFrame sin Titulo
     */
    public CInternalFrame() {
            super();
      if (System.getProperty("Param") != null)
         this.setFrameIcon(Iconos.getImageIcon("ls_" + System.getProperty("Param").toLowerCase()));
      else
         this.setFrameIcon(Iconos.getImageIcon("logo"));
      activacion();
    }

    /**
     * Constructor
     *
     * Crea un VInternalFrame con un Titulo
     */
    public CInternalFrame(String titulo) {
            super(titulo);
//      this.setFrameIcon(Iconos.getImageIcon("ls_" + System.getProperty("Param").toLowerCase()));
      activacion();
    }

    public CInternalFrame(String titulo, boolean resizable, boolean closable) {
             super(titulo, resizable, closable);
//      this.setFrameIcon(Iconos.getImageIcon("ls_" + System.getProperty("Param").toLowerCase()));
      activacion();
    }

    public CInternalFrame(String titulo, boolean resizable, boolean closable,
                          boolean maximizable) {
      super(titulo, resizable, closable, maximizable);
//      this.setFrameIcon(Iconos.getImageIcon("ls_" + System.getProperty("Param").toLowerCase()));
      activacion();
    }

    /**
     * Constructor
     *
     * Metodo primitivo de creacion, este pone los valores apropiados
     */
    public CInternalFrame(String titulo, boolean resizable, boolean closable,
                          boolean maximizable, boolean iconifiable)
    {
      super(titulo, resizable, closable, maximizable, iconifiable);
  //      this.setFrameIcon(Iconos.getImageIcon("ls_" + System.getProperty("Param").toLowerCase()));
      activacion();
    }

    public void setSize(int width, int height) {
           if (System.getProperty("os.name").toUpperCase().indexOf("WIN") == -1)
              height = height + 10;
           super.setSize(width, height);
    }
    public void setSize(Dimension d) {
           if (System.getProperty("os.name").toUpperCase().indexOf("WIN") == -1)
              d.height = d.height + 10;
           super.setSize(d);
    }
    /**
     * Indica a quien tiene que poner el foco cuando se le haga un setSelected
     * @param c
     */
    public void setFoco(CInternalFrame c)
    {
      foco=c;
    }
    @Override
    public void setSelected(boolean b) throws java.beans.PropertyVetoException
    {
      if (foco==null)
      {
        if (! b)
          focOwer=this.getFocusOwner();
        else
        {
          if (focOwer!=null)
            focOwer.requestFocus();
        }
        super.setSelected(b);
      }
      else
        foco.setSelected(b);
    }

  /**
   * Activa la ventana la primera vez que se visualiza
   */
  private void activaVentana(AncestorListener event) {
    if (primera) {
      primera=false;
      try {
        setSelected(true);
        this.removeAncestorListener(event);
      } catch (Exception k) {
      }
    }
  }
  /**
  * Ativacion de eventos
  * @author Diego Cuesta
  */
  private void activacion() {
    this.addAncestorListener(new AncestorListener() {
      public void ancestorAdded(AncestorEvent event) {
        activaVentana(this);
      }
      public void ancestorRemoved(AncestorEvent event) {
        activaVentana(this);
      }
      public void ancestorMoved(AncestorEvent event) {
        activaVentana(this);
      }
    });
// PONE QUE el ENTER actue igul que el TABULADOR
    HashSet hs = new HashSet();
    hs.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
    hs.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
    this.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, hs);


  }

  /**
  *  Establece el fichero HTML generico ...
  */
  public void setAyuda(String ficHtml)
  {
  }

  public void setCursor(Cursor c) {
         super.setCursor(c);
         validate();
         repaint();
  }

  public void setEnabled(boolean b)
  {
    Cursor c = null;
    if (!b)
       c = new Cursor(Cursor.WAIT_CURSOR);
    else
       c = new Cursor(Cursor.DEFAULT_CURSOR);
    setCursor(c);
    for (int i=0;i<getComponentCount();i++)
        getComponent(i).setCursor(c);
    if (! b)
    {
      swMax = this.isMaximizable();
      swMin = this.isIconifiable();
      swClo = this.isClosable();
      swFirst=false;
      this.setIconifiable(false);
      this.setClosable(false);
      this.setMaximizable(false);
    }
    else
    {
      if (!swFirst)
      {
        this.setIconifiable(swMin);
        this.setClosable(swClo);
        this.setMaximizable(swMax);
      }
    }
    super.setEnabled(b);
    setEnabledParent(b);
//    setEnabledComponents(c,b);
  }

/*  public void setEnabledComponents(Container c,boolean b)
  {
    Component[] lista = c.getComponents();
    int n = c.getComponentCount();

          for(int i = 0; i < n; i++)
                 {
        try {
          ((Component) lista[i]).setEnabled(b);
        } catch (Exception k)
        {
          System.out.println("VInternalFrame: (setEnabledComponents) Lanzada Exception");
          k.printStackTrace();
        }
     }
  }*/

  public void setEnabledParent(boolean enabled)
  {

            Component[] lista = this.getContentPane().getComponents();
                  int n = this.getContentPane().getComponentCount();
                  for(int i = 0; i < n; i++)
                  {
        try {
           if(lista[i] instanceof CEditable)
            ((CEditable) lista[i]).setEnabledParent(enabled);
         } catch (Exception k)
        {
//          System.out.println("VInternalFrame: (setEnabledParent) Lanzada Exception");
          k.printStackTrace();
        }
      }
  }

  /**
  * Retorna el valor Anterior
  */
  public String getValorOld() { return ""; };
  /**
   * Retorna el valor Actual
   */
  public String getValorAct() {
         return "";
  };

}
