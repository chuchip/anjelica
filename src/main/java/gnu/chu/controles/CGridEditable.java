package gnu.chu.controles;

/*
<p>Título: JTable Editable Avanzado</p>
 * <p>Descripción: Extiende de CGrid. permite modificar el valor de los campos
 * facilmente. Controlando validacion en cambio de columnas y filas.
 * </p> 
 * <p>Copyright: Copyright (c) 2005-2015
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU segun es publicada por
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
 * @author chuchi P.
 * @version 3.1  (9/1/2013)
 */
import gnu.chu.eventos.GridEvent;
import gnu.chu.eventos.GridListener;
import gnu.chu.interfaces.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;


public class CGridEditable extends Cgrid implements CQuery {
    private int rowEdit=0;
    private boolean query=false;
    final private  String TIPO_LINKBOX="L";
    final private  String TIPO_TEXTFIELD="T"; 
    final private  String TIPO_COMBOBOX="C";
    
    ArrayList grListener=new ArrayList();
    private boolean lineaEditable = true;
    private boolean ponValoresEnabled = false;
    private boolean ponValoresInFocus = false;
    boolean reqFocusEdit = false;
    public boolean binsert = false; // Indica si se ha pulsado el boton insertar (en cambiaLinea)

    int colNueva = 0; // Col. donde ir cuando se inserta una Linea
    private int eatCambioLinea = 0;
    int eatCambioCol = 0;
    int colIni = 0;
    int colFin = 0;
    int nColErr = 0; // Columna a la que ir en caso de error.
    boolean swEvent = false; // Procesando Eventos.
    private // Procesando Eventos.
            int antRow = 0;
    private int antColumn = 0;
//  int nColuT = 0;
    ArrayList campos = null;
    ArrayList <String> tCampo;
    ArrayList  vCampo = null; // Valores por defecto
    boolean swIniciar = false;
//  boolean canInsertLinea= true; // Indica si se pueden insertar Nuevas Lineas
//  boolean canBorrarLinea=true; // Indica si se pueden borrar lineas.

  public CGridEditable()
  {
    
    super();
  }
  
  public CGridEditable(int numcol)
  {    
    setGridEditable(true);
    iniciar(numcol);
  }
  /**
   * @deprecated 
   * Usado anteriormente para que no siguiera con el proceso despues de insertar linea
   * @param aa 
   */
  public void  setProcInsLinea(boolean aa)
  {
      
  }
  public void iniciar(int numcol)
  {
    try {
      if (swIniciar)
          throw new Exception("No se puede llamar a esta funcion más que una vez");
      setNumColumnas(numcol);
    } catch (Exception k)
    {
       System.out.println(k.getMessage());
       return;
    }
    swIniciar=true;
    setBotonBorrar();
    setBotonInsert();

    tableView.setRowSelectionAllowed(false);
    tableView.setColumnSelectionAllowed(false);
    tableView.setCellSelectionEnabled(true);
    tableView.getTableHeader().setResizingAllowed(false);
    tableView.getTableHeader().setReorderingAllowed(false);
   
    super.setOrdenar(false);
    activarEventos();
    this.setEnabled(false);
  }

