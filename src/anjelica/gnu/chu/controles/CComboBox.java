package gnu.chu.controles;

import java.awt.event.*;
import gnu.chu.sql.*;
import java.sql.Types;
import java.sql.SQLException;
import gnu.chu.interfaces.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
/**
 *
 * <p>Título: CComboBox </p>
 * <p>Descripción: Clase que extiende JComboBox añadiendo diferentes utilidades
 * para hacer mas facil su manejo.
 * Incluye la opción de ajustar la anchura del desplegable, que puede ser diferente
 * a la del JComboBxox en si. Solo hay que poner el prefered size, a la anchura deseada.
 * Gracias: Santhosh Kumar
 * (http://www.jroller.com/santhosh/entry/make_jcombobox_popup_wide_enough)
 * </p>
 * <p>Copyright: Copyright (c) 2005-2010
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
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
 * <p>Empresa: miSL</p>
 * @author chuchi P
 * @version 1.0
 */
public class CComboBox extends JComboBox implements CEditable,CQuery
{ 
  CGridEditable gridEdit=null;
  boolean modif=false;
  String conectaColumna="";
  private boolean actDatosDB=true;
  private final int tipoCampo=-1; //  Indica el Types de Campo - Char, decimal,date o time
  String formato="";
  boolean query=false;
  boolean depPadre=true;
  private String copia = "",copiaValor="";

  List<String> indice = new ArrayList<>();

  public CComboBox(ComboBoxModel modelo) {
      super(modelo);
      iniciar();
  }
  public CComboBox(Object[] items)
  {
    super(items);
    iniciar();
  }
  public CComboBox(Vector items)
  {
    super(items);
    iniciar();
  }

  public CComboBox()
  {
    super();
    iniciar();
  }
  private void iniciar()
  {
      
       escuchaTeclas();
  }
  public void setColumnaAlias(String col)
  {
    conectaColumna=col;
  }
  @Override
  public String getColumnaAlias()
  {
    return conectaColumna;
  }

  String ajusta(String s, String formato)
  {
    String txt;
    switch (tipoCampo)
    {
        case Types.DATE:
           Fecha fe = new Fecha(s,"yyyy-MM-dd",formato);
           txt=fe.getFecha();
           break;
         case Types.DECIMAL:
           if (formato.length()!=0)
             txt=Formatear.FormatChar(s,formato);
           else
             txt=s;
           break;
         default:
           txt=s;
     }
      return txt;
  }
  /**
   * Devuelve como un List todos los posibles valores del combo
   * @return  List de valores
   */
  public List getListaValores()
  {
      return indice;
  }
  /**
   * Devuelve como una cadena todos los posibles valores del combo
   * @return String con todos los valores posibles
   */
  public String getValores()
  {
     String s1="";
     for (String indice1 : indice)
     {
          if (indice1 == null)
              continue;
          s1 += indice1;
     }
     return s1;
     
  }
/**
 * Carga los valores disponibles a traves de un datosTabla
 *  Si la consulta tiene mas de una columna, la primera sera el indice y la segunda
 *   el nombre que aparecera en el combo.
 * @param dt
 * @throws SQLException 
 */
 public void setDatos(DatosTabla dt) throws SQLException
 {
   addItem(dt);
 }

 public void setModificable(boolean modif)
 {
   this.modif=modif;
   setEditable(modif);
 }

    private boolean layingOut = false;

    @Override
    public void doLayout(){
        try{
            layingOut = true;
            super.doLayout();
        }finally{
            layingOut = false;
        }
    }
 
    @Override
    public Dimension getSize(){
        Dimension dim = super.getSize();
        if(!layingOut)
            dim.width = Math.max(dim.width, getPreferredSize().width);
        return dim;
    }
/**
 * Añadir Items (valores disponibles en el comboBox)
 * @param dt DatosTabla con los datos ya disponibles.
 * Si la consulta tiene mas de una columna, la primera sera el indice y la segunda
 * el nombre que aparecera en el combo.
 * @throws SQLException 
 */
  public void addItem(DatosTabla dt) throws SQLException
  {
    addItem(dt,true);
  }
  /**
   * Añadir items sobre un datotabla.
   * Si la consulta tiene mas de una columna, la primera sera el indice y la segunda
   * el nombre que aparecera en el combo.
   * @param dt
   * @param borrarAnt
   * @throws SQLException
   */
  public void addItem(DatosTabla dt,boolean borrarAnt) throws SQLException
  {
    if (getItemCount()>0 && borrarAnt)
      removeAllItems();
    if (dt.getNOREG())
      return;
    Object vv=dt.getObject(1);
    if (vv == null)
      return;
    boolean adb=actDatosDB;
    actDatosDB=false;

    setEditable(true);

    try {
      do {
        if (dt.getNumCol() > 1)
          addItem(dt.getString(2,true) +
                  (dt.getNumCol()>2?"   (" + dt.getObject(3) + ")":""),
                  dt.getString(1,true));
        else
          addItem(dt.getString(1,true));
      } while ( dt.next());
    } catch (SQLException k)
    {
      actDatosDB=adb;
      setEditable(modif);
      throw k;
    }
    actDatosDB=adb;
    setEditable(modif);
    setSelectedIndex(0);
    return;
  }

