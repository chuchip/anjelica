package gnu.chu.controles;

/**
 *
 * <p>Título: CPanel</p>
 * <p>Descripción: Clase heredada de JPanel con esteroides</p>
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
import java.util.*;
import gnu.chu.interfaces.CEditable;
import gnu.chu.interfaces.CQuery;
import gnu.chu.interfaces.CContenedor;


public class CPanel extends JPanel implements CEditable,CQuery,CContenedor
{
  boolean editable=true;
  boolean swAddFocoListen=false;
  private boolean swBuscaDisa=false;
  Hashtable<Integer,AbstractButton> htButton=new Hashtable();
    /**
    * Crea un Nuevo VPanel
    */
    public CPanel(LayoutManager layout, boolean isDoubleBuffered) {
    // esto se queda para cuando funcione el JPanel
    super(layout, isDoubleBuffered);
    }

    /**
        *  	*	Crea un nuevo VPanel con la especificaciones LayoutManager
    */
    public CPanel(LayoutManager layout) {
     // esto se queda para cuando funcione el JPanel
     super(layout, true);
    }

    /**
        *  	*	Crea un Nuevo VPanel con un FlowLayout. Si <<code>isDoubleBuffered</code>
    * es true, el VPanel usa un doble Buffer
    */
    public CPanel(boolean isDoubleBuffered) {
   // esto se queda para cuando funcione el JPanel
    super(isDoubleBuffered);
    }

    /**
        *  	*	Crea un VPanel con un doble Buffer y un flow layout
    **/
    public CPanel() {
        super();
    }

    /**
        *  	*	Pone enabled o disable todos los Controles que contenga
    */
  private static boolean[] listaEn = new boolean[100];
  boolean firstTime = true;


 /* public void setEnabled2(boolean e) {
    try {
    Component[] lista = getComponents();
    int n = getComponentCount();
    //System.err.println("El panel "+this.toString()+" tiene: "+n+" componentes");
    if(firstTime) {
      for(int i = 0; i < n; i++)
          listaEn[i] = ((Activable)lista[i]).esEnabled();
      firstTime=false;
    }
    for(int i = 0; i < n; i++) {
      if(!e) {
        if(Class.forName("VirtualCom.Controles.VPanel").isAssignableFrom (lista[i].getClass()))
              ((VPanel)lista[i]).setEnabled2(false);
        else {
          //System.err.println("Desactivar: "+lista[i].toString());

//          ((Activable)lista[i]).setEnabled2(false);
//          ((Activable)lista[i]).setEstadoPanel(false);
            ((Activable)lista[i]).setFT(false);


          }
      }
      else {
        if(Class.forName("VirtualCom.Controles.VPanel").isAssignableFrom (lista[i].getClass()))
              ((VPanel)lista[i]).setEnabled2(true);
        else {
         // System.err.println("Activar: "+lista[i].toString());

//          ((Activable)lista[i]).setEnabled2(((Activable)lista[i]).esEnabled());
//          ((Activable)lista[i]).setEstadoPanel(true);
          ((Activable)lista[i]).setFT(true);
        }
      }
    }
      super.setEnabled(e);
    }
    catch(Exception e2346) {
     // System.err.println("++++++++++++++++++++++ERROR GORDO+++++++++++++++++++++++++++++");
    }
  }

  /**
  * Correccinn de enabled
  *
  public boolean en = this.isEnabled();
  public boolean esEnabled() {
    return en;
  }
  boolean estadoPanel = true;
  public void setEstadoPanel(boolean b) {
    estadoPanel = b;
  }
  public void setEnabled(boolean b) {
    setEnabled2(b);
  }*/

//  public void setFT (boolean b) {};
 public void setDefButton(AbstractButton b)
 {
   setButton(java.awt.event.KeyEvent.VK_ENTER, b);
   setButton(java.awt.event.KeyEvent.VK_F4, b);
 }

public void setEscButton(AbstractButton b)
{
  setButton(java.awt.event.KeyEvent.VK_ESCAPE, b);
}
/**
 * Define el buton al que pulsar cuando se le da a F2
 * @param b Boton
 */