  private void activarEventos()
  {
    this.setButton(KeyEvent.VK_F8, null);
    this.setButton(KeyEvent.VK_F7, null);
    tableView.getColumnModel().addColumnModelListener(new
        TableColumnModelListener()
    {
      public void columnAdded(TableColumnModelEvent e)
      {}

      public void columnRemoved(TableColumnModelEvent e)
      {}

      public void columnMoved(TableColumnModelEvent e)
      {}

      public void columnMarginChanged(ChangeEvent e)
      {}

      public void columnSelectionChanged(ListSelectionEvent e)
      {
        if (e.getValueIsAdjusting()) // || antColumn==getSelectedColumn())
          return;
        if (eatCambioCol > 0)
        {
          eatCambioCol--;
          return;
        }
//        System.out.println(" Col: "+getValString(0,5)+ " - "+getValString(1,5));
        procCambiaCol(e);
      }
    });
    tableView.getSelectionModel().addListSelectionListener(new
        ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        if ( getEatCambioLinea() > 0)
        {
          setEatCambioLinea(getEatCambioLinea() - 1);
          return;
        }
        if (e.getValueIsAdjusting()) // && e.getFirstIndex() == e.getLastIndex())
          return;
        if (!puedeCambiarLinea())
        {
          if (getSelectedColumn() != getAntColumn())
            eatCambioCol++;
          return;
        }
        cambiaLinea();
      }
    });

    tableView.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (!isEnabled())
          return;
        if ( ( (JComponent) campos.get(tableView.getSelectedColumn())).
            isEnabled())
        {
//          System.out.println("Tecla consumida "+e.getKeyChar());
          e.consume();
          return;
        }
        switch (e.getKeyCode())
        {
          case KeyEvent.VK_F8: // Borrar Linea Activa.
            SwingUtilities.invokeLater( new Thread()
            {
                    @Override
              public void run()
              {
                Bborra.doClick();
              }
            });
            break;
          case KeyEvent.VK_F7: // Insertar Nueva Linea
            SwingUtilities.invokeLater( new Thread()
            {
                    @Override
              public void run()
              {
                Binser.doClick();
              }
            });
        }
      }
    });
  }

  void procCambiaCol(ListSelectionEvent e)
  {
    int col = getSelectedColumn();
    if (isEnabled() && campos != null)
    {
      if (col == getAntColumn() && e != null)
      {
        if (e.getFirstIndex() != col)
          col = e.getFirstIndex();
        else if (e.getLastIndex() != col)
          col = e.getLastIndex();
      }
//      System.out.println("antColumn: " + antColumn);
      cambiaColumna0(getAntColumn(),col);
    }
    cambiaLinea();
    setAntColumn(col);
  }
  /**
   * Llamada por CTextField para ver que campo del grid debe actualizar.
   * @return row a editar (setValor)
   */
  public int getRowEditada()
  {
      return rowEdit==0?getSelectedRow():rowEdit;
  }
  boolean puedeCambiarLinea()
  {
    if (!isEnabled() || campos == null || swEvent || getAntRow() == getSelectedRow())
    {
      if (!isEnabled())
        afterCambiaLineaDis(getSelectedRowDisab());
      return true;
    }
    swEvent = true;
    if (getSelectedRow() == 0 && TABLAVACIA)
      TABLAVACIA = false;
//    System.out.println("Antigua Row: "+antRow);
    ponValores(getAntRow());//,true,true);
    rowEdit=getAntRow();
    if ( (nColErr = cambiaLinea1(getAntRow(), getAntColumn())) >= 0)
    {
      rowEdit=0;
      swEvent = false;
      requestFocus(getAntRow(),nColErr);
      return false;
    }
    rowEdit=0;

    if (getAntColumn() != getSelectedColumn())
    {
      eatCambioCol++;
      cambiaColumna0(getAntColumn(),getSelectedColumn(), getAntRow());
    }
    else
      cambiaColumna0( getAntColumn(),getSelectedColumn(), getAntRow());

    setAntRow(getSelectedRow());
    setAntColumn(getSelectedColumn());
    ponValores(getAntRow(), false, false);
    afterCambiaLinea0();
    swEvent = false;
    return true;
  }

  void cambiaLinea()
  {
    if (isEnabled() && campos != null  )
    {
      if (tableView.getSelectedRow()<0 || tableView.getSelectedColumn()<0)
        return;
      if (tableView.isCellEditable(tableView.getSelectedRow(),
                                   tableView.getSelectedColumn()))
      {
        if (! tCampo.get(getSelectedColumn()).equals("C"))
         tableView.editCellAt(tableView.getSelectedRow(),
                             tableView.getSelectedColumn());
      }
    }
  }
 @Override
  protected void Bborra_actionPerformed()
  {
    setEatCambioLinea(2);
    int rw;
    if (!TABLAVACIA)
    {
      if (tableView.isEditing())
        tableView.editingStopped(new ChangeEvent(this));
      rw = tableView.getSelectedRow();

      if (!deleteLinea(rw, tableView.getSelectedColumn()))
      {
        requestFocus(getSelectedRow(), getSelectedColumn());
        return;
      }
      int nRow = getRowCount();
      removeLinea(rw);
      boolean swVacio=false;
      if (TABLAVACIA)
        swVacio=true;
      boolean vacio=isVacio();
      if (rw == nRow - 1)
        rw = (rw > 0) ? rw - 1 : 0;
      requestFocus(rw, getSelectedColumn());
      ponValores();
      resetCambio();
      afterDeleteLinea();
      afterCambiaLinea0();
      if (vacio)
      {
        if (!afterInsertaLinea0(true))
            return;
      }
      setAntRow(rw);
      TABLAVACIA=swVacio;
    }
    else
    {
      requestFocus(0, getSelectedColumn());
      setAntRow(0);
    }
  }
  /**
   * Añadir listener sobre eventos del grid.
   * @param gridListener
   */
  public void addGridListener(GridListener gridListener)
  {
    grListener.add(gridListener);
  }
   public void removeGridListener(GridListener gridListener)
  {
    grListener.remove(gridListener);
  }
   /**
    *
    * Procesa los cambios en el grid (cambio columna o cambio linea)
    * ademas de si se ha insertado linea o no.
    *
    * @param ev GridEvent con los parametros q indican tipo evento
    * @param swColu Es evento tipo cambio Columna
    */
  protected void processGridEvent(GridEvent ev, boolean swColu)
  {
    for (int i = 0; i < grListener.size(); i++)
    {
        if (swColu)
          ( (GridListener) grListener.get(i)).cambioColumna(ev);
        else
        {
          ( (GridListener) grListener.get(i)).cambiaLinea(ev);
        }
    }
  }
  /**
   * Esta rutina sera llamda, cada vez que se borra la linea.
   * Si retorna false, NO SE BORRARA.
   * @param  row linea
   * @param   col columna
   * @return true si puede borrar linea
   */
    @Override
    public boolean deleteLinea(int row, int col) {
        if ( getQuery())
         return false;
        GridEvent grEvent = new GridEvent(this);
        grEvent.setColumna(col);
        grEvent.setLinea(row);
        for (int i = 0; i < grListener.size(); i++) {
            ((GridListener) grListener.get(i)).deleteLinea(grEvent);
            if (! grEvent.getPuedeBorrarLinea())
                return false;
        }
        return canBorrarLinea;
    }

  public void setCanDeleteLinea(boolean canDelete)
  {
    Bborra.setEnabled(canDelete);
    canBorrarLinea = canDelete;
  }

  public boolean getCanDeleteLinea()
  {
    return canBorrarLinea;
  }

    @Override
  protected void Binser_actionPerformed(boolean insLinea)
  {
    binsert = true;
    int cl = -1;
    int colAnt = getSelectedColumn();
    int rw;
    if (tableView.isEditing())
    {
      if (tCampo.get(colAnt).equals("T"))
      {
        if (( (CTextField) campos.get(colAnt)).isEnabled() && ( (CTextField) campos.get(colAnt)).isEditable())
          ( (CTextField) campos.get(colAnt)).procesaSalir();
        if ( ( (CTextField) campos.get(colAnt)).getError() &&
            ( (CTextField) campos.get(colAnt)).isEnabled() &&
            ( (CTextField) campos.get(colAnt)).isEditable())
        {
          return;
        }
      }
      tableView.editingStopped(new ChangeEvent(this));
    }

    boolean il = insertaLinea1(getSelectedRow(), getSelectedColumn());
    if (il == true)
    {
      ponValores(getSelectedRow());
      cl = cambiaLinea1(getSelectedRow(), getSelectedColumn());
    }
    if (cl >= 0 || !il)
    {
      requestFocus(getSelectedRow(), cl);
      binsert = false;
      return;
    }
    eatCambioCol++;
    cambiaColumna0(getSelectedColumn(),colNueva);
        setEatCambioLinea(2);
    ArrayList v = new ArrayList();
    insLinea(v);
    rw = getSelectedRow();
    addLinea(v, rw);
    requestFocus(rw, colNueva);
    if (!afterInsertaLinea0(insLinea))
        return;
    afterCambiaLinea0();
    binsert = false;
  }

  public void setLineaEditable(boolean linEditable)
  {
    if (campos==null)
      return;
    lineaEditable=linEditable;
    for (int n=0;n<vCampo.size();n++)
    {
      if (campos.get(n) instanceof CEditable)
          ( (CEditable) campos.get(n)).setEditableParent(lineaEditable);
    }
  }

  public boolean isLineaEditable()
  {
    return this.lineaEditable;
  }

    @Override
  public void setEnabled(boolean enab)
  {
    super.setEnabled(enab);

  
    setEditable(enab);
    Pboton.setEnabled(enab);
//    Binser.setEnabled(enab);
    if (!enab)
      tableView.editingStopped(new javax.swing.event.ChangeEvent(tableView));
  }
  /**
   * Un grieditable nunca puede ser ordenable.
   * @param ordenable
   */
    @Override
  public void setOrdenar(boolean ordenable)
  {
      swOrden=false;
  }
 
  /**
   * Indica si se pueden ajustar el ancho de las columnas con el raton
   *
   * @param ajAncCol Ajustar s/n
   */
  public void setAjusAncCol(boolean ajAncCol)
  {
    tableView.getTableHeader().setResizingAllowed(ajAncCol);
  }

  public boolean getAjusAncCol()
  {
    return tableView.getTableHeader().getResizingAllowed();
  }
  /**
   * @deprecated
   * @see setCampos(ArrayList)
   * @param v 
   */
  public void setCampos(Vector v) throws Exception
  {
      setCampos(new ArrayList(v));
  }
  /**
   * Establece los campos sobre los que se editara.
   * Si el JComponent devuelve false en  el getRequestFocusEnabled() cuando
   * se camabie el campo anterior a este; Este campo sera ignorado.
   * Es decir si estamos en el campo 1 y el campo 2 esta puesto con setRequestFocusEnabled(false)
   * al pulsar TAB o ENTER se pasara al campo 3, no al 2. Sin embargo este campo se podra editar
   * moviendose con el raton o con el cursor atras (SHIFT+TAB)
   * @param v Vector con JComponent. Actualmente soporta JTextField, JComboBox, JCheckBox y
   * JLinkBox
   * @throws IllegalArgumentException En caso de que el numero de campos mandados o los tipos de estos
   * no sean correctos.
   */
  public void setCampos(ArrayList v) throws IllegalArgumentException, ClassNotFoundException
  {
      if (!swIniciar)
          throw new IllegalArgumentException("Llame primero a la funcion Iniciar");
    if (v.size() != nCol)
    {
      msgError = "(setCampos) No de Campos(" + v.size() +
          ") NO coincide con No Columnas(" + nCol + ") del Grid";
      setEnabled(true);
      throw new IllegalArgumentException(msgError);
    }
    campos = v;
    tCampo = new ArrayList();
    vCampo = new ArrayList();
    colIni = nCol;
    colFin = 0;
    DefaultCellEditor dce;
    for (int n = 0; n < nCol; n++)
    {
      final Component comp = (Component) v.get(n);
      if (Class.forName("gnu.chu.controles.CButton").isAssignableFrom(comp.getClass()))
      { 
        ((CButton) comp).setMargin(new Insets(0,0,0,0));
        tableView.getColumn(tableView.getColumnName(n)).setCellRenderer(new CButton());
        tableView.getColumn(tableView.getColumnName(n)).setCellEditor(
            new miCellEditor( (CButton) comp));
        vCampo.add( ( (CButton) comp).getText());
        tCampo.add("b");
//        ( (CLinkBox) comp).texto.setBackground(Color.yellow);
//        ( (CLinkBox) comp).texto.setForeground(Color.black);
        ( (CButton) comp).addActionListener(new ActionListener()
        {
                    @Override
          public void actionPerformed(ActionEvent e)
          {
            javax.swing.SwingUtilities.invokeLater(new Thread()
            {
                            @Override
              public void run()
              {
                requestFocusSelected();
              }
            });
          }
        });
      }

      if (Class.forName("gnu.chu.controles.CLinkBox").isAssignableFrom(comp.
          getClass()))
      {
        tableView.getColumnModel().getColumn(n).setCellEditor( (CLinkBox) comp);
        vCampo.add( ( (CLinkBox) comp).getText());
        ( (CLinkBox) comp).setGridEditable(this);
        tCampo.add("L");
        ( (CLinkBox) comp).texto.setBackground(Color.yellow);
        ( (CLinkBox) comp).texto.setForeground(Color.black);
        ( (CLinkBox) comp).texto.addKeyListener(new tfKeyListener( (CEditable)
            comp, n, this));
        ( (CLinkBox) comp).texto.setGridEditable(this);
      }

      if (Class.forName("gnu.chu.controles.CComboBox").isAssignableFrom(comp.
          getClass())  )
      {
        tableView.getColumnModel().getColumn(n).setCellEditor(new
            DefaultCellEditor( (JComboBox) v.get(n)));
        
        ( (CComboBox) comp).setGridEditable(this);
        vCampo.add( ( (CComboBox) comp).getText());

        tCampo.add("C");

      }
      if (Class.forName("gnu.chu.controles.CCheckBox").isAssignableFrom(comp.
          getClass()))
      {
        if( Formato[n].equals(""))
              setFormatoColumna(n, "B"+((CCheckBox) comp).getCharSelect()+
                            ((CCheckBox) comp).getCharNoSelect());
        tableView.getColumnModel().getColumn(n).setCellEditor(new
            DefaultCellEditor( (CCheckBox) comp));
        if (Formato[n].equals("") || Formato[n].charAt(0) != 'B')
          vCampo.add( ( (CCheckBox) comp).getSelecion());
        else
          vCampo.add(( (CCheckBox) comp).isSelected());

        comp.setBackground(Color.yellow);
        comp.setForeground(Color.black);

        tCampo.add("B");
        ( (CCheckBox) comp).setHorizontalAlignment(AbstractButton.CENTER);
        ( (CCheckBox) comp).setGridEditable(this);
        comp.addKeyListener(new tfKeyListener( (CEditable) comp, n, this));
        ( (CCheckBox) comp).addActionListener(new ActionListener()
        {
                    @Override
          public void actionPerformed(ActionEvent e)
          {
            javax.swing.SwingUtilities.invokeLater(new Thread()
            {
              @Override
              public void run()
              {
                requestFocusSelected();
              }
            });
          }
        });
      }

      if (Class.forName("gnu.chu.controles.CTextField").isAssignableFrom(comp.
          getClass()))
      {
        tableView.getColumnModel().getColumn(n).setCellEditor(new
            DefaultCellEditor( (CTextField) comp)
        {
           @Override
          public Object getCellEditorValue()
          {
            if (getQuery())
                return ( (CTextField) comp).getText();
            else
                return ( (CTextField) comp).getTexto();
          }
                    @Override
          public boolean stopCellEditing()
          {
            if (! ((CTextField) comp).puedoSalir())
               return false;
            return super.stopCellEditing();
          }
          

        });
        vCampo.add( ( (CEditable) comp).getText());
        ( (CTextField) comp).setGridEditable(this);
        ( (CTextField) comp).setRowGrid(n); 
//        if ( ( (CTextField) comp).getTipoCampo()==Types.DATE)
//          setFormatoColumna(n, ((CTextField) comp).getFormato() );
        tCampo.add("T");
        comp.setBackground(Color.yellow);
        comp.setForeground(Color.black);
        comp.addKeyListener(new tfKeyListener( (CEditable) comp, n, this));
      }
      if (comp.isEnabled())
        comp.addFocusListener(new focusAdaptGrid(this, n));
      setCellEditable(comp.isEnabled(), n);
      if (comp.isEnabled() && colIni > n)
        colIni = n;
      if (comp.isEnabled())
        colFin = n;
    }
    if (colNueva == 0)
      colNueva = colIni;
//    ponValores(0);
  }
  /**
   * Pone a los campos el mismo formato que tengan los Objetos
   * mandados en setCampos (solo si son CTextField).
   */
  public void setFormatoCampos()
  {
      for (int n=0;n<this.getColumnCount();n++)
      {
            try {
                if (Class.forName("gnu.chu.controles.CTextField").isAssignableFrom(campos.get(n).getClass())) 
                {
                    if (((CTextField) campos.get(n)).getTipoCampo() == Types.DECIMAL) {                            
                        setFormatoColumna(n, ((CTextField) campos.get(n)).getFormato());
                    }
                    if (((CTextField) campos.get(n)).getTipoCampo() == Types.DATE) {
                        setFormatoColumna(n, ((CTextField) campos.get(n)).getFormato());
                    }
                }
                 if (Class.forName("gnu.chu.controles.CCheckBox").isAssignableFrom(campos.get(n).getClass())) 
                 {                      
                        setFormatoColumna(n, "B"+((CCheckBox) campos.get(n)).getCharSelect()+
                            ((CCheckBox) campos.get(n)).getCharNoSelect());
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CGridEditable.class.getName()).log(Level.SEVERE, null, ex);
            }
      }

  }
  public void requestFocusSuper(int row, int col)
  {
        setAntColumn(col);
    super.requestFocus(row, col);
  }

  public void requestFocusFinal()
  {
    requestFocus(getRowCount() - 1, 0);
  }
  public void requestFocusFinalLater()
  {
    requestFocusLater(getRowCount() - 1, 0);
  }
  public void requestFocusInicioLater()
  {
    SwingUtilities.invokeLater(new Thread()
    {
      public void run()
      {
        requestFocusInicio();
      }
    });

  }
    @Override
  public void requestFocusInicio()
  {
    if (!isEnabled())
    {
      super.requestFocus(0, 0);
      return;
    }
    setLineaEditable(true);
    setAntColumn(colIni);
    setAntRow(0);
    super.requestFocus(0, colNueva);
    
    if ( tCampo !=null && ! tCampo.get(colNueva).equals("C"))
        tableView.editCellAt(0, colNueva);
    tableView.scrollRectToVisible(tableView.getCellRect(0, colNueva, true));
    if (! this.isVacio())
      this.ponValores(0);
    resetCambio();
  }
  /**
   * Devuelve true si se esta editando un campo.
   * @return true si se esta editando un campo.
   */
  public boolean isEditando()
  {
      return tableView.isEditing();      
  }
  /**
   * Devuelve columna que se esta editando (0 es la primera)
   * @return columna que se esta editando (0 es la primera)
   */
  public int getColumnaEditando()
  {
      return tableView.getEditingColumn();
  }
  /**
   * Devuelve Linea que se esta editando (0 es la primera)
   * @return Linea que se esta editando (0 es la primera)
   */
  public int getLineaEditando()
  {
      return tableView.getEditingRow();
  }
  
  
  public void requestFocus(int col)
  {
      requestFocus(getSelectedRow(),col);
  }
    @Override
  public void requestFocus(int row, int col)
  {
    int rowAnt=getSelectedRow();
    if (!isEnabled() || campos == null)
    {
      super.requestFocus(row, col);
      return;
    }

    if (row < 0)
      row = 0;

    if (col < 0)
      col = 0;
    if (col >= nCol)
      col = nCol - 1;
    int col1 = -1;
    col1=getProxColEdi(col-1);

    if (col1==col-1)
      col1=getProxColEdi(-1);

    if (col1 == -1)
      col1 = 0;
    super.requestFocus(row, col1);
    setAntColumn(col1);
    if (getAntRow()!=row)
    {
      if (! this.isVacio())
      {
        if (isPonValoresInFocus())
          this.ponValores(row);
      }
      resetCambio(); // Si cambio la linea reseteo los flags de cambio
    }
    setAntRow(row);
    int rowEdit = row;
    int colEdit = col1;
    try {
        
         if (! tCampo.get(colEdit).equals("C") || rowAnt==rowEdit)
         {
            tableView.editCellAt(rowEdit, colEdit);
            tableView.scrollRectToVisible(tableView.getCellRect(rowEdit, colEdit, true));
         }
    } catch (ArrayIndexOutOfBoundsException k)
    {
         Logger.getLogger(CGridEditable.class.getName()).log(Level.SEVERE, "Error en editCell. Row: "+rowEdit+" Col:"+colEdit, k);
    }
  }
     /**
    * Función para eliminar la fila especificada del grid.
    * @param fila fila  a borrar.
    * @return true siempre
    */
    @Override
     public boolean removeLinea(int fila){
       try {
         if (isEnabled())
         {
          if (tableView.isEditing())
            tableView.editingStopped(new javax.swing.event.ChangeEvent(tableView));
         }
         super.removeLinea(fila);
 
       } catch (ArrayIndexOutOfBoundsException k)
       {
         Logger.getLogger(CGridEditable.class.getName()).log(Level.SEVERE, "Error en removeLinea Fila: "+fila, k);
       }
       return true;
     }

  public void requestFocusLater()
  {
    requestFocusLater(getSelectedRow(),getSelectedColumn());
  }

    @Override
  public void requestFocusLater(final int row,final int col)
  {
    SwingUtilities.invokeLater(new Thread()
       {
            @Override
         public void run()
         {
           CGridEditable.this.requestFocus();
           CGridEditable.this.requestFocus(row,col);
         }
       });
  }

  /**
   * Pasa al Grid los valores de los campos (CTextField, CComboBox, etc.)
   *
   * Se llamara desde fuera, cuando se quiera dar por finalizada la carga
   * en el grid.
   * @deprecated usar salirGrid
   */
  public void procesaAllFoco()
  {
    salirGrid();
  }
  /**
   * Pasa los valores de los CEditable al grid
   * (realiza un setValor de todas los campos al grid)
   */
  public void setValCamposToGrid()
  {      
      if (campos==null)
       return;
     int linea=tableView.getSelectedRow();
     procesaAllFoco(linea);    
  }
  /**
   * Pasa al Grid los valores de los campos (CTextField, CComboBox, etc.)
   * de la linea mandada al grid.
   *
   * Se llamara desde fuera, cuando se quiera dar por finalizada la carga
   * en el grid.
   * @param linea procesa foco en esta linea
   */
  public void procesaAllFoco(int linea)
  {
    ArrayList v = new ArrayList();
    for (int nCampo = 0; nCampo < nCol; nCampo++)
      v.add(actValGrid(linea, nCampo));
    setLinea(v, linea);
  }
  /**
   * Devuelve el valor que hay en el campo
   * mandado por el numero de Linea y Numero de Campo (Columna).
   * La diferencia getValor* es que devuelve el valor de los campos CTextField,
   * CComboBox, etc, excepto si estan disabled que los coge del Grid directamente.
   * @param linea int Numero de Linea
   * @param nCampo int Numero de Campo (Columna)
   * @return Object
   */
  private Object actValGrid(int linea, int nCampo)
  {
    CCheckBox chT;
    if (tCampo.get(nCampo).equals("T"))
    {
      if (! ( (CTextField) campos.get(nCampo)).isEnabled() || ! ( (CTextField) campos.get(nCampo)).isEditable())
        return getValString(linea, nCampo); // No esta enabled o editable. Devuelvo el valor del grid
      if ( ( (CTextField) campos.get(nCampo)).getTipoCampo() ==  Types.DATE)
        return ( (CTextField) campos.get(nCampo)).getFecha();
      else
        return ( (CTextField) campos.get(nCampo)).getText();
    }
    if (tCampo.get(nCampo).equals("L"))
    {
      if (! ( (CLinkBox) campos.get(nCampo)).isEnabled() || ! ( (CLinkBox) campos.get(nCampo)).isEditable())
        return getValString(linea, nCampo); // No esta enabled o editable. Devuelvo el valor del grid
      return ( (CLinkBox) campos.get(nCampo)).getText();
    }
    if (tCampo.get(nCampo).equals("B") )
    {
      chT = ( (CCheckBox) campos.get(nCampo));
      if (!chT.isEnabled())
        return getValBoolean(linea,nCampo);
      if (Formato[nCampo].equals("") || Formato[nCampo].charAt(0) != 'B')
      {
        return chT.isSelected() ? chT.getStringSelect() : chT.getStringNoSelect();
      }
      else
        return chT.isSelected();
    }
    if (tCampo.get(nCampo).equals("C") )
    {
      if (! ( (CComboBox) campos.get(nCampo)).isEnabled() )
        return getValString(linea, nCampo); // No esta enabled o editable. Devuelvo el valor del grid
      return ( (CComboBox) campos.get(nCampo)).getText();
    }
    if (tCampo.get(nCampo).equals("b") )
    {
      if (! ( (CButton) campos.get(nCampo)).isEnabled())
           return getValString(linea, nCampo);
      return ( (CButton) campos.get(nCampo)).getText();
    }

    return "";
  }

  /**
   * Pasa los valores del grid a los campos editables (CEditable)
   */
  public void ponValores()
  {
    ponValores(getSelectedRow(), true, false);
  }
  /**
   * Pasa los valores del grid a los campos editables (CEditable)
   * @param linea int Numero de linea del grid a pasar.
   */
  public void ponValores(int linea)
  {
    ponValores(linea, false, true);
  }
  /**
   * Pone los valores del grid a los Campos editables (CTextField, etc.)
   * @param linea Linea a la q poner los valores
   * @param foc boolean si true NO Poner el valor si el compenente tiene el foco
   * @param salir boolean si true NO Pone el valor si n es igual a la columna selecionada.
   */
  public void ponValores(int linea, boolean foc, boolean salir)
  {
    if (tCampo==null || tCampo.isEmpty())
        return;
    if (TABLAVACIA)
    {
      for (int n = 0; n < nCol; n++)
      {
        if (tCampo.get(n).equals("T"))
        {
            if (getQuery())
              ( (CTextField) campos.get(n)).resetTexto();
            else
            {
               if (( (CTextField) campos.get(n)).getTipoCampo()==Types.DATE && vCampo.get(n) instanceof java.util.Date)
               {
                   ( (CTextField) campos.get(n)).setDate((java.util.Date) vCampo.get(n));
               }
               else
               {
                    ( (CTextField) campos.get(n)).setText(vCampo.get(n).toString());
               }
            }
        }
        if (tCampo.get(n).equals("P"))
          ( (CEditable) campos.get(n)).setText(vCampo.get(n).toString());
        if (tCampo.get(n).equals("B"))
        {
          if (Formato[n].equals("") || Formato[n].charAt(0) != 'B')
            ( (CCheckBox) campos.get(n)).setSelecion(vCampo.get(n).toString());
          else
            ( (CCheckBox) campos.get(n)).setSelected( ( (Boolean) vCampo.get(n)));
        }
        if (tCampo.get(n).equals("C"))
          ( (CComboBox) campos.get(n)).setValor(vCampo.get(n).toString());
        if (tCampo.get(n).equals("L"))
          ( (CLinkBox) campos.get(n)).setText(vCampo.get(n).toString());
        if (tCampo.get(n).equals("b") )
          ( (CButton) campos.get(n)).setText(vCampo.get(n).toString());
      }
    }
    else
    {
      for (int n = 0; n < nCol; n++)
        ponValores1(n, linea, foc, salir);
    }
  }
  /**
   * Pone a los campos (CTextField,etc) los valores del GRID.
   * @deprecated usar actualizarGrid()
   */
  public void salirFoco()
  {
    actualizarGrid();
  }
  /**
   * Pasa a los componentes editables (CTextField,CCombBox, etc) los calores actuales del Grid.
   * Usar cuando se hayan modificado externamente los campos del grid (setValor) y se
   * desee sincronizar con los valores de los componentes
   */
  public void actualizarGrid()
  {
     actualizarGrid(getSelectedRow());  
  }
   /**
   * Pasa a los componentes editables (CTextField,CCombBox, etc) los calores de la linea madanda.
   * 
   * Usar cuando se hayan modificado externamente los campos del grid (setValor) y se
   * desee sincronizar con los valores de los componentes
   */
  public void actualizarGrid(int row)
  {
       ponValores(row, false, false);
  }
  /**
   * Pone a los campos Ctextfield, etc, los valores de la linea mandada
   * como parametro
   * @deprecated
   * @param row Linea 
   */
  public void salirFoco(int row)
  {
    ponValores(row, false, false);
  }
  /**
   * Ejecutar cuando se salga del grid.
   * (en ej_edit, por ejemplo)
   * Pasa los campos de los campos editables al grid
   */
  public void salirGrid()
  {
     // Paro la edicion si el grid esta enabled
    if (isEnabled())
    {
      if (tableView.isEditing())
        tableView.editingStopped(new javax.swing.event.ChangeEvent(tableView));
    }
    procesaAllFoco(tableView.getSelectedRow());
  }

  /**
   * Pone a los campos (CTextField,etc) los valores del GRID.
   * @param col int Columna del grid de donde coger el valor
   * @param linea int Linea del grid de donde coger el valor
   * @param foc boolean si true NO Poner el valor si el compenente tiene el foco
   * @param salir boolean si true NO Pone el valor si n es igual a la columna selecionada.
   */
  public void ponValores1(int col, int linea, boolean foc, boolean salir)
  {
    if (getValString(col) == null)
      return;
    if (this.getSelectedColumn() == col && salir)
      return;
    if ( ( (Component) campos.get(col)).isEnabled() == false && !isPonValoresEnabled())
      return;
    
    if ( ( (Component) campos.get(col)).hasFocus() && foc)
      return;
    switch (tCampo.get(col))
    {
        case "P":
          ( (CEditable) campos.get(col)).setText(getValString(linea, col).trim());
            break;
        case "T":
           if ( ( (CTextField) campos.get(col)).getTipoCampo() == Types.DECIMAL)
             ( (CTextField) campos.get(col)).setText(getValString(linea, col, true));
           else
            ( (CEditable) campos.get(col)).setText(getValString(linea, col));
           break;
        case "B":    
            if (Formato[col].equals("") || Formato[col].charAt(0) != 'B')
                ( (CCheckBox) campos.get(col)).setSelecion(getValString(linea, col));
            else
                ( (CCheckBox) campos.get(col)).setSelected(getValBoolean(linea, col));
            break;
        case "C":
            if (!( (CComboBox) campos.get(col)).setValor(getValString(linea, col)))
                ( (CComboBox) campos.get(col)).setText(getValString(linea, col));
            break;
        case "L":
           ( (CLinkBox) campos.get(col)).setText(getValString(linea, col)); 
            break;
        case "b":
           ( (CButton) campos.get(col)).setText(getValString(linea, col));
    }   
  }

  public void procesaTecla(KeyEvent e, CEditable comp, int columna)
  {
    JTable table = tableView;
    int row = table.getSelectedRow();
    int col = table.getSelectedColumn();
    if (e.isControlDown() || e.isShiftDown())
      return;

    CTextField tf = null;
    boolean isLkBox = false;
    try
    {
      if (Class.forName("gnu.chu.controles.CLinkBox").isAssignableFrom(comp.
          getClass()))
      {
        tf = ( (CLinkBox) comp).texto;
        isLkBox = true;
      }
      if (Class.forName("gnu.chu.controles.CTextField").isAssignableFrom(comp.
          getClass()))
        tf = (CTextField) comp;
    }
    catch (Exception k)
    {}
    int rw;
    char tecla = e.getKeyChar();
    switch (e.getKeyCode())
    {
      case KeyEvent.VK_RIGHT:
        procesaTeclaRigth(e,tf, table, col, row);
        return;
      case KeyEvent.VK_LEFT:
        procesaTeclaLeft(e,tf, table, col, row);
        return;
      case KeyEvent.VK_DOWN: // Baja de Linea
 //       if (!isLkBox)
          procesaTeclaDown(e, columna);
        break;
      case KeyEvent.VK_UP: //Sube
        e.consume();
        if (getSelectedRow() == 0)
          return;
        if (tCampo.get(columna).equals("T"))
          this.setValor(( (CTextField) campos.get(columna)).getText(),columna);
        cambiaColumna0(getSelectedColumn(),getSelectedColumn());
        ponValores(getSelectedRow());
        if ( (col = cambiaLinea1(getSelectedRow(), getSelectedColumn())) >= 0)
        {
          if (col != getSelectedColumn())
          {
            eatCambioCol++;
            requestFocus(getSelectedRow(), col);
            return;
          }
          else
          {
//           eatCambioCol++;
            requestFocus(getSelectedRow(), col);
            return;
          }
        }
        setEatCambioLinea(getEatCambioLinea() + 1);

        requestFocus(getSelectedRow() - 1, getSelectedColumn());
        ponValores(getSelectedRow());
        afterCambiaLinea0();
        if (tCampo.get(columna).equals("T"))
          ( (CTextField) campos.get(columna)).selectAll();
        break;
      case KeyEvent.VK_F8: // Borrar Linea Activa.
           SwingUtilities.invokeLater( new Thread()
            {
              public void run()
              {
                Bborra.doClick();
              }
            });
        break;
      case KeyEvent.VK_F7: // Insertar Nueva Linea
            SwingUtilities.invokeLater( new Thread()
            {
              public void run()
              {
                 Binser.doClick();
              }
            });
    }
  }

  void procesaTeclaDown(KeyEvent e, int columna)
  {
    e.consume();
    if (tCampo.get(columna).equals("T"))
    {
      ( (CTextField) campos.get(columna)).leePesoBasc();
      ( (CTextField) campos.get(columna)).procesaSalir();  
      if (( (CTextField) campos.get(columna)).getError()) 
      {
          msgError=( (CTextField) campos.get(columna)).getMsgError();
          return;
      }
      this.setValor(( (CTextField) campos.get(columna)).getText(),columna);
    }

//    if (tCampo.get(nColuT).equals("T"))
//      this.setValor(( (CTextField) campos.get(nColuT)).getText(),columna);
    mueveSigLinea(columna, true);
    if (tCampo.get(columna).equals("T"))
      ( (CTextField) campos.get(columna)).selectAll();
  }

  void procesaTeclaRigth(KeyEvent e,CTextField tf, JTable table, int col, int row)
  {
    if (tf != null)
    {
      if (tf.getCaretPosition() != tf.getTextSuper().length())
        return;
    }
    int columna= getSelectedColumn();
    if (tCampo.get(columna).equals("T"))
    {     
      ( (CTextField) campos.get(columna)).procesaSalir();  
      if (( (CTextField) campos.get(columna)).getError()) 
      {
          msgError=( (CTextField) campos.get(columna)).getMsgError();
          e.consume();
          return;
      }
    }
    if (table.getSelectedColumn() == getUltColAct())
    {
      mueveSigLinea(colIni);
      return;
    }
    cambiaColumna0(col, row);
    col = getProxColEdi(getSelectedColumn());// table.getSelectedColumn() + 1;
    requestFocus(row, col);
  }

  void procesaTeclaLeft(KeyEvent ke,CTextField tf, JTable table, int col, int row)
  {
    if (tf == null)
    {
      procesaTeclaLeft1(ke,table, col, row);
      return;
    }
    
    if (tf.getCaretPosition() == 0 ||
        (tf.getSelectionStart() == 0 &&
         tf.getSelectionEnd() == tf.getTextSuper().length()))
    {
      procesaTeclaLeft1(ke,table, col, row);
    }
  }

  void procesaTeclaLeft1(KeyEvent ke,JTable table, int col, int row)
  {
    int columna= getSelectedColumn();
    if (tCampo.get(columna).equals("T"))
    {     
      ( (CTextField) campos.get(columna)).procesaSalir();  
      if (( (CTextField) campos.get(columna)).getError()) 
      {
          msgError=( (CTextField) campos.get(columna)).getMsgError();
          ke.consume();
          return;
      }
    }
    if (table.getSelectedColumn() == colIni)
    { // Subir de Columna
      if (getSelectedRow() > 0)
        procCambiaLinea(getSelectedRow(), getSelectedRow() - 1, col,
                        colFin);
      return;
    }
    int colnueva1 = getAntColEdit(getSelectedColumn());
    cambiaColumna0(col, colnueva1,getSelectedRow());
    col=colnueva1;
    requestFocus(row, col);
  }

  private void afterCambiaLinea0()
  {
    resetCambio();
    afterCambiaLinea();
  }
  /**
   * LLama a cambia Linea si no hay ningun error interno
   * @param row
   * @param col
   * @return 
   */
  public int cambiaLinea1(int row, int col)
  {
    int n;
    for (n = 0; n < nCol; n++)
    {
      if (tCampo.get(n).equals("T") )
      { // Si el Campo es TextField y tiene Error y esta enabled y Editable
        if (( (CTextField) campos.get(n)).isEnabled() && ( (CTextField) campos.get(n)).isEditable())
            ( (CTextField) campos.get(n)).procesaSalir();
        if (( (CTextField) campos.get(n)).getError() &&
           ((CTextField) campos.get(n)).isEnabled() &&
           ((CTextField) campos.get(n)).isEditable())
        {
         ( (CTextField) campos.get(n)).getMsgError();
          return n;
        }
      }
    }
    if (getReordenando())
        return -1;

    n = cambiaLinea(row, col);  
    return n;
  }

  void cambiaColumna0(int col,int colNueva)
  {
    cambiaColumna0(col,colNueva, getSelectedRow());
  }

  private void cambiaColumna0(int col, int colNueva,int row)
  {
    if (col < 0)
        return;
    if (tCampo.get(col).equals("L"))
    {
      ( (CLinkBox) campos.get(col)).setText( ( (CLinkBox) campos.
          get(col)).getText());
      this.setValor( ( (CLinkBox) campos.get(col)).getText() + " - " +
                    ( (CLinkBox) campos.get(col)).getTextCombo(), row,
                    col);
    }
    cambiaColumna(col, colNueva,row);
  }
  /**
   * Funcion a Machacar si se quiere controlar algo cuando se cambia una columna
   * Se deberia usar grEvent  en vez de machacar.
   * @param col int
   * @param colNueva int
   * @param row int
   */
    @Override
  protected void cambiaColumna(int col,int colNueva,int row)
   {
     GridEvent grEvent=new GridEvent(this);
     grEvent.setColumna(col);
     grEvent.setLinea(row);
     grEvent.setLineaNueva(getSelectedRow());
     grEvent.setColNueva(colNueva);

     processGridEvent(grEvent, true);
     afterCambiaColumna(col,colNueva,row);
   }
  
    @Override
   protected void cambiaColumna(int col,int row)
   {
       cambiaColumna(col,col,row);
   }
   /**
    * Resetea el cambio de todas las variables del Grid
    */
    @Override
   public void resetCambio()
   {
     if (campos==null)
       return;
     int nCol = campos.size();
     for (int n = 0; n < nCol; n++)
     {
       if (campos.get(n)==null)
         continue;
       ( (CEditable) campos.get(n)).resetCambio();
     }
   }

   /**
    * Comprueba si ha habido cambios en algun campo de la linea del grid
    * @return true si ha habido cambios en el grid
    */
    @Override
   public boolean hasCambio()
   {
     if (campos==null)
       return false;
     int nCol = campos.size();
     for (int n = 0; n < nCol; n++)
     {
       if (campos.get(n)==null)
         continue;

       if ( ( (CEditable) campos.get(n)).hasCambio())
         return true;
     }
     return false;
   }

  /**
   * Machacar esta funcion si quiere controlar algo una vez
   * que ya se ha cambiado la linea
   * Los Component mandados en setCampos estaran ya actualizados al valor
   * de la linea actual del grid.
   * @see addGridListener
   */
  public void afterCambiaLinea()
  {
     GridEvent grEvent=new GridEvent(this);
     grEvent.setColumna(getSelectedColumn());
     grEvent.setLinea(getSelectedRow());
     
     for (Object grListener1 : grListener)
     {
            ((GridListener) grListener1).afterCambiaLinea(grEvent);
     }
  }

  public boolean isCampo(Component c)
  {
    for (Object campo : campos)
    {
        if (c == campo)
            return true;
    }
    return false;
  }

    @Override
  void addDefaultRow()
  {
    if (vCampo == null)
    {
      super.addDefaultRow();
      return;
    }
    ArrayList v1 = new ArrayList();
    insLinea(v1);
    datosModelo.addRow(new Vector(v1));
  }

  /**
   * Machacar esta funcion si quiere controlar algo Cuando se cambie la linea
   * Se deberia usar GridEvent
   * Es llamada cuando el grid esta disabled. Los componentes tendran el valor
   * de la ultima linea activa.
     * @param nRow numero de linea a procesar
   * 
   */
  public void afterCambiaLineaDis(int nRow)
  {
     GridEvent grEvent=new GridEvent(this);
     grEvent.setColumna(getSelectedColumnDisab());
     grEvent.setLinea(nRow);
     grEvent.setColNueva(getSelectedColumnDisab());
     for (Object grListener1 : grListener)
     {
            ((GridListener) grListener1).afterCambiaLineaDis(grEvent);
     }
  }

  /**
   * Esta rutina sera llamada, cada vez que se cambia la linea.
   * Si retorna >=0, NO SE CAMBIARA y se saltara al Campo devuelto.
   * <strong>El primer campo es 0</strong>
   * Sustituirla en la definición del grid para realizar alguna acción
   * cuando se cambie de linea.
   * Se deberia usar addGridListener(gridListener x) y tratar ahi el evento. 
   * @ see getColError en  gnu.chu.eventos.GridEvent
   * @param row Linea
   * @param col Columna
   * @return campo al que ir por error. < 0 si todo ha ido bien
   * @see addGridListener
   */

  public int cambiaLinea(int row, int col)
  {
      
     GridEvent grEvent=new GridEvent(this);
     grEvent.setColumna(col);
     grEvent.setLinea(row);
     grEvent.setLineaNueva(getSelectedRow());
    
     grEvent.setColNueva(col);
     processGridEvent(grEvent,false);
     return grEvent.getColError();
  }

  public void mueveSigLinea()
  {
    mueveSigLinea(getSelectedColumn());
  }
  /**
   * Mueve el cursor a la siguiente linea, en la columna especificada
   * @param columna  Columna donde posicionarse despues de mover a la siguiente linea.
   */
  public void mueveSigLinea(int columna)
  {
    mueveSigLinea(columna, true);
  }

  void mueveSigLinea(int columna, boolean focus)
  {
    boolean swInsLinea;
    int row = getSelectedRow();
    int rw = tableView.getSelectedRow() + 1;
    if (rw >= tableView.getRowCount())
    { // Tengo q insertar una nueva linea.      
      if (TABLAVACIA)
        TABLAVACIA = false;
      cambiaColumna0(tableView.getSelectedColumn(),tableView.getSelectedColumn(), row);
      ponValores(getSelectedRow(), focus, false);
      binsert = false;
      if (!insertaLinea1(getSelectedRow(), columna))
      { // Anulado
        requestFocus(tableView.getSelectedRow(), columna);
        return;
      }

      if ( (nColErr = cambiaLinea1(getSelectedRow(), columna)) >= 0)
      { // Anulado       
        requestFocusLater(tableView.getSelectedRow(), nColErr);
        return;
      }
      setAntRow(rw);
      swInsLinea = true;

      ArrayList v = new ArrayList();
      insLinea(v);
      addLinea(v);
      
      ponValores(rw, false, false);

      if (swInsLinea)
      {
        if (! afterInsertaLinea0(false))
            return;
      }
      afterCambiaLinea0();
      requestFocus(rw, colNueva); 
//      SwingUtilities.invokeLater(new Thread()
//      {
//        public void run()
//        {
//          try
//          {
//            Thread.sleep(100);
//          }
//          catch (InterruptedException k)
//          {}
//          ( (Component) campos.get(colNueva)).requestFocus();
//        }
//      });
    }
    else // No hay que insertar una nueva linea.
      procCambiaLinea(row, row + 1, getSelectedColumn(), columna);
  }

  boolean procCambiaLinea(int rowAnt, int rowNueva, int colAnt, int colNueva)
  {
    cambiaColumna0(colAnt,colNueva, rowAnt);
    ponValores(rowAnt);
    if ( (nColErr = cambiaLinea1(rowAnt, colAnt)) >= 0)
    { // Me dicen que no cambie de Linea.
      if (nColErr != colAnt)
        eatCambioCol++;
      requestFocusLater (rowAnt, nColErr);
      return false;
    }
        setEatCambioLinea(1);
    if (colAnt != colNueva)
      eatCambioCol++;
    requestFocus(rowNueva, colNueva);
    ponValores(rowNueva, false, false);
    afterCambiaLinea0();
    return true;
  }
  /**
   * Devuelve un ArrayList con la linea x defecto a insertar.
   * @return 
   */
  public ArrayList getLineaDefecto()
  {
      ArrayList aa=new ArrayList();
      insLinea(aa);
      return aa;
  }
  /**
   * Llena el Vector mandado con los valores por defecto para una Nueva Linea
   */
  private void insLinea(ArrayList v)
  {
    for (int n = 0; n < nCol; n++)
    {
      if (tCampo.get(n).equals("T") ||
          tCampo.get(n).equals("P") ) // Texto o Editable
        v.add(vCampo.get(n));
      if (tCampo.get(n).equals("B") )
         v.add(vCampo.get(n));
      if (tCampo.get(n).equals("C") ) // Combo
        v.add(vCampo.get(n));
      if (tCampo.get(n).equals("L") ) // CLinkBox
        v.add(vCampo.get(n));
      if (tCampo.get(n).equals("b")) // Button
        v.add(vCampo.get(n));
    }
  }
    /**
     * Pone el valor por defecto para una columna. Se usara cada vez que se inserte una linea
     * @param col
     * @param valor 
     */
  public void setDefaultValor(int col,Object valor)
  {
    vCampo.set(col,valor);
  }
  /**
   * Machacar esta función si se quiere controlar algo despues de Insertar una linea
   * insLinea = true se ha insertado una linea con F7 o el boton
   *          = false es una nueva linea al final del grid
   * @return true si debe permitir insertar una nueva linea
   * @param insLinea
   */
  public boolean afterInsertaLinea(boolean insLinea)
  {
     GridEvent grEvent=new GridEvent(this);
     grEvent.setColumna(getSelectedColumn());
     grEvent.setLinea(getSelectedRow());
     for (Object grListener1 : grListener)
     {
        if (! ((GridListener) grListener1).afterInsertaLinea(grEvent))
            return false;
     }
     return true;
  }
  
  
  /**
   * Machacar esta funcion si se quiere controlar algo despues de Insertar una linea
   * insLinea = true se ha insertado una linea con F7 o el boton
   *          = false es una nueva linea al final del grid
   */
  private boolean afterInsertaLinea0(boolean insLinea)
  {    
    return afterInsertaLinea(insLinea);
  }

  