  @Override
  public void addItem(Object v)
  {
    addItem(v,v.toString());
  }
  
  public void addItem(String[][] arrayValor)
  {
      for (String[] valores : arrayValor)
      {      
          addItem(valores[0],valores[1]);      
      }
    
  }
/**
 * Añade un indice nuevo al comboBox
 * @param valorCombo Valor a mostrar en pantalla
 * @param indice lo q se devolvera a recoger getValor()
 */
  public void addItem(Object valorCombo,String indice)
  {
    super.addItem(valorCombo);
    this.indice.add(indice);
  }

  public void setActDatosDB(boolean a)
  {
    actDatosDB=a;
  }

  public boolean getActDatosDB()
  {
    return actDatosDB;
  }

  /*
  * Retorna el  Campo selecionado.
  */
    @Override
  public String getText()
  {
    try {
        return getSelectedItem().toString();
    } catch (Exception k)
    {
        return "";
    }
  }
/**
 * Devuelve el Texto del combo, segun el valor mandado (indice)
 * @param val
 * @return null si no lo encuentra.
 */
  public String getText(String val)
  {
    if (val == null)
      return null;
    int n=0;
    for (String i : indice)
    {
      if (i.equals(val))
        return getItemAt(n).toString();
      n++;
    }
    return null;
  }
  /*
  * Seleciona El item que coincida con el texto enviado.
  * @deprecated
  * Utilizar setText
  */
  public void setTexto(String s)
  {
    setText(s);
  }
  /*
  * Seleciona El item que coincida con el texto enviado.
  */
@Override
public void setText(String s)
{
    try {
        setSelectedItem(s);
    } catch (Exception k)
    {
    }
}

    /**
     * Pone el campo en modo query.
     * @param b boolean
     *
     */
    @Override
  public void setQuery(boolean b){
    if (query==b)
      return;
    query=b;
    if (b)
    {
      addItem(gnu.chu.interfaces.ejecutable.VACIO);
      setSelectedItem(gnu.chu.interfaces.ejecutable.VACIO);
    }
    else
    {
      removeItem(gnu.chu.interfaces.ejecutable.VACIO);
    }
  }
    @Override
  public boolean getQuery()
  {
    return query;
  }

    @Override
  public String getStrQuery()
  {
    if (getSelectedItem().toString().compareTo(gnu.chu.interfaces.ejecutable.VACIO)==0)
      return "";
    else
      if (getValor().equals(gnu.chu.interfaces.ejecutable.VACIO))
        return conectaColumna+" = '"+getText()+"'";
      else
        return conectaColumna+" = '"+getValor()+"'";
  }
  /**
   * Devuelve el indice (el indice) de la lista selecionada
   * @return Valor del combo
   */
  public String getValor()
  {
    int n=getSelectedIndex();
    return getValor(n);
  }

  public int getValorInt()
  {
    int n = getSelectedIndex();
    if (n==-1)
        return 0;
    try {
     return Integer.parseInt(getValor(n).trim());
    } catch (Throwable k1)
    {
          Logger.getLogger(CComboBox.class.getName()).log(Level.SEVERE, null, k1);
          return 0;
      }
  }
  /**
   * Es el indice igual al mandando (haciendo un trim)
   *
   * @param valCompara String
   * @return boolean
   */
  public boolean isValor(String valCompara)
  {   
    return getValor(getSelectedIndex()).trim().equals(valCompara);
  }
  public String getValor(int index){
      
    if (index<indice.size() && index>=0)
      return indice.get(index);
    else
      return "";
  }
  
  public void setValor(double valor)
  {
    setValor(""+valor);
  }
  public void setFormato(String formato)
  {
    this.formato=formato;
  }
  public String getFormato()
  {
      return formato;
  }
  public void setDate(Date fecha) throws ParseException
  {
    if (formato.equals(""))
        setValor(Formatear.getFecha(fecha, "dd-MM-yyyy"));
    else
        setValor(Formatear.getFecha(fecha, formato));
  }
  public Date getDate()throws ParseException
  {
      if (formato.equals(""))
        return Formatear.getDate(getValor(), "dd-MM-yyyy");
    return Formatear.getDate(getValor(), formato);
  }