public void setAltButton(AbstractButton b)
{
  setButton(java.awt.event.KeyEvent.VK_F2, b);
}
  /**
   * Cuando el Boton por defecto esta disable antes lo pulsaba.
   * Si llamamos con true, buscara al panel padre para coger el boton por Defecto.
   * A todos los efectos si swDisa=true y el boton esta disabled es como
   * si no habriamos puesto boton por defecto.
     * @param swDisa
   * @default false
   */
  public void setDefButtonDisable(boolean swDisa)
  {
    swBuscaDisa=swDisa;
  }

  public boolean getDefButtonDisable()
  {
    return swBuscaDisa;
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
  public void setButton(int tecla, AbstractButton boton)
  {
    if (boton==null)
      htButton.remove(tecla);
    else
      htButton.put(tecla,boton);
  }


    @Override
  public void setEnabled(boolean b)
  {
    super.setEnabled(b);
    setEnabledParent(b);
  }

    @Override
  public void setEnabledParent(boolean enabled)
  {
    if (enabled && ! isEnabled())
      return;
    Component[] lista = this.getComponents();
    int n = this.getComponentCount();
    for(int i = 0; i < n; i++)
    {
      try {
        if(lista[i] instanceof CEditable)
          ((CEditable) lista[i]).setEnabledParent(enabled);
      } catch (Exception k) {}
    }
  }
  public boolean isEditable()
  {
    return editable;
  }
  public void setEditable(boolean editable)
  {
    this.editable=editable;
  }

  public void setEditableParent(boolean editable)
  {
    if (editable && !isEditable())
      return;
    Component[] lista = this.getComponents();
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
  * Funcion que me pone en blanco los controles que implementan CEditable
  * a un valor por defecto
  */
  @Override
 public void resetTexto()
 {
   Component[] lista = this.getComponents();
   int n = this.getComponentCount();
   for (int i = 0; i < n; i++)
   {
     try
     {
       if (lista[i] instanceof CEditable)
       {
         if (lista[i] instanceof CTextField)
         {
           if (! ( (CTextField) lista[i]).getDependePadre())
             continue;
         }

         ( (CEditable) lista[i]).resetTexto();
       }
     }
     catch (Exception k)
     {}
   }
 }

  @Override
 public void resetCambio()
 {
   Component[] lista = this.getComponents();
   int n = this.getComponentCount();
   for (int i = 0; i < n; i++)
   {
     try
     {
       if (lista[i] instanceof CEditable)
       {
         if (lista[i] instanceof CTextField)
         {
           if (! ( (CTextField) lista[i]).getDependePadre())
             continue;
         }

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
    Component[] lista = this.getComponents();
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
    Component[] lista = this.getComponents();
    int n = this.getComponentCount();
    for (int i = 0; i < n; i++)
    {
      try
      {
        if (lista[i] instanceof CQuery)
        {
          if (lista[i] instanceof CTextField )
          {
            if (! ((CTextField) lista[i]).getDependePadre())
              continue;
          }
          ( (CQuery) lista[i]).setQuery(b);
        }
      }
      catch (Exception k)
      {}
    }
    revalidate();
    repaint();
  }

  /**
  * @return true si alguno de los componentes de el Tiene un Error.
  *         false si Ninguno esta en Error.
  */
  @Override
 public Component getErrorConf()
 {
   Component[] lista = this.getComponents();
   int n = this.getComponentCount();
   for (int i = 0; i < n; i++)
   {
     try
     {
       if (Class.forName("gnu.chu.interfaces.CEditable").isAssignableFrom(lista[i].getClass()))
       {
         if ( ( (gnu.chu.interfaces.CEditable) lista[i]).getErrorConf() != null)
           return lista[i];
       }
     }
     catch (Exception k)  {}
   }
   return null;

 }

  @Override
  public void setText(String s) {}
  @Override
  public String getText() {return "";}
  @Override
  public String getStrQuery() {return "";}
  @Override
  public boolean getQuery() {return false;}

  /**
   * Devuelve el boton especificado
   * @param tecla
   * @return 
   */
  @Override
  public AbstractButton getButton(int tecla)
 {
   return estatic.getButtonPanel(tecla,htButton, this);
 }

 public void getVectorQuery(Vector v)
 {
     Component[] lista = this.getComponents();
     int nEl = this.getComponentCount();
     for (int n = 0; n < nEl; n++)
     {
       try
       {
         if (lista[n] instanceof gnu.chu.interfaces.CQuery && lista[n].isEnabled())
           if (( (gnu.chu.interfaces.CQuery) lista[n]).getColumnaAlias()!=null
               && ! ( (gnu.chu.interfaces.CQuery) lista[n]).getColumnaAlias().equals(""))
             v.add( ( (gnu.chu.interfaces.CQuery) lista[n]).getStrQuery());
       }
       catch (Exception k)
       {}
     }
 }
  @Override
 public String getColumnaAlias()
 {
   return "";
 }
}

