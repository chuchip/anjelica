package gnu.chu.controles;

import gnu.chu.eventos.*;
import gnu.chu.interfaces.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
/**
 *
 * <p>Título: CLinkBox </p>
 * <p>Descripción: Clase que implementa un JComboBox y un CTextField
 * Permite tanto escribir un valor como elegirlo de un desplegable, sincronizandose
 * entre los dos campos.
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
public class CLinkBox
    extends CPanel
    implements CQuery, CEditable, FocusListener, CellEditor, TableCellEditor
{
  String copia  ="";
  CGridEditable gridEdit = null;
  ArrayList<FocusListener> eventVector = new ArrayList();
  boolean swIniciar = false;
  private ArrayList<CambioListener> cambioListenerList = new ArrayList();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  boolean aceptaNulo = true;
  private int ancTexto = 80;
  public CTextField texto = new CTextField()
  {
    @Override
    protected boolean doClick(int keyCode)
    {
      if (keyCode != KeyEvent.VK_ENTER)
      {
        event = false;
        processCambio();
        actualizaCombo(true);
        event = true;
      }
      return true;
    }
  };

  public CComboBox combo = new CComboBox();


  public CButton button = new CButton(Iconos.getImageIcon("nuevo"))
  {
    @Override
    public boolean isFocusTraversable()
    {
      return false;
    }
  };
  String NombIdx; // Columna principal del DatosTabla
  String NombDef; // Columna que se visualizara en el Combo

  public ArrayList<String> vecIdx = new ArrayList();
  public ArrayList<String> vecDef = new ArrayList();

  public boolean buttonSN = false; // Indica si esta visible el boton

  public boolean event = true;
  boolean Encontrado = true;

//  boolean ceroNull = false;
  boolean formato = false; // Indica si debe coger el Formato del Texto o poner el de defecto.
  boolean query = false;
  boolean aceptaError = false;
  int posX = 0;
  int posY = 0;
  int ancho = 0;
  int alto = 0;

  public void setAceptaError(boolean b)
  {
    aceptaError = b;
  }

  public boolean getAceptarError()
  {
    return aceptaError;
  }

  public void setFormato(boolean f)
  {
    formato = f;
  }

  public boolean getFormato()
  {
    return formato;
  }

  @Override
  public void setFont(Font f)
  {
    super.setFont(f);
    if (combo != null)
      combo.setFont(f);
    if (texto != null)
      texto.setFont(f);
  }

  public CLinkBox()
  {
    this(false);
  }

  public CLinkBox(boolean botCons)
  {
    try
    {
      buttonSN = botCons;
      jbInit();
    }
    catch (Exception e)
    {
       SystemOut.print(e);
    }
  }

  private void jbInit() throws Exception
  {
    this.setLayout(gridBagLayout1);
    texto.setMinimumSize(new Dimension(30, 17));
    texto.setPreferredSize(new Dimension(80, 17));
    texto.setSimpleQuery(false);
    combo.setPreferredSize(new Dimension(60, 17));
    button.setMargin(new Insets(0, 0, 0, 0));
    button.setPreferredSize(new Dimension(18, 18));
    combo.setFocusable(false);
    this.add(texto, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0
                                           , GridBagConstraints.SOUTHEAST,
                                           GridBagConstraints.VERTICAL,
                                           new Insets(0, 0, 0, 0), 0, 0));
    this.add(combo, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                           , GridBagConstraints.SOUTHWEST,
                                           GridBagConstraints.BOTH,
                                           new Insets(0, 0, 0, 2), 0, 0));
    if (buttonSN)
      this.add(button,
               new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 0.0,
                                      1.0
                                      , GridBagConstraints.WEST,
                                      GridBagConstraints.VERTICAL,
                                      new Insets(0, 0, 0, 2), 0, 0));

    activaEventos();
    swIniciar = true;
  }

  public void setAceptaNulo(boolean acepNulo)
  {
    aceptaNulo = acepNulo;
  }

  public boolean getAceptaNulo()
  {
    return aceptaNulo;
  }

  /**
   * Actualiza el combo dependiendo del valor del texto
   * @param actCombo  actuaaliza valor combo boolean
   */
  public void actualizaCombo(boolean actCombo)
  {
    String WText = "";
    event = false;

    if (texto.getText().trim().compareTo("") == 0)
    {
      if (!aceptaNulo)
        Encontrado = false;
      event = true;
      Encontrado=true;
      return;
    }

    if (combo.getItemCount() == 0)
    {
      Encontrado = false;
      event = true;
      return;
    }

    switch (texto.getTipoCampo())
    {
      case Types.DECIMAL:
        if (texto.getCeroIsNull())
        {
          if (texto.getValorDec() == 0)
          {
            if (!aceptaNulo)
            {
              Encontrado = false;
              event = true;
              return;
            }
            if (actCombo)
            {
              combo.setEditable(true);
              combo.setSelectedItem("");
              combo.setEditable(false);
            }
            Encontrado = true;
            event = true;
            return;
          }
        }
        if (formato)
          WText = texto.getText();
        else
        {
          DecimalFormat form = new DecimalFormat();
          form.applyPattern("##0.#####");
          WText = form.format(texto.getValorDec());
        }
        break;
      case Types.CHAR:
        WText = texto.getText();
        break;
      case Types.DATE:
        Fecha fech = new Fecha(texto.getText(), texto.getFormato());
        WText = fech.getFecha();
        break;
    }

    for (int wi = 0; wi < vecIdx.size(); wi++)
    {
      if (vecIdx.get(wi).trim().equals(WText.trim()))
      {
        if (actCombo)
          combo.setSelectedIndex(wi); //vecDef.elementAt(wi).toString().trim());
        event = true;
        Encontrado = true;
        return;
      }
    }
    Encontrado = false;
    if (actCombo)
    {
      combo.setEditable(true);
      combo.setSelectedItem("No Existe");
      combo.setEditable(false);
    }
    event = true;
  }

  /**
   * Retorna si esta visible el boton de a�adir
   * @return true esta visible, false no lo esta
   */
  public boolean getButtonAdd()
  {
    return buttonSN;
  }
  public CComboBox getComboBox() {
        return combo;
  }

  /**
   * @deprecated Usar getCeroIsNull()
   * @return boolean Null = 0 ?
   */
  public boolean getCeroNull()
  {
    return getCeroIsNull();
  }

  /**
   * Retorna se el VLinkBox1 esta activo
   * @return true Activo - false Desactivo
   */
  public boolean getEnabled()
  {
    return texto.isEnabled();
  }

  /**
   * Devuelve si no ha encontrado el registro
   * @return true si hay error
   */
  public boolean getError()
  {
    return !Encontrado;
  }

  /**
   * Retorna si esta en modo Query
   * @return true si esta en modo Query
   */
  @Override
  public boolean getQuery()
  {
    return query;
  }

  /**
   * Recoge el texto del Query formado
   * @return texto formado
   */
  @Override
  public String getStrQuery()
  {
    return texto.getStrQuery();
  }

  /**
   * Retorna el texto del CTextField
   * @return Texto que tiene el CTextField
   */
  @Override
  public String getText()
  {
    if (texto.getGridEditable() == null)
      return texto.getText();
    return getTexto(texto.getText());
//          texto.setGridEditable(null);
  }
  /**
   * Devuelve el Texto del Combo a partir del Valor del TextField
   * @param texto String Valor del TextField
   * @return String Valor del Combo
   */
  public String getValor(String texto)
  {
    if (texto==null)
       return null;
     int nItems= vecIdx.size();
     for (int n=0;n<nItems;n++)
     {
       if (vecIdx.get(n).equals(texto))
         return vecDef.get(n);
     }
     return null;
  }
  /**
   * Devuelve el Valor del TextField a partir del Valor del Combo
   * @param texto String valor del Combo
   * @return String Valor del TextField
   */
  public String getText(String texto)
  {
    if (texto==null)
     return null;
   int nItems= vecDef.size();
   for (int n=0;n<nItems;n++)
   {
     if (vecDef.get(n).equals(texto))
       return vecIdx.get(n);
   }
   return null;

  }

  /**
   * Retorna el texto del JComboBox
   *
   * @return Texto que tiene el JComboBox
   */
  public String getTextCombo()
  {
    return combo.getText();
  }

  public double getValorDec()
  {
    return texto.getValorDec();
  }
  public int getValorInt()
  {
    return texto.getValorInt();
  }
  @Override
  public void resetTexto()
  {
    if (texto.isQuery())
      setText("");
    else
    {
      if (combo.getItemCount() == 0)
      {
        texto.setText("");
        Encontrado = false;
      }
      else
      {
        combo.setSelectedIndex(0);
        texto.setText(vecIdx.get(combo.getSelectedIndex()).trim());
      }
    }
  }

  @Override
  public void requestFocus()
  {
    texto.requestFocus();
  }
  
  public void requestFocusLater()
  {
    SwingUtilities.invokeLater(new Thread()
    {
        @Override
        public void run()
        {       
          CLinkBox.this.requestFocus();
        }
    });
  }
  /**
   * Asigna si se visualiza el boton de anadir un registro nuevo
   * @param b true poner el boton de Insertar
   */
  public void setButtonAdd(boolean b)
  {
    this.remove(button);
    buttonSN = b;
    if (buttonSN)
    {
      this.add(button,
               new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 0.0,
                                      1.0, GridBagConstraints.WEST,
                                      GridBagConstraints.VERTICAL,
                                      new Insets(0, 0, 2, 2), 0, 0));
    }
  }

 
  /**
   * Establece que 0 es igual a null
   * @param ceroIsNull  por defecto es true
   */
  public void setCeroIsNull(boolean ceroIsNull)
  {
    texto.setCeroIsNull(ceroIsNull);
  }

  public boolean getCeroIsNull()
  {
    return texto.getCeroIsNull();
  }

  public void setColumnaAlias(String alias)
  {
    texto.setColumnaAlias(alias);
  }

  @Override
  public String getColumnaAlias()
  {
    return texto.getColumnaAlias();
  }

  /**
   *
   * @param e true marca como con error
   */
  public void setError(boolean e)
  {
    Encontrado = !e;
  }

  /**
   * Pone en modo Query el control
   * @param b true poner en modo query
   */
  @Override
  public void setQuery(boolean b)
  {
    query = b;
    texto.setQuery(b);
    button.setEnabled(!b);
  }

    @Override
  public void setText(String s)
  {
    setText(s, true);
  }
  /**
   * Pone el valor inicial en el LinkBox
   */
  public void setTextInicio()
  {
      if (combo.isVacio())
          return;
      setText(vecIdx.get(0));
  }
      
  public void setGridEditable(CGridEditable gridEdit)
  {
    if (texto!=null)
        texto.setGridEditable(gridEdit);
    this.gridEdit = gridEdit;
  }
 public CGridEditable getGridEditable()
  {
    return this.gridEdit;
  }
 /**
  * Controla si el valor introducido es valido.
  * Realiza un requestfocus al campo si no lo es.
  * @return true si es valido.
  */
  public boolean controla()
  {
    return controla(true);
  }