  public boolean getError()
  {
      if (! isEditable())
          return false;
      return false;
  }
  public void setValor(int s)
  {
    try {
        for (int n=0;n<indice.size();n++)
        {
          if (indice.get(n)==null)
            continue;
          if (Integer.parseInt(indice.get(n))==s)
          {
            this.setSelectedIndex(n);
            return;
          }
        }
    } catch (NumberFormatException k)
    { // Ignoro Errores de NumberFormatException
        
    }
  }
  /**
   * Pone el valor mandado al CombBox
   * @param valor Valor a poner en el combo
   * @return  false si no encuentra el valor en la lista de los posibles.
   */
  public boolean setValor(String valor)
  {
    if (valor==null)
      return false;
  
    for (int n=0;n<indice.size();n++)
    {
      if (indice.get(n)==null)
        continue;
      if (indice.get(n).equals(valor))
      {
        this.setSelectedIndex(n);
        return true;
      }
    }
    return false;
  }
  /**
   * Retorna el valor del combo para un texto mandado
   * @param texto String Indice sobre el que buscar el Texto
   * @return String Valor del combo
   */    
    public String getValor(String texto)
  {
    if (texto==null)
      return null;
    int nItems=getItemCount();
    for (int n=0;n<nItems;n++)
    {
      if (getItemAt(n).toString().equals(texto))
        return indice.get(n);
    }
    return null;
  }
  /**
   * Devuelve el valor para un indice mandado
   * @param val Indice
   * @return  Valor o null si no encuentra el indice.
   */
  public Object getItemAt(String val){
      for (int n=0;n<indice.size();n++)
      {
         if (indice.get(n).equals(val))
            return getItemAt(n);
      }
      return null;

  }

  @Override
  public Component getErrorConf()
  {
    return null;
  }
  /**
  * Rutina que comprueba si el campo ha cambiado desde la ultima ejecucinn de
  * resetCambio.
  * @return true -> Ha cambiado.
  *         false-> NO ha cambiado.
  * <p>
  * En el caso de que no se haya llamado a la funcion resetCambio
  * lanzara un NullPointerException.
  *
  */
  @Override
  public boolean hasCambio()
  {
      return ! copia.trim().equals(getText().trim());
  }
  public String getTextAnt()
  {
    return copia;
  }
  /**
  * Iguala la variable 'copia' a lo que haya actualmente en el VTextField
  * <p>
  * Si llamamos inmediatamente a la funcion hasCambio, esta devolveria true.
  *
  */
  @Override
  public void resetCambio()
  {
    copia=getText();
    copiaValor=getValor();
  }
  
  /**
  * Retorna el indice Anterior
  * @return String
  */
  public String getValorOld() { return copiaValor; };
  
  public void setTextInicio()
  {
      
  }
  /**
   * Retorna el indice Actual. Igual que hacer getText()
  *  @return String
   */
  public String getValorAct() {
         return getText();
  };
   /**
  * Funcion que me pone en blanco los controles que implementan VEditable
 a un indice por defecto
  */
  @Override
  public void resetTexto()
  {
    if (getQuery())
      setText(gnu.chu.interfaces.ejecutable.VACIO);
    else
    {
      if (getItemCount()>0)
        setSelectedIndex(0);
    }
  }
  
  public void setTextFin()
  {
    if (getQuery())
      setText(gnu.chu.interfaces.ejecutable.VACIO);
    else
    {
      if (getItemCount()>0)
        setSelectedIndex(getItemCount()-1);
    }
      
  }
  private boolean activado=true;
  private boolean activadoParent=true;

  @Override
  public void setEnabled(boolean enab)
  {
    activado=enab;
    if (activado)
    {
      if (activadoParent  || !depPadre)
        super.setEnabled(true);
      return;
    }
    super.setEnabled(false);
  }
  /**
   * Utilizada para que no dependa de si su panel padre se pone enabled o no.
   * Hace q funcione como los componentes normales si se le manda false
   * @param depPadre boolean Depende de Padre ?
   */
  public void setDependePadre(boolean depPadre)
  {
    this.depPadre=depPadre;
  }

  public boolean getDependePadre()
    {
      return this.depPadre;
    }