/**
 * Funcion a machacar cuando se quiera hacer algo despues de borrar linea
 *
 */
    @Override
  public void afterDeleteLinea()
  {
     GridEvent grEvent=new GridEvent(this);
     grEvent.setColumna(getSelectedColumn());
     grEvent.setLinea(getSelectedRow());
     for (Object grListener1 : grListener)
     {
        ((GridListener) grListener1).afterDeleteLinea(grEvent);
     }
  }

  Vector datosLinea()
  {
    CCheckBox chT;
    Vector v = new Vector();
    for (int nCampo = 0; nCampo < nCol; nCampo++)
    {
      if (tCampo.get(nCampo).equals("T") ||
          tCampo.get(nCampo).equals("P") )
        v.addElement( ( (CEditable) campos.get(nCampo)).getText());
      if (tCampo.get(nCampo).equals("B"))
      {
        if (Formato[nCampo].equals("") || Formato[nCampo].charAt(0) != 'B')
        {
          chT = ( (CCheckBox) campos.get(nCampo));
          v.addElement(chT.isSelected() ? chT.getStringNoSelect() :
                       chT.getStringNoSelect());
        }
        else
          v.addElement(( (CCheckBox) campos.get(nCampo)).isSelected());
      }
      if (tCampo.get(nCampo).toString().compareTo("C") == 0)

//        if ( ( (CComboBox) campos.get(nCampo)).isAsignadoValorBD())
//          v.addElement( ( (CComboBox) campos.get(nCampo)).getValor());
//        else
        v.addElement( ( (CComboBox) campos.get(nCampo)).getText());

      if (tCampo.get(nCampo).toString().compareTo("L") == 0)
        v.addElement( ( (CLinkBox) campos.get(nCampo)).getText());
      if (tCampo.get(nCampo).toString().compareTo("b") == 0)
        v.addElement( ( (CButton) campos.get(nCampo)).getText());

    }
    return v;
  }

  /**
   * Esta rutina sera llamda, cada vez que se inserta la linea.
   * Si retorna false, NO SE CAMBIARA.
   *
     * @param row
     * @param col
     * @return 
   */
  public boolean insertaLinea1(int row, int col)
  {
     if ( getQuery())
         return false;
    for (int n = 0; n < nCol; n++)
    {
      if (tCampo.get(n).compareTo("T") == 0)
      { // Si el Campo es TextField y tiene Error y esta enabled
        if ( ( (CTextField) campos.get(n)).isEnabled() && ( (CTextField) campos.get(n)).isEditable())
          ( (CTextField) campos.get(n)).procesaSalir();
        if ( ( (CTextField) campos.get(n)).getError() &&
            ( (CTextField) campos.get(n)).isEnabled() &&
            (( (CTextField) campos.get(n)).isEditable() ))
        {
          return false;
        }
      }
    }
    return insertaLinea(row, col);
  }
  /**
   * Machacar clase si se desea hacer algo antes de insertar linea
   * No se deberia machacar. Usar en su lugar addGridListener
   * @see GridListener 
   * @param row
   * @param col
   * @return true si se puede insertar linea. false en caso contrario.
   */
  public boolean insertaLinea(int row, int col)
  {
       if ( getQuery())
         return false;
        GridEvent grEvent = new GridEvent(this);
        grEvent.setColumna(col);
        grEvent.setLinea(row);
        for (int i = 0; i < grListener.size(); i++) {
            if (!((GridListener) grListener.get(i)).insertaLinea(grEvent))
                return false;
        }      
        return canInsertLinea;
  }

  public void setCanInsertLinea(boolean insertLinea)
  {
    Binser.setEnabled(insertLinea);
    canInsertLinea = insertLinea;
  }

  public boolean getCanInsertLinea()
  {
    return canInsertLinea;
  }

  void procesaEnter(KeyEvent ke)
  {    
    if (ke.isAltDown() || ke.isControlDown() || ke.isShiftDown())
      return;
    int columna=getSelectedColumn();
    if (tCampo.get(columna).equals("T"))
    {     
      ( (CTextField) campos.get(columna)).procesaSalir();  
      if (( (CTextField) campos.get(columna)).getError()) 
      {
          msgError=( (CTextField) campos.get(columna)).getMsgError();
          ke.consume();
          return;
      }
    }
    int ultColAct = getUltColAct();
    if (getSelectedColumn() == ultColAct)
    {
      procesaTab(ke);
      return;
    }
    eatCambioCol++;
    int colNueva = -1;
    colNueva = getProxColEdi(getSelectedColumn());
    cambiaColumna0(getSelectedColumn(),colNueva);
    int col = getSelectedColumn() + 1;
    requestFocus(getSelectedRow(), colNueva);
        setAntColumn(col - 1);
    procCambiaCol(null);
    ke.consume();
  }

  int getUltColAct()
  {
    return getUltColAct(getSelectedColumn());
  }

  int getUltColAct(int colActual)
  {
    int ultColAct = colActual;
    for (int n = colActual + 1; n < nCol; n++)
    {
      if ( ( (Component) campos.get(n)).isEnabled())
        ultColAct = n;
    }
    return ultColAct;
  }
  /**
   * Establece si debe realizarse un requestFocus por defecto a las columnas que no son
   * editables. Por defecto es false.
   * @param ReqFocusEdit boolean true si debe hacerse un req. Focus por defecto aunque el
   * cambo no sea swGridEditable. Solo valido para TextFields
   */
  public void setReqFocusEdit(boolean ReqFocusEdit)
  {
    this.reqFocusEdit=ReqFocusEdit;
  }
  /**
   *
   * @return boolean true si se hara un request focus a las columnas con un campo no swGridEditable
   */
  public boolean getReqFocusEdit()
  {
   return reqFocusEdit;
  }

  /**
   * Devuelve la proxima columna swGridEditable
   * @param colActual int Columna a partir de la que buscar
   * @return int Proxima Columna Editable. La misma columna si no hay ninguna swGridEditable
   */
  int getProxColEdi(int colActual)
  {
    for (int n = colActual + 1; n < nCol; n++)
    {
      if ( ( (Component) campos.get(n)).isEnabled())
      {
        if (! ((JComponent) campos.get(n)).isRequestFocusEnabled())
          continue;
        if (tCampo.get(n).equals(TIPO_TEXTFIELD))
        {
          if ( ( (CTextField) campos.get(n)).isEditable() || reqFocusEdit)
            return n;
        }
       
        if (tCampo.get(n).equals(TIPO_LINKBOX))
        {
            if   (( (CLinkBox) campos.get(n)).isEditable() || reqFocusEdit)
                return n;
        }
        if (tCampo.get(n).equals(TIPO_COMBOBOX))       
                return n;

      }
    }
    return colActual;
  }

  void procesaTab(KeyEvent ke)
  {
    int row = getSelectedRow();
    if (ke.isAltDown() || ke.isControlDown())
      return;
    eatCambioCol++;
    int colNueva;
    int columna=getSelectedColumn();
    if (tCampo.get(columna).equals("T"))
    {     
      ( (CTextField) campos.get(columna)).procesaSalir();  
      if (( (CTextField) campos.get(columna)).getError()) 
      {
          msgError=( (CTextField) campos.get(columna)).getMsgError();
          ke.consume();
          return;
      }
    }
    if (!ke.isShiftDown())
    {
      int ultColAct = getUltColAct();
      if (getSelectedColumn() == ultColAct )
      {
        cambiaColumna0(getSelectedColumn(),0);
        mueveSigLinea(0);
        if (ke.getKeyCode() == KeyEvent.VK_ENTER || ke.getKeyCode() == KeyEvent.VK_TAB)
          ke.consume();
      }
      else
      {
        ke.consume();
        colNueva=getProxColEdi(getSelectedColumn());
        cambiaColumna0(getSelectedColumn(),colNueva);
        requestFocus(getSelectedRow(), colNueva);
      }
    }
    else
    {
      if (getSelectedColumn() == colIni)
      {
        if (row > 0)
          row--;
        if (!procCambiaLinea(getSelectedRow(), row, getSelectedColumn(), colFin))
        {
          ke.consume();
          return;
        }
        setEatCambioLinea(1);
        cambiaColumna0(getSelectedColumn(),colFin);
        requestFocus(row, colFin);
        ke.consume();
      }
      else
      {
        ke.consume();
        colNueva=getAntColEdit(getSelectedColumn());
        cambiaColumna0(getSelectedColumn(),colFin);
        requestFocus(getSelectedRow(),colNueva );
      }
    }
  }

  int getAntColEdit(int col)
  {
    for (int n = col - 1; n >= 0; n--)
    {
      if ( ( (Component) campos.get(n)).isEnabled())
      {
        if (tCampo.get(n).equals("T"))
        {
          if ( ( (CTextField) campos.get(n)).isEditable() || reqFocusEdit)
            return n;
        }
        else
          return n;
      }
    }
    return col;
  }

  /**
   * Establece la Columna donde ira cuando se inserte una nueva linea.
   * La primera columna es 0.
   * por defecto es igual a colIni (0)
   *
   * @param colNue Columna donde ir
   */
  public void setColNueva(int colNue)
  {
    colNueva=colNue;
  }
  public int getColNueva()
  {
    return colNueva;
  }
    @Override
  public void removeAllDatos(){
      
        if (SwingUtilities.isEventDispatchThread())
        {
          removeAllDatos_(); 
        }
        else
        {
          try {
            SwingUtilities.invokeAndWait(new Runnable()
            {
                    @Override
               public void run()
               {
                   removeAllDatos_();
               }
            });
            
          } catch (InterruptedException ex) {
            Logger.getLogger(Cgrid.class.getName()).log(Level.SEVERE, null, ex);
          } catch (InvocationTargetException ex) {
            Logger.getLogger(Cgrid.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
    }
    
   void removeAllDatos_()
   {
    removeAllDatos_Cgrid();
  
    if (isPonValoresInFocus() )
      ponValores();
    TABLAVACIA=true;
  }
  public void setQuery(boolean swQuery)
  {
    query=swQuery;   
    if (campos==null)
       return;
     int n1=getColumnCount();
     for (int n = 0; n < n1 ; n++)
     {
       if (campos.get(n)==null)
         continue;
       if (campos.get(n) instanceof CQuery  )
         ( (CQuery) campos.get(n)).setQuery(swQuery);
     }
  }
    @Override
  public boolean getQuery() {return query;}
  /**
   * Establece si se deben poner los campos del Grid a los editables cuando se realiza 
   * un requestFocus.
   * Por defecto es false.
   * @param ponValInFocus 
   */
  public void setPonValoresInFocus(boolean ponValInFocus)
  {
    ponValoresInFocus=ponValInFocus;
  }

  public boolean getPonValoresInFocus()
  {
    return isPonValoresInFocus();
  }
  public void setPonValoresEnabled(boolean ponValorEnab)
  {
    ponValoresEnabled=ponValorEnab;
  }
  /**
   * @deprecated use isPonValoresEnabled
   * @return 
   */
  public boolean getPonValoresEnabled()
  {
    return isPonValoresEnabled();
  }

    /**
     * Especifica si el parametro ponvaloresEnabled esta activo.
     * @return the ponValoresEnabled
     */
    public boolean isPonValoresEnabled() {
        return ponValoresEnabled;
    }

    /**
     * @return the ponValoresInFocus
     */
    public boolean isPonValoresInFocus() {
        return ponValoresInFocus;
    }

    /**
     * @return the eatCambioLinea
     */
    public int getEatCambioLinea() {
        return eatCambioLinea;
    }

    /**
     * @param eatCambioLinea the eatCambioLinea to set
     */
    public void setEatCambioLinea(int eatCambioLinea) {
        this.eatCambioLinea = eatCambioLinea;
    }

    /**
     * @return the antRow
     */
    public int getAntRow() {
        return antRow;
    }

    /**
     * @param antRow the antRow to set
     */
    public void setAntRow(int antRow) {
        this.antRow = antRow;
    }

    /**
     * @return the antColumn
     */
    public int getAntColumn() {
        return antColumn;
    }

    /**
     * @param antColumn the antColumn to set
     */
    public void setAntColumn(int antColumn) {
        this.antColumn = antColumn;
    }

}
class tfKeyListener extends KeyAdapter
{
  CEditable tf;
  int nCampo;
  JTable table;
  CGridEditable jt;
  public tfKeyListener(CEditable c,int nCampo,CGridEditable jt)
  {
    this.tf = c;
    this.nCampo = nCampo;
    this.jt=jt;
    table=jt.tableView;
  }
    @Override
  public void keyPressed(KeyEvent e)
  {
    jt.procesaTecla(e,tf,nCampo);

  }
}

class focusAdaptGrid extends FocusAdapter
{
  int nCol;
  CGridEditable padre;
  public focusAdaptGrid(CGridEditable grid, int col)
  {
    nCol=col;
    padre=grid;
  }
  @Override
  public void focusGained(java.awt.event.FocusEvent e)
  {
      
       if (e.getComponent() instanceof CComboBox )
           ((CComboBox) e.getComponent()).showPopup();
       if (padre.getTengoFoco() )
       {
        if (e.getOppositeComponent()==padre.tableView || e.getOppositeComponent()==null
          || e.getOppositeComponent() == padre  )
          return;
        for (int n=0;n<padre.nCol;n++)
        {
           if (e.getOppositeComponent()==padre.campos.get(n))
            return;
         }
       }
       padre.setTengoFoco(true);
       GridEvent grEvent=new GridEvent(padre);
       grEvent.setFocusGained(true);
       grEvent.setColumna(-1);
       grEvent.setLinea(padre.getSelectedRow());
       grEvent.setColNueva(padre.getSelectedColumn());
       for (Object grListener1 : padre.grListener)
       {
            ((GridListener) grListener1).focusGained(grEvent);
       }
      
//      
  }
    @Override
  public void focusLost(java.awt.event.FocusEvent e)
  {
    if (e.getOppositeComponent()==padre.tableView || e.getOppositeComponent()==null
        || e.getOppositeComponent() == padre)
      return;

    for (int n=0;n<padre.nCol;n++)
    {
      if (e.getOppositeComponent()==padre.campos.get(n))
        return;
    }
    padre.setTengoFoco(false);
    GridEvent grEvent=new GridEvent(padre); 
    grEvent.setFocusLost(true);
    grEvent.setColumna(-1);
    grEvent.setLinea(padre.getSelectedRow());
    grEvent.setColNueva(padre.getSelectedColumn());
    for (Object grListener1 : padre.grListener)
    {
        ((GridListener) grListener1).focusLost(grEvent);
    }
    
  }
}