/**
  * Controla si el valor introducido es valido.
  * Realiza un requestfocus al campo si el parametro reqFocus es true.
 *  @param reqFocus true si debe realizar un requestFocus en caso de error.
 * 
  * @return true si es valido.
  */
  public boolean controla(boolean reqFocus)
  {
    actualizaCombo(false);
    if (!Encontrado)
    {
      if (reqFocus)
        texto.requestFocusLater();
      return false;
    }
    return true;
  }

  public void setValorDec(double valor)
  {
    setText(""+valor);
  }

  public void setValorInt(int valor)
  {
    setText("" + valor);
  }

  /**
   * Pone texto al VTextField
   * @param s Texto a poner
   * @param b true formatear el texto
   */
  public void setText(String s, boolean b)
  {
    if (s == null)
      return;
    event = false;
    if (gridEdit != null)
      s = getTexto(s);
    texto.setText(s, b);

    if (s.trim().compareTo("") == 0 || (s.trim().compareTo("0") == 0 && getCeroIsNull()))
    {
      if (!aceptaNulo) //&& !texto.getQuery())
        Encontrado = false;
      else
      {
        combo.setEditable(true);
        combo.setSelectedItem("");
        combo.setEditable(false);
      }
    }
    else
    {
      actualizaCombo(true);
    }
    event = true;
  }
  /**
   * @deprecated user addDatoVec
   * @param v Vector Datos a poner al LinkBox
   */
  public void setDatosVec(Vector v)
  {
    addDatosVec(v);
  }
  /**
   * Carga los datos con un vector
   * @param v Datos a poner al LinkBox
   */
  public void addDatosVec(Vector v)
  {
    event = false;
    vecIdx.clear();
    vecDef.clear();
    if (combo.getItemCount() > 0)
      combo.removeAllItems();

    for (int i = 0; i < v.size(); i++)
    {
      vecIdx.add( ( (Vector) v.elementAt(i)).elementAt(0).toString());
      vecDef.add( ( (Vector) v.elementAt(i)).elementAt(1).toString());
    }

    for (String vecDef1 : vecDef)
    {
          combo.addItem(vecDef1);
    }
    event = true;
  }


  public void addDatos(String indice, String nombre)
  {
    addDatos(indice, nombre, true);
  }


  public void addDatos(String indice, String nombre, boolean avisa)
  {
    event = false;
    vecIdx.add(indice);
    vecDef.add(nombre);
    combo.addItem(nombre);
//    if (avisa)
//     this.getComboModel().avisaAdd();
    event = true;
  }

  public void removeAllItems()
  {
    event = false;
    vecIdx.clear();
    vecDef.clear();
    combo.removeAllItems();
    event = true;
  }

  public void removeItem(String c)
  {
    if (vecDef.indexOf(c) < 0)
      return;
    event = false;
    vecDef.remove(c);
    vecIdx.remove(c);
    combo.removeItem(c);
    event = true;
  }

  public void removeItemAt(int el)
  {
    if (el >= vecDef.size())
      return;
    event = false;
    vecDef.remove(el);
    vecIdx.remove(el);
    combo.removeItemAt(el);
    event = true;
  }

  public void addDatos(DatosTabla dt) throws SQLException
  {
    addDatos(dt,true);
  }

  public void addDatos(DatosTabla dt,boolean vaciar) throws SQLException
  {
    event = false;
    if (vaciar)
    {
      vecIdx.clear();
      vecDef.clear();
      combo.removeAllItems();
    }
    if (dt.getNOREG())
    {
      event = true;
      combo.setEditable(false);
      return;
    }

    do
    {
      vecIdx.add(dt.getString(1));
      vecDef.add(dt.getString(2)+(dt.getColumnCount()>2?" ... " + dt.getString(3):""));
      combo.addItem(dt.getString(2) +(dt.getColumnCount()>2?" ... " + dt.getString(3):""));
    } while (dt.next());
    event = true;
    combo.setEditable(false);
  }


  /**
   * Funcion donde se declaran todos los Eventos
   **/
  private void activaEventos()
  {
    combo.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (!getEnabled())
          return;

        if (!event || vecIdx.isEmpty())
          return;

        event = false;
        String text = vecIdx.get(combo.getSelectedIndex()).trim();
        texto.setText(text);
        event = true;
        Encontrado = true;
        processCambio();
      }
    });

    combo.getAccessibleContext().getAccessibleComponent().addFocusListener(new
        FocusListener()
    {
      @Override
      public void focusGained(FocusEvent e)
      {
        if (!Encontrado)
        {
          texto.sonido();

          if (combo == null)
            return;

          if (combo.getSelectedIndex() < 0)
          {
            return;
          }
          if (combo.getItemCount() > 0)
          {
            if (!aceptaError)
            {
              texto.setText(vecIdx.get(combo.getSelectedIndex()).trim());
            }
          }
        }
      }

      @Override
      public void focusLost(FocusEvent e)
      {
        if (e.getOppositeComponent() == texto)
          processFocusEvent(e);
      }
    });

    texto.addFocusListener(new FocusAdapter()
    {
      @Override
      public void focusLost(FocusEvent e)
      {
        if (!getEnabled())
          return;

        if (!event)
          return;

        event = false;
        processCambio();
        actualizaCombo(true);
        event = true;
      }
    });

    texto.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (!getEnabled())
          return;
       
        if (!event)
          return;
        switch (e.getKeyCode())
        {
          case KeyEvent.VK_DOWN:
             if (!isEditable())
                 return;
            if (getGridEditable()!=null && ! e.isShiftDown())
                return;
            combo.setFocusable(true);
            combo.requestFocus();
            combo.showPopup();
            e.consume();
            SwingUtilities.invokeLater(new Runnable()
            {
              @Override
              public void run()
              {
                combo.setFocusable(true);
              }
            });
            break;
          case KeyEvent.VK_INSERT:
            if (buttonSN)
              button.doClick();
            break;
          default:
        }
      }
    });
  }

  public void setFormato(int tipCampo, String formato, int maxLong)
  {
    if (formato==null)
    {
      setFormato(false);
      return;
    }
    setFormato(true);
    texto.setTipoCampo(tipCampo);
    texto.setFormato(formato);
    if (maxLong != 0)
      texto.setMaxLong(maxLong);
    setFormato(true);
  }

  public void setFormato(int tipCampo, String formato)
  {
    setFormato(tipCampo,formato,formato.length());
  }

  public void setMayusculas(boolean mayusc)
  {
    texto.setMayusc(mayusc);
  }

  public boolean getMayusculas()
  {
    return texto.getMayusc();
  }

  public void setMinusculas(boolean minusc)
  {
    texto.setMinusc(minusc);
  }

  public boolean getMinusculas()
  {
    return texto.getMinusc();
  }

  @Override
  public void addFocusListener(FocusListener fc)
  {
    if (eventVector == null)
      return;
    eventVector.add(fc);
    if (texto != null)
      texto.addFocusListener(this);
  }

  @Override
  public void focusGained(FocusEvent e)
  {
    processFocusEvent(e);
  }

  @Override
  public void focusLost(FocusEvent e)
  {
    processFocusEvent(e);
  }

  @Override
  public void processFocusEvent(FocusEvent e)
  {
    super.processFocusEvent(e);
    if (eventVector.isEmpty())
      return;
    if (e.getOppositeComponent() == combo)
      return;
    for (FocusListener eventVector1 : eventVector)
    {
          int id = e.getID();
          //FocusListener listener = (FocusListener) eventVector1;
          if (eventVector1 != null)
          {
              switch (id)
              {
                  case FocusEvent.FOCUS_GAINED:
                      eventVector1.focusGained(e);
                      break;
                  case FocusEvent.FOCUS_LOST:
                      eventVector1.focusLost(e);
                      break;
              }
          }
    }
  }
  @Override
  public synchronized void addKeyListener(KeyListener l) {
      if (texto!=null)
        texto.addKeyListener(l);
  }
  public synchronized void addCambioListener(CambioListener cambioListener)
  {
    texto.resetCambio();
    cambioListenerList.add(cambioListener);
  }

  public synchronized void removeCambioListener(CambioListener cambioListener)
  {
    cambioListenerList.remove(cambioListener);
  }

  protected void processCambio()
  {
    if (!texto.isEnabled() ||  ! hasCambio())
      return;

    copia=texto.getText();
    CambioEvent ev = new CambioEvent(this);
    for (CambioListener cambioListenerList1 : cambioListenerList)
    {
         cambioListenerList1.cambio(ev);
    }
  }
  /**
   * Indica la anchura del combo una vez desplegado.
   * @param anc 
   */
  public void setAnchoComboDesp(int anc)
  {
      combo.setPreferredSize(new Dimension(anc,this.getPreferredSize().height));
  }
  /**
   * Indica cuanto tiene que ser el ancho del campo texto
   * @param anc 
   */
  public void setAncTexto(int anc)
  {
    ancTexto = anc;
    texto.setPreferredSize(new Dimension(ancTexto, 16));
    texto.setMinimumSize(new Dimension(ancTexto, 16));
    validate();
    repaint();
  }

  public int getAncTexto()
  {
    return ancTexto;
  }

  protected EventListenerList listenerList = new EventListenerList();
  transient protected ChangeEvent changeEvent = null;

  // Force this to be implemented.
  @Override
  public Object getCellEditorValue()
  {
    return getText() + " - " + getTextCombo();
  }

  /**
   * Returns true.
   * @param e  an event object
   * @return true
   */
  @Override
  public boolean isCellEditable(EventObject e)
  {
    return true;
  }

  /**
   * Returns true.
   * @param anEvent  an event object
   * @return true
   */
  @Override
  public boolean shouldSelectCell(EventObject anEvent)
  {
    return true;
  }

  /**
   * Calls <code>fireEditingStopped</code> and returns true.
   * @return true
   */
  @Override
  public boolean stopCellEditing()
  {
    fireEditingStopped();
    return true;
  }

  /**
   * Calls <code>fireEditingCanceled</code>.
   */
  @Override
  public void cancelCellEditing()
  {
    fireEditingCanceled();
  }

  /**
   * Adds a <code>CellEditorListener</code> to the listener list.
   * @param l  the new listener to be added
   */
  @Override
  public void addCellEditorListener(CellEditorListener l)
  {
    listenerList.add(CellEditorListener.class, l);
  }

  /**
   * Removes a <code>CellEditorListener</code> from the listener list.
   * @param l  the listener to be removed
   */
  @Override
  public void removeCellEditorListener(CellEditorListener l)
  {
    listenerList.remove(CellEditorListener.class, l);
  }

  /**
   * Returns an array of all the <code>CellEditorListener</code>s added
   * to this AbstractCellEditor with addCellEditorListener().
   *
   * @return all of the <code>CellEditorListener</code>s added or an empty
   *         array if no listeners have been added
   * @since 1.4
   */
  public CellEditorListener[] getCellEditorListeners()
  {
    return (CellEditorListener[]) listenerList.getListeners(
        CellEditorListener.class);
  }

  /**
   * Notifies all listeners that have registered interest for
   * notification on this event type.  The event instance
   * is created lazily.
   *
   * @see EventListenerList
   */
  protected void fireEditingStopped()
  {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2)
    {
      if (listeners[i] == CellEditorListener.class)
      {
        // Lazily create the event:
        if (changeEvent == null)
          changeEvent = new ChangeEvent(this);
        ( (CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
      }
    }
  }

  /**
   * Notifies all listeners that have registered interest for
   * notification on this event type.  The event instance
   * is created lazily.
   *
   * @see EventListenerList
   */
  protected void fireEditingCanceled()
  {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2)
    {
      if (listeners[i] == CellEditorListener.class)
      {
        // Lazily create the event:
        if (changeEvent == null)
          changeEvent = new ChangeEvent(this);
        ( (CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
      }
    }
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected,
                                               int row, int column)
  {
    if (value != null)
      this.setText(getTexto(value.toString()));
    return this;
  }

  /**
   * Devuelve el texto del combo sobre el texto
   * tipico de un linkbox.
   * El texto sera con el formato "1 - XXXX"
   *
   * @param text String Texto con formato "N - XXXXX"
   * @return String Devuelve la parte antes del guion
   */

  public String getTexto(String text)
  {
    if (text == null)
      return null;
    int p = text.indexOf("-");
    if (p > 0)
      text = text.substring(0, p - 1);
    return text.trim();
  }
  /**
   * Retorna el valor del Texto a partir de la cadena que une texto+combobox
   * @param text Texto a parsear
   * @see getTexto
   * @return int con el valor del texto. Si no se pudo convertir a int,
   * devolvera 0
   */
  public int getTextoInt(String text) throws NumberFormatException
  {
    return Integer.parseInt(getTexto(text));
  }
  public CTextField getTextField()
  {
      return texto;
  }

  /**
   * Devuelve el texto del combo sobre el texto
   * tipico de un linkbox.
   * El texto sera con el formato "1 - XXXX"
   * Utilizado para los gridEditables
   *
   * @param text String Texto con formato "N - XXXXX"
   * @return String Devuelve la parte despues del guion
   */
  public String getTextoCombo(String text)
  {
    if (text == null)
      return "";
    int p = text.indexOf("-");
    if (p > 0)
    {
      if (text.length() >= p + 2)
        text = text.substring(p + 2);
      else
        text = "";
    }
    return text.trim();
  }
  @Override
  public boolean hasCambio()
  {
    return texto.hasCambio();
  }
  @Override
  public void resetCambio()
  {
    texto.resetCambio();
  }

  public void setHasCambio(boolean hasCambio)
  {
    texto.setCambio(hasCambio);
  }


  public boolean isNull(boolean trim)
  {
    return texto.isNull(trim);
  }
  public boolean isNull()
  {
    return texto.isNull();
  }
  public void setEditable(boolean edit)
  {
    if (texto==null)
      return;
    texto.setEditable(edit);
    combo.setEnabled(edit);
  }
  @Override
  public boolean isEditable()
  {
    if (texto==null)
      return false;
    return texto.isEditable();
  }

  @Override
  public void setEditableParent(boolean editable)
  {
    setEditable(editable);
  }

} // Final de La clase