  public void setEnabledParent(boolean enab)
  {
    activadoParent=enab;
    if (! enab)
    {
      if (super.isEnabled() && depPadre)
      {
        super.setEnabled(false);
      }
    }
    else
    {
      if (! super.isEnabled() && activado && depPadre)
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
  public void removeAllItems() {
    super.removeAllItems();
    indice.clear();
  }
  @Override
  public void removeItem(Object c) {
    int pos = -1;
    for (int i=0;i<getItemCount();i++) {
        if (getItemAt(i).equals(c)) {
            pos = i;
            break;
        }
    }
    super.removeItem(c);
    if (pos != -1)
       indice.remove(pos);
  }
  /**
   * Indica si el texto visualizado en el Combo se le ha asignado Valor
   * @deprecated Usar isVacio
   * @return boolean
   */
  public boolean isAsignadoValorBD() {
       return isVacio();
  }
  /**
   * Indica si hay algun indice disponible en el Combo
   *
   * @return boolean
   */
  public boolean isVacio()
  {
    if (indice.isEmpty()) return true;
    if (indice.get(0) == null)
      return true;
    return ( (indice.get(0).equals(gnu.chu.interfaces.ejecutable.VACIO))
        || (indice.get(0).equals("")));
  }
  public boolean isNull()
  {
    return isNull(true);
  }
  public boolean isNull(boolean trim)
  {
    return trim?getText().trim().equals(""):getText().equals("");
  }
  public void removeListeners()
  {
    int nList=listenerList.getListenerCount();
     Object o[] = listenerList.getListenerList();
     Class c[]=new Class[nList];
     EventListener l[]=new EventListener[nList];
     int n=0;
     for (int i = o.length-2; i>=0; i-=2,n++)
     {
      c[n]=(Class) o[i];
      l[n]=(EventListener) o[i+1];
     }
     int i= listenerList.getListenerCount();
     for (n=0;n< nList;n++)
      listenerList.remove(c[n],l[n]);
  }
  public void dispose(){}

  private void escuchaTeclas()
  {
    this.addKeyListener(new KeyAdapter()
    {
      AbstractButton defaultButton;
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (!e.isAltDown())
        {
          switch (e.getKeyCode())
          {
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_F2:
            case KeyEvent.VK_F4:
            case KeyEvent.VK_F5:
            case KeyEvent.VK_F6:
            case KeyEvent.VK_F7:
            case KeyEvent.VK_F8:
            case KeyEvent.VK_F9:
            case KeyEvent.VK_F10:
              defaultButton = estatic.getButton(e.getKeyCode(),CComboBox.this);
              if (defaultButton != null)
              {
                defaultButton.requestFocus();
                defaultButton.doClick();
              }
//              if (e.getKeyCode() == e.VK_F2)
//              {
//                AbstractButton alternateButton = getButton(e.VK_F2);
//                if (alternateButton != null)
//                {
//                  alternateButton.requestFocus();
//                  alternateButton.doClick();
//                }
//              }
          }
        }
      }
    });
  }

  AbstractButton getAlternateButton()
  {

    AbstractButton dfButton;
    try {
      Container c1= getParent();
      if(Class.forName("gnu.chu.controles.CPanel").isAssignableFrom (c1.getClass()))
      {
        dfButton=((gnu.chu.controles.CPanel) c1).getAltButton();
        return dfButton;
      }
    } catch (Exception k) { }
    return null;
  }

  AbstractButton getDefaultButton()
  {
   return estatic.getButton(KeyEvent.VK_ENTER,this);
  }
  public AbstractButton getButton(int tecla)
  {
    return estatic.getButton(tecla,this);
  }


  public void setGridEditable(CGridEditable gridEdit)
  {
    this.gridEdit = gridEdit;
  }

  public CGridEditable getGridEditable()
  {
    return gridEdit;
  }

  protected void procesaTab(KeyEvent ke)
  {
    if (gridEdit!=null)
      gridEdit.procesaTab(ke);
  }
  protected void procesaEnter(KeyEvent ke)
  {
     if (gridEdit != null)
       gridEdit.procesaEnter(ke);
  }

    @Override
  public void processKeyEvent(KeyEvent ke)
   {
     if (ke.getID() == KeyEvent.KEY_PRESSED)
     {
       int pulsado=ke.getKeyCode();
       switch (pulsado)
       {
         case KeyEvent.VK_TAB:
           if (gridEdit!=null &&  ! ke.isShiftDown())
                ke.setKeyCode(KeyEvent.VK_ENTER);
           super.processKeyEvent(ke);
           if (ke.isShiftDown())
               break;
           ke.setKeyCode(KeyEvent.VK_TAB);
           procesaTab(ke);
           break;
         case KeyEvent.VK_ENTER:
         case KeyEvent.VK_SPACE:
           super.processKeyEvent(ke);             
           procesaEnter(ke);
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

}
