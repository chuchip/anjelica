package gnu.chu.controles;

/**
 * 
 * <p>Título: JTable Avanzado</p>
 * <p>Descripción: JTable con carga desde un DatosTabla, posiblidades de poner
 * color facilmente, ajuste de columnas, cabeceras, ordenar etc.
 * Tambien permite salvar la configuracion (anchura y orden de las columnas)
 * </p>
 * <p>Copyright: Copyright (c) 2005-2014
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
 * @version 2.0 Incluido control de transaciones. (@see initTransation) 
 * 
 */
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Color;
import java.util.*;
import gnu.chu.sql.*;
import java.sql.*;
import gnu.chu.utilidades.*;
import gnu.chu.isql.*;
import javax.swing.tree.TreeCellEditor;

public class Cgrid extends CPanel implements Serializable
{
  private boolean  tengoFoco=false;
  private ToolTipHeader headerToolTip;
  private boolean res=false;
  private boolean activo=true;
  private boolean activoPanel=true;
  private boolean swPrepaBotonInsDele=true;
  boolean canInsertLinea= true; // Indica si se pueden insertar Nuevas Lineas
  boolean canBorrarLinea=true; // Indica si se pueden borrar lineas.
  String descrCabecReport=null;
  boolean swGridEditable = false;
  DatosTabla datTabla;
  DatosTabla dt;
  conexion conex;
  CPanel Pboton = new CPanel();
  public CButton Bborra = new CButton("Borrar",Iconos.getImageIcon("delete-row"))
  {
        @Override
    public boolean isFocusable()
    {
      return false;
    }
  };
  public CButton Binser = new CButton("Insertar",Iconos.getImageIcon("insert-row"))
  {
        @Override
    public boolean isFocusable()
    {
      return false;
    }
  };

  EntornoUsuario entUsu=null;
    public static int DERECHA=2;
    public static int CENTRO=1;
    public static int IZQUIERDA=0;
    private int posYview=0;
    int rowFocus=0;
    int columnFocus=0;
    Point viewPosition=new Point(0,0);
    private boolean swLanzaCargaDatosThread = false;
    //
    private boolean llame=false;
    // Nnmero de columnas de la tabla.
    public int nCol=0;
    public boolean swAdd=false;
    // Variable para decir si queremos que el sorter actue al pulsar en la
    // Cabecera de la tabla.
    boolean swOrden=true; // indica si es ordenable
    private boolean buscarVisible=true;
    private boolean cellEditable=false;
    private boolean cuadrar=false; // respeta el tamano minimo en cuadrar()
    String nombre = new String("Grid");

    // String para obtener mensajes de error.
    public String msgError="";

    int PID;
  
    public int ca; // Columna Activa.
    public int ra; // Row Activa
    boolean foco;
    boolean clickCons=false;
    boolean activoMenu=true;
    boolean enabled=true;

    boolean cargandoDatos=false; //Nos indica si se estan cargando datos en el grid
                                 // Cuando la carga es en Background

    boolean gridActivo=false;//Para activar el evento del scrPanel

    // Para anadir un menú desplegable sobre la tabla con cuatro acciones
    // para utilizarlo habrn que declarar el grid de tipo pad(setPad(true)).
    JPopupMenu popMenu = new JPopupMenu();
    JMenu copyItem = new JMenu("Copiar");
    JMenuItem copyCellItem = new JMenuItem("Celda");
    JMenuItem copyRowItem = new JMenuItem("Linea");
    JMenuItem copyColItem = new JMenuItem("Columna");
    JMenuItem copyAllItem = new JMenuItem("Todo");
//    JMenuItem impAllItem = new JMenuItem("Imprimir");
    JMenuItem addItem = new JMenuItem("Alta",'A');
    JMenuItem editItem = new JMenuItem("Edicion",'E');
    JMenuItem bajaItem = new JMenuItem("Baja",'B');
    JMenuItem salirItem = new JMenuItem("Salir",'S');
    JMenuItem configItem = new JMenuItem("Salvar Configuracion",'C');
    JMenuItem resetItem = new JMenuItem("Configuracion por Defecto",'D');

    BorderLayout borderLayout1=new BorderLayout();

    public CPanel panelG=new CPanel(null); // Panel Principal
    public CPanel panelBusqueda=new CPanel(); // Panel para buscar datos en el grid
    public CPanel panelBuscar=new CPanel(null); // Panel que se pone encima del panelBusqueda
    CLabel Titulo=new CLabel("Buscar:");
    CTextField busquedaE=new CTextField(); // Texto a buscar
    CButton Bajustar=new CButton(Iconos.getImageIcon("ajustar")); // Se pone encima del panelBusqueda

    String[] cabeImp = null;
    String nombImp = "grid";
    String descImp = "Volcado de Pantalla";
    /**
    *  Me indica si el grid estn en modo de ajuste del ancho de columnas automatico.
    **/
    public boolean AJUSTAR=false;
    /**
    * Cabecera superior de la tabla,(campos de los datos).
    */
     public Vector cabecera=new Vector();

    /**
    * Valores iniciales de los datos,(datos por defecto).
    */
    public Vector datos=new Vector();


    // ***** Elementos que conforman la tabla.!! TableModel y JTable son obligatorios. *****
    /**
    * Definición de como van a ser los datos. Es obligatorio hacerlo.
    */
    public miDefaultTableModel datosModelo;

    /**
    * Representacinn de la tabla.
    */
    public JTable tableView ;
    /**
    * Barras de desplazamiento para la tabla.
    */
    public CScrollPane scrPanel;

    // Estructura de ordenación.
    public TableSorter sorter;
    // ***** final de los elementos que conforman la tabla. *****

    // Me indica si el contenido de la tabla estn vacio.
    public boolean TABLAVACIA=true;

    // Para indicar que en la opcinn de buscar se tengan en cuenta,o no,las maynsculas
    private boolean mayusculas=true;

    // Habrn un render por cada columna(afecta al diseno de las columnas)
    private miCellRender[] renders;

    // Formato de las columnas,aplicado en filtrarGrid
    String[] Formato;

    // Variable Global donde se dejaremos el valor Double de un campo del grid
    Double ValorDec;

    // Variable utilizada para saber si hay que guardar la configuracinn;
    public boolean CONFIGURAR=false;

    // Variable para saber si pad
    public boolean MENU=false;

    // Variables para guardar configuracinn del grid
    String nomPrg="";

    // Vector para almacenar los parnmetros de la configuracion por defecto del grid.
    private Vector valAnteriores=new Vector();
    private Vector identif=new Vector();

    int rowSelec=0,colSelec=0;//Celda seleccionada
    int numRegistros=50;//Numero de registros a recargar cada vez que se avanza
    int puntoDeScroll=50;//Punto en el que debe de saltar el evento para mns datos


//    int[] ordencols;
    boolean ordenarCols=false;
    protected boolean finRecargar=true;
    private boolean esDatosTabla=false;// Cargamos datosTabla o Vector

    private boolean swAjustarGrid=false;

    protected double anchopanelOld = 0;
    protected boolean swAsignaMinWith = false;
    protected boolean swPonPanel = false;
    private int rowCopy = 0, colCopy = 0;
    private boolean inTransation=false;
    private ArrayList<ArrayList> datosSinc=new ArrayList();
    private HashMap<Point ,Boolean> datosUpd=new HashMap();
    
    // Creacion de un grid vacio.
    public Cgrid(){
      this(0,true);
    }

    // El parametro es para indicar el nnmero de columnas de la tabla
    // y asn poder crear un modelo de datos.
    public Cgrid(int numcol){
      this(numcol,true);
    }

    // El parametro booleano se utiliza para activar o desactivar
    // la opción de que ordene los datos al pulsar sobre el JTableHeader(cabecera).
    public Cgrid(int numcol,boolean orden){
      swOrden=orden;
      try{
          nCol=numcol;
          jbInit();
        }
        catch(Exception e){
            SystemOut.print(e);
        }
    }
    /**
     * Pone el grid en modo transacion.
     * A partir de este momento todas los setDato y getDato actuaran sobre el buffer.
     * 
     */
    public void initTransation()
    {
        datosSinc.clear();
        for(int row=0;row<getRowCount();row++)
        {
            ArrayList datosCol=new ArrayList();
            for (int col=0;col<getColumnCount();col++)
                datosCol.add(sorter.getValueAt(row, col));
            datosSinc.add(datosCol);
        }
        datosUpd.clear();
        inTransation=true;
    }
    /**
     * Realiza la transacion. (Pasa los valores de datosSinc al grid)
     */
    public void commitTransation() throws InterruptedException, InvocationTargetException
    {
        SwingUtilities.invokeAndWait(new Runnable(){
            @Override
            public void run()
            {
                Point punto;
                Iterator<Point> it = datosUpd.keySet().iterator();
                while (it.hasNext())
                {
                    punto = it.next();
                    datosModelo.setValueAt(datosSinc.get(punto.y).get(punto.x) ,punto.y,punto.x);
                }
                inTransation=false;
            }
        });
    }
    public void cancelTransation()
    {
        inTransation=false;
    }
    public boolean isInTransation()
    {
        return inTransation;
    }
    @Override
    public void setLayout(LayoutManager lm)
    {
        // No hago nada.
    }
    private void jbInit() throws Exception
    {
      if (nCol==0)
          return;
      for (int i = 0; i < nCol; i++)
        cabecera.addElement("" + i);
      crear_modelo_datos();

      addDefaultRow();

      crear_controles_tabla();

      ca = 0;
      ra = 0;
      // Establecemos los formatos de las columnas
      Formato = new String[nCol];
      for (int x = 0; x < nCol; x++)
        Formato[x] = "";

      // Establecemos el array de renders.
      renders = new miCellRender[nCol];
      for (int x = 0; x < nCol; x++)
      {
        renders[x] = new miCellRender(this);
        setRenderer(x, renders[x]);
      }

      //Alineacion izquierda por defecto
      for (int x = 0; x < nCol; x++)
      {
        alinearColumna(x, 0);
      }

//      ordencols = new int[nCol];

    Bajustar.setBounds(new Rectangle(434, 2, 19, 16));
    popMenu.add(copyItem);
      copyItem.add(copyCellItem);
      copyItem.add(copyRowItem);
      copyItem.add(copyColItem);
      copyItem.add(copyAllItem);
      MENU = true;
      super.setLayout(new BorderLayout());
      panelG.setLayout(new BorderLayout());
      this.setBackground(Color.gray);
      this.setBorder(new BevelBorder(BevelBorder.LOWERED));

      panelBusqueda.setBorder(BorderFactory.createLoweredBevelBorder());
      panelBusqueda.setMinimumSize(new Dimension(50, 20));
      panelBusqueda.setPreferredSize(new Dimension(50, 20));

      busquedaE.setOpaque(true);
      busquedaE.setMaxLong(0);
      busquedaE.setForeground(Color.red);
      busquedaE.setBackground(Color.yellow);
      busquedaE.setEnabled(false);
      busquedaE.setMayusc(false);

//      panelBuscar.setBorder(BorderFactory.createLineBorder(Color.black));
      panelBuscar.setMaximumSize(new Dimension(361, 18));
      panelBuscar.setMinimumSize(new Dimension(361, 18));
      panelBuscar.setPreferredSize(new Dimension(361, 18));
      panelBuscar.setSize(160, 20);
      panelBuscar.setLayout(null);
      Titulo.setBounds(new Rectangle(3, 1, 46, 13));
      busquedaE.setBounds(new Rectangle(48, 0, 275, 15));
      panelBusqueda.setSize(10, 20);
      panelBusqueda.setPreferredSize(new Dimension(10, 20));
      panelBusqueda.setMinimumSize(new Dimension(10, 20));
      panelBusqueda.setMaximumSize(new Dimension(10, 20));

      Bajustar.setMargin(new Insets(0,0,0,0));
      Bajustar.setMaximumSize(new Dimension(19, 19));
      Bajustar.setMinimumSize(new Dimension(19, 19));
      Bajustar.setPreferredSize(new Dimension(19, 19));
      Bajustar.setToolTipText("Ajustar grid");

      panelBusqueda.setLayout(gridBagLayout1);
      panelBuscar.add(Titulo, null);
      panelBuscar.add(busquedaE, null);
      panelBusqueda.add(Bajustar,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 2), 0, 0));
      panelBusqueda.add(panelBuscar,    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 3), 0, 0));
      panelG.add(scrPanel,BorderLayout.CENTER);
      this.add(panelG,  BorderLayout.CENTER);
      this.add(panelBusqueda,  BorderLayout.SOUTH);
      tableView.requestFocus();
      tableView.setRowSelectionInterval(0, 0);
      tableView.setColumnSelectionInterval(0, 0);
      this.setVisible(true);
      this.validate();
    }
    
    public void setToolTipHeader(String[] toolTips)
    {
       headerToolTip.setToolTipStrings(toolTips);
    }
    public void setToolTipHeader(String toolTip)
    {
       headerToolTip.setToolTipText(toolTip);
    }
    
    public void setToolTipHeader(ArrayList lista)
    {        
        Object o[]=lista.toArray();
        setToolTipHeader(Arrays.asList(o).toArray(new String[o.length]));
    }
    public void setToolTipHeader(int col, String tooltipText)
    {                
        headerToolTip.setToolTipString(col,tooltipText);
    }
    public String getToolTipHeader(int col)
    {                
        return headerToolTip.getToolTipString(col);
    }

    public void setNumColumnas(int i) throws Exception {
        if (nCol != 0) {
            throw new UnsupportedOperationException("Esta funcion no se puede llamar mas que una vez");
        }
        nCol = i;
        jbInit();
    }

    public void matar() {
    }

    public boolean isMatable() {
        return true;
    }

 /*
    public void setPID(int nuevoPID) {
        PID = nuevoPID;
    }

   public int getPID() {
        return PID;
    }

    public int getPeso() {
        return 10;
    }
*/
    public String getNombre() {
        return nombre;
    }

    public int getMaxCopias() {
        return 2;
    }


    public void setNombre(String nuevoNombre) {
        nombre = nuevoNombre;
        for (int x = 0; x < nCol; x++) {
            renders[x].setNombre(nombre);
        }
    }
    /**
     * Muestra u oculta el panel de Buscar texto en el grid.
     * Por defecto este panel es mostrado.
     * @param b boolean true = Mostrar, false=ocultar
     */

    public void setBuscarVisible(boolean b)
    {
      buscarVisible = b;
      if (!b)
        this.remove(panelBusqueda);
      else
        panelBusqueda.add(panelBuscar,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

      this.validate();
      this.repaint();
    }

    public boolean getBuscarVisible(){
      return buscarVisible;
    }


    public void setAllRenderer(){
       for(int x=0;x<nCol;x++)
         setRenderer(x,renders[x]);
    }
    public void resetAllRenderer(){
      for(int x=0;x<nCol;x++)
         setRenderer(x,null);
    }
    
    public void resetRenderer(int col){
         setRenderer(col,null);
    }

    public void setErrForeColor(Color trueColor){
       for(int x=0;x<nCol;x++)
         renders[x].setErrForeColor(trueColor);
    }

    public void setErrBackColor(Color trueColor){
       for(int x=0;x<nCol;x++)
         renders[x].setErrBackColor(trueColor);
    }
    /**
     * Funciones para obtener y establecer el nnmero de registros a traer mns
     * datos cada vez que se avanza en el grid,por defecto 25
     * Si el numero es 0, significara q cargue de golpe todos los disponibles.
     * @param numeroregistros int
     */
    public void setNumRegCargar(int numeroregistros){
        numRegistros=numeroregistros;
    }

    public int getNumRegCargar(){
        return numRegistros;
    }

    /**
    * Funciones para establecer el punto en el que debe saltar el evento de
    * traer mas datos al grid :ChangeListener de scrPanel.getViewport
    * por defecto 50.
    * El evento se produce cuando la parte inferior del grid coincide con la
    * altura virtual de la tabla menos nuestro punto de scroll
    **/
    public void setPuntoDeScroll(int puntodescroll){
        puntoDeScroll=puntodescroll;
    }

    public int getPuntoDeScroll(){
        return puntoDeScroll;
    }
    /**
     * Carga Todos Los datos de un datosTabla.
     * Para ponerse a editar todos los datos, por ejemplo.
     */
    public void cargaTodo(){
      if(!esDatosTabla) return;
      if (datTabla.isLast())
        return;
      do
      {
        reCargar();
      } while (! datTabla.isLast());
    }
    
    /**
    * Funcion que me almacena mas datos en el grid
    **/
    public void reCargar(){
         cargandoDatos=true;
         ArrayList<ArrayList> v1;   
         if((v1=getNuevosDatos())==null)
         {
            cargandoDatos=false;
            return;
         }
         boolean enab= isEnabled();
         boolean visi = tableView.isVisible();
         setEnabled(false);
         panelG.setVisible(false);
         int numdat=v1.size();
         try{
             for(int i=0;i<numdat;i++)
             {
                addLinea(v1.get(i),true);
             }
         }catch (Exception e){
            cargandoDatos=false;
            return;
         }
         redibujar();
         finRecargar=true;
         panelG.setVisible(visi);
         setEnabled(enab);
         cargandoDatos=false;
    }
    /**
     * Funcion para ir rellenando el grid cuando se produzca
     * el evento que manda cargar mas datos.
     * @return Vector
     */
    public ArrayList<ArrayList> getNuevosDatos()
    {
      if (!esDatosTabla || datTabla.isLast())
        return null;
      try
      {
        ArrayList<ArrayList> dat = new ArrayList();
        int contador = 0;
        int h1=0;
        do
        {
          ArrayList v = new ArrayList();
          for (h1 = 1; h1 <= nCol; h1++)
          {
            // Trunca si es un string
              if (utilSql.getTipo(datTabla.getTipCampo(h1)) ==  Types.DATE)
              {
                if (Formato[h1-1].compareTo("") != 0)
                {
                  if (Formato[h1-1].charAt(0) == 'F')
                    v.add(datTabla.getFecha(h1, "yyyy-MM-dd"));
                  else
                    v.add(datTabla.getFecha(h1, "dd-MM-yyyy"));
                }
                else
                {
                  v.add(datTabla.getFecha(h1, "dd-MM-yyyy"));
                }
              }
//              else  if (utilSql.getTipo(datTabla.getTipCampo(h1)) ==  Types.INTEGER)
//                v.addElement(""+datTabla.getInt(h1,true));
//              else if (utilSql.getTipo(datTabla.getTipCampo(h1)) ==  Types.DECIMAL)
//                v.addElement(""+datTabla.getDouble(h1,true));
              else
                v.add(datTabla.getObject(h1));
          }
          dat.add(v);
          contador++;
        }   while (datTabla.next() && (contador < numRegistros || numRegistros <= 0));
        return dat;
      }  catch (Exception k)
      {
         SystemOut.print(k);
        msgError = "Error:Insertar Datos en Grid. " + k.getMessage();
        removeAllDatos();
        return null;
      }
    }


    public void setCabeceraImp(String[] txt){
        cabeImp = txt;
    }
    public void setNombreImp(String nomb, String desc){
        nombImp = nomb;
        descImp = desc;
    }



    /**
    *  Funcion que me captura la variable de error msgError.
    **/
    public String getMsgError(){
      return ("GRID:"+msgError);
    }
    public miCellRender getRenderer(int colu)
    {
        return renders[colu];
    }
    /**
    * Cambiar el Renderer de una columna
    */
    public void setRenderer(int colu,miCellRender tcr)
    {
       if (colu<0 || colu>=tableView.getColumnCount()){
            msgError="Error(setColorColumna):La columna no existe.";
            return;
        }
        TableColumn col=tableView.getColumn(tableView.getColumnName(colu));
        if (Formato[colu].compareTo("") != 0)
        {
          if (Formato[colu].charAt(0)=='B')
          {
            col.setCellRenderer(null);
            return;
          }
        }
        col.setCellRenderer(tcr);

    }

    /**
    * Funcinn que me activa/desactiva la opcion de ordenar el grid al pulsar sobre
    * la cabecera.
    **/
  /*  public void ordenarGrid(boolean ordenar){
       if(!ordenar)

    }*/

    /**
    * Evita que el usuario cambie columnas de sitio n de tamano
    */
    public void noMoverCabecera(boolean cambiarOrden, boolean cambiarTamano){
      tableView.getTableHeader().setReorderingAllowed(cambiarOrden);
      tableView.getTableHeader().setResizingAllowed(cambiarTamano);
    }

    /**
    * Redefinimos el requestFocus
    */
    @Override
    public void requestFocus(){
      tableView.requestFocus();
    }
    /**
     * Le hace un request Focus a la columna y linea selecionada
     */
    public void requestFocusSelected()
    {
      requestFocus();
      requestFocus(tableView.getSelectedRow(),tableView.getSelectedColumn());
    }
    public void requestFocusSelectedLater()
    {
      requestFocus();
      requestFocusLater(tableView.getSelectedRow(),tableView.getSelectedColumn());
    }
    public void requestFocusInicioLater()
    {
      if (SwingUtilities.isEventDispatchThread())
      {
            requestFocusInicio();
            return;
      }
      javax.swing.SwingUtilities.invokeLater(new Thread()
      {
            @Override
        public void run()
        {
          requestFocusInicio();
        }
      });
    }
    /**
    * Da el foco a la primera celda del grid
    **/
    public void requestFocusInicio(){
      requestFocus();
      requestFocus(0,0);
    }
    public void  requestFocusLater(final int fila,final int columna)
    {
      javax.swing.SwingUtilities.invokeLater(new Thread()
      {
            @Override
        public void run()
        {
          requestFocus(fila,columna);
        }
      });
    }
    /**
     * Realiza un request Focus y ajusta el table view para que se vea la fila y
     * columna mandada
     * @param fila int
     * @param columna int
     */
    public void requestFocus(int fila,int columna)
    {
      try {
        if (fila >= getRowCount())
          fila = getRowCount() - 1;
        if (fila < 0)
          fila = 0;
        tableView.setRowSelectionInterval(fila, fila);
        if (columna >= getColumnCount())
          columna = getColumnCount() - 1;
        if (columna < 0)
          columna = 0;
        tableView.setColumnSelectionInterval(columna, columna);
  //      System.out.println("RowInterval: "+tableView.getSelectedRow());
        ajustar_scroll(fila);
        colSelec = columna;
      } catch (java.lang.IllegalArgumentException k)
      {
//        System.err.println("Error en Cgrid.requestFocus. \n"+
//                           " Fila Or: "+filOr+"Col Or: "+colOr+"\nFila: "+fila+" Columna: "+columna+
//                           " Rows: "+tableView.getRowCount()+ " Columns: "+tableView.getColumnCount());
//        k.printStackTrace();
      }
    }
    /**
    * Función que me vacía (borra) todos los datos del grid.
    **/
    public void removeAllDatos()
    {
        if (SwingUtilities.isEventDispatchThread())
        {
          removeAllDatos_Cgrid(); 
        }
        else
        {
          try {
            SwingUtilities.invokeAndWait(new Runnable()
            {
                    @Override
               public void run()
               {
                   removeAllDatos_Cgrid();
               }
            });
            
          } catch (InterruptedException ex) {
            Logger.getLogger(Cgrid.class.getName()).log(Level.SEVERE, null, ex);
          } catch (InvocationTargetException ex) {
            Logger.getLogger(Cgrid.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
    }
  
    void removeAllDatos_Cgrid(){
      // Mantenemos una fila ficticia siempre que el grid este vacio.
      datosModelo.setNumRows(0);
      addDefaultRow();
      requestFocus(0,0);
      TABLAVACIA=true;
      this.validate();
      if (! isEnabled())
        rowFocus=0;
      else
        columnFocus=0;
    }

    /**
    * Añade la fila inicial por defecto
    **/
    void addDefaultRow(){
      Vector vv= new Vector();

      for(int j=0;j<nCol;j++)
         vv.addElement("");
      datosModelo.addRow(vv);
    }
    /**
    * Funcion que me Filtra cada uno de los valores de los campos que
    * componen el grid (en este caso solo se aplica formato a los
    * nnmeros decimales.
    */
    private Object filtrarGrid(Object dat, int col){
      if(col<0 || col> tableView.getColumnCount()){
            msgError="Error(filtrarGrid):Numero de columnas erroneo";
            return dat;
      }
      if (dat==null)
      {
        if (Formato[col].compareTo("")!=0)
        {
          if (Formato[col].charAt(0)=='B')
            return false;
          else
            return "";
        }
        else
          return "";
      }
      // Busco fechas
      if (dat instanceof java.util.Date)
      { // Me han enviado una fecha
          if (Formato[col].equals(""))
              return Formatear.getFechaVer((java.util.Date) dat);
          else
              return Formatear.formatearFecha((java.util.Date)  dat, Formato[col]);
      }
      if (! Formato[col].equals(""))
      {
        if (Formato[col].charAt(0)=='F')
        {
          if (dat.toString().trim().compareTo("")==0)
            return "";
          Fecha fec = new Fecha(dat.toString(),"yyyy-MM-dd",Formato[col].substring(1));
          String s = fec.getFecha();
          if (fec.getError().compareTo("") != 0)
            s="";
          return s;
        }
       if (Formato[col].charAt(0)=='B')
       {// Tipo Boolean
        if (dat instanceof Boolean)
        {
//          System.out.println("Tipo Boolean");
          return ((Boolean) dat);
        }
        if (Formato[col].charAt(1)=='-')
        {
          try {
              return Integer.parseInt(dat.toString().trim()) != 0;
          } catch (NumberFormatException k)
          {
            return false;
          }
        }
        else
        {
            return dat.toString().equals("" + Formato[col].charAt(1));
        }
       }
        // Formato decimal
       try{
          Double d = Formatear.strToDouble2(""+dat);
          return ((""+Formatear.FormatDecimal(d,Formato[col])).trim());
       }catch(Exception e){
          return dat;
       }
      }
      else
        return (dat);
    }
    /**
     * @deprecated usar setDatos(ArrayList)
     * @param vector
     * @return 
     */
    public boolean setDatos(Vector vector){
       return almacenarDatos(new ArrayList(vector));
    }
    /**
    * Función que me rellena el grid con los datos de un ArrayList en el orden
    * en que se encuentran almacenados.
    **/
    public boolean setDatos(final ArrayList datos){
        res=false;
      
        if (SwingUtilities.isEventDispatchThread())
        {
           res=almacenarDatos(datos); 
        }
        else
        {
          try {
            SwingUtilities.invokeAndWait(new Runnable()
            {
                    @Override
               public void run()
               {
                   res=almacenarDatos(datos);
               }
            });
            
          } catch (InterruptedException ex) {
            Logger.getLogger(Cgrid.class.getName()).log(Level.SEVERE, null, ex);
            return false;
          } catch (InvocationTargetException ex) {
            Logger.getLogger(Cgrid.class.getName()).log(Level.SEVERE, null, ex);
            return false;
          }
        }
        return res;
    }
    /**
    * Funcion que me rellena el grid con los datos de un Vector en el orden
    * en que se encuentran almacenados.
    * @deprecated Usar setDatos en su lugar
    */
    public boolean almacenarDatos(ArrayList<ArrayList> dt){
          boolean enab=isEnabled();
          setEnabled(false);
          gridActivo=true;
          esDatosTabla=false;
          cargandoDatos=true;
          if (dt.isEmpty()){
            msgError="(almacenarDatos) ArrayList de ArrayList sin Elementos MENORES";
            setEnabled(enab);
            cargandoDatos=false;
            return false;
          }
          //	Llenar el Grid con los datos de Base de Datos.
          if ((dt.get(0)).size()<0  ||
                      (dt.get(0)).size()>tableView.getColumnCount()){
              msgError="Error(almacenarDatos):Número de columnas erroneo.";
              setEnabled(enab);
              cargandoDatos=false;
              return false;
          }
          removeAllDatos();
          if (dt.isEmpty()){
             setEnabled(enab);
             cargandoDatos=false;
             return true;
          }

         ////Intento de arreglo (creo que gana algo de velocidad)
         int tamano=dt.size();
         for(int h1=0;h1<tamano;h1++){
               if(dt.get(h1)==null){
                  dt.set(h1,new ArrayList());
               }
               addLinea(dt.get(h1));
          }
        
          TABLAVACIA=false;
          cargandoDatos=false;
          setAllRenderer();
          requestFocus(0,0);
          ra=0;
          ca=0;
          setEnabled(enab);
          return true;
    }
   /**
    * Funcion que me rellena el grid con los datos de un Vector en el orden
    * en que se encuentran almacenados.
    */
    public void iniciaCargaDatos()
    {
      gridActivo=true;
      esDatosTabla=false;
      removeAllDatos();
      requestFocus(0,0);
      reCargar();
    }

    /**
    * Función que me devuelve el valor de la fila seleccionada,columna
    * especificada en forma de String.
    **/
    public String getValString(int col){
      ra=getSelectedRow();
      if(ra<0 || ra >=tableView.getRowCount()){
          msgError="Error(getValString):Numero de fila ("+ra+") erroneo.";
          return null;
      }
      if (col<0 || col>= tableView.getColumnCount()){
          msgError="Error(getValString):Numero de columna ("+col+") erroneo.";
          return null;
      }
      Object o=getValor(col);
      if (o==null)
        return null;
      return o.toString();
    }
    /**
    * Funcion que me devuelve el valor de la fila seleccionada,columna
    * especificada en forma de String.
    **/
    public String getValString(int fil,int col)
    {
      if(fil<0 || fil >=tableView.getRowCount()){
          msgError="Error(getValString):Numero de fila erroneo.";
          return null;
      }
      if (col<0 || col>= tableView.getColumnCount()){
          msgError="Error(getValString):Numero de columna erroneo.";
          return null;
      }
      Object o=getValor(fil,col);
      if (o==null)
        return null;
      return o.toString();
    }
    /**
     * Comprueba q los valores de Columna y Fila (row) son correctos.
     * @param col int
     * @param row int
     * @return boolean true si son correctos
     */
    public boolean chkColRow(int col, int row)
    {
      if (row < 0 || row >= tableView.getRowCount())
      {
        msgError = "Error: Numero de fila erroneo";
        return false;
      }
      if (col < 0 || col >= tableView.getColumnCount())
      {
        msgError = "Error :Numero de columna erroneo";
        return false;
      }
      return true;
    }
    /**
     * Devuelve como un date el valor de la columna selecionada (activa)  y la columna mandada
     * @param col
     * @return
     * @throws java.text.ParseException 
     */
    public java.util.Date getValDate(int col) throws java.text.ParseException
    {
      return getValDate(getSelectedRow(),col);
    }
    public java.util.Date getValDate(int fil, int col) throws java.text.ParseException
    {
      return getValDate(fil,col,getFormatoColumna(col));
    }
    /**
     * Devuelve el valor de una columna en formato DATE
     * Si la columna esta vacia devuelve Null.
     * @param fil int
     * @param col int
     * @param formFec String
     * @return Date
     * @throws java.text.ParseException
     */
    public java.util.Date getValDate(int fil, int col,String formFec) throws java.text.ParseException
    {
      if (!chkColRow(col, fil))
        return null;

      Object o = getValor(fil, col);
      if (o == null)
        return null;
      String fecha=o.toString().trim();      
      if (Formatear.isNullDate(fecha))
          return null;
      return Formatear.getDate(fecha,formFec);

    }

    /**
     * Funcion que me devuelve el valor de la fila seleccionada,columna
     * especificada(nombre) en forma de String.
     **/
    public String getValString(String col)
    {
      int pos = -1;
      pos = buscarPosicionColumna(col);
      if (pos == -1)
      {
        msgError = "Error(getValString):Nombre de columna no encontrada.";
        return null;
      }
      ra = getSelectedRow();
      if (ra < 0 || ra >= tableView.getRowCount())
      {
        msgError = "Error(getValString):Numero de fila erroneo.";
        return null;
      }
      Object o = getValor(col);
      if (o == null)
        return null;
      return o.toString();
    }
    /**
    * Funcinn que me devuelve el valor de la fila especificada,columna
    * especificada(nombre) en forma de String.
    **/
    public String getValString(int fil,String col){
      int pos=-1;
      pos=buscarPosicionColumna(col);
      if (pos==-1) {
            msgError="Error(getValString):Nombre de columna no encontrada.";
            return null;
      }
      if(fil<0 || fil >=tableView.getRowCount()){
          msgError="Error(getValor):Numero de fila erroneo.";
          return null;
      }
      Object o=getValor(fil,col);
      if (o==null)
        return null;
      return o.toString();
    }

    public String getValString(int fila,boolean hacertrim){
         if (hacertrim)
           return getValString(fila).trim();
         else
           return getValString(fila);
    }

    public String getValString(int fila,int columna,boolean hacertrim){
         if(hacertrim)
           return getValString(fila,columna)==null?"":getValString(fila,columna).trim();
         else
           return getValString(fila,columna);

    }

    public String getValString(String nomcol,boolean hacertrim){
         if(hacertrim) return getValString(nomcol).trim();
         else return getValString(nomcol);
    }

    public String getValString(int fila,String nomcol,boolean hacertrim){
         if(hacertrim) return getValString(fila,nomcol).trim();
         else return getValString(fila,nomcol);
    }
    /**
     * funcion que devuelve el valor de la columna activa como boolean
     */
    public boolean getValBoolean(int col) {
      return getValBoolean(getSelectedRow(), col);
    }
    /**
     * funcion que devuelve el valor de una fila/columna como boolean
     */
    public boolean getValBoolean(int fila, int col) {
      if (getValString(fila, col)==null)
        return false;
      if (getValString(fila, col).equals("true"))
        return true;
      else
        return false;
    }
    /**
     * @deprecated usar getValorInt(int col)
    * Funcion que me devuelve el valor de la fila seleccionada,columna
    * especificada en forma de entero.
    **/
    public int getValInt(int col){
      return getValorInt(getSelectedRow(),col);
    }
    public int getValorInt(int col){
      return getValorInt(getSelectedRow(),col);
    }
    /**
     * @deprecated Usar getValorInt(fila, columna)
    * Funcion que me devuelve el valor de la fila especificada,columna
    * especificada en forma de entero.
    **/

    public int getValInt(int fil,int col){
      return getValorInt(fil,col);
    }
    /**
    * Funcion que me devuelve el valor de la fila especificada,columna
    * especificada en forma de entero.
    **/
    public int getValorInt(int fil,int col){
      Object o=getValor(fil,col);
      if (o==null)
        return 0;
      String val=o.toString().trim();
      if (val.equals(""))
        return 0;
      try {
        return Integer.parseInt(val);
      } catch (Exception k){return 0;}

    }
    public int getValorInt(String col){
      return getValorInt(getSelectedRow(),buscarPosicionColumna(col));
    }

    public int getValorInt(int fil,String col){
      return getValorInt(fil,buscarPosicionColumna(col));
    }

    /**
    * Funcion para recoger valores de columnas ocultas
    **/
    public Object getValOculto(int fila,int columna){
            return datosModelo.getValueAt(fila,columna);
    }

    /**
     * Devuelve en un arrayList todas los valores en una columna
     * @param col
     * @return arrayList con los valores en una columna del grid
     */
    public ArrayList getValorColumna(int col) throws NumberFormatException
    {
        if (col<0 || col>= tableView.getColumnCount()){
          msgError="Error(getValor):Numero de columna erroneo.";
          return null;
        }
        ArrayList lista=new ArrayList();
        int nRow=getRowCount();
        for (int n=0;n<nRow;n++)
            lista.add(getValor(n,col));
        return lista;
    }
     /**
     * Devuelve el valor existente en la linea (row) selecionada
     * @param col Columna 
     * @return  Valor. Null si la columna estan fuera de rango.
     */
    public Object getValor(int col){
        return getValor(getSelectedRow(),col);
    }
     /**
     * Devuelve el valor existente de una posicion en el grid
     * @param fil Fila (row)
     * @param col Columna 
     * @return  Valor. Null si la fila o o la columan estan fuera de rango.
     */
    public Object getValor(int row,int col){
        if (row<0 || row>= tableView.getRowCount()){
          msgError="Error(getValor):Numero de fila erroneo.";
          return null;
        }
        if (col<0 || col>= tableView.getColumnCount()){
          msgError="Error(getValor):Numero de columna erroneo.";
          return null;
        }
        if (isInTransation())
            return datosSinc.get(row).get(col);
        else
            return sorter.getValueAt(row,col);
    }
    /**
     * Devuelve de la linea actual el valor especificado de la columna especificado
     * por su nombre de cabecera.
     * @param col
     * @return 
     */
    public Object getValor(String col){
        return getValor(getSelectedRow(),col);
    }

    // Fila especificada y columna especificada mediante su nombre de cabecera.
    public Object getValor(int row,String col){
        if (row<0 || row>= tableView.getRowCount()){
          msgError="Error(getValor):Numero de fila erroneo.";
          return null;
        }
        int pos=-1;
        pos=buscarPosicionColumna(col);
        if (pos==-1) {
            msgError="Error(cogerCelda):Columna no encontrada.";
            return null;
        }
        if (isInTransation())
            return datosSinc.get(row).get(pos);
        else
            return sorter.getValueAt(row,pos);
    }

    /**
    * Función que me devuelve true si el grid esta vacio false en caso contrario.
    **/
    public boolean isVacio(){
       return TABLAVACIA;
    }



    /**
    * Funcinn que me devuelve el valor de la fila seleccionada,columna
    * especificada en forma de double.
    **/
    public double getValorDec(int col){
       return getValorDec(getSelectedRow(),col);
    }

    /**
    * Función que me devuelve el valor de la fila especificada,columna
    * especificada en forma de double.
    **/
    public double getValorDec(int fil,int col){
        if (fil<0 || fil>= tableView.getRowCount()){
          msgError="Error(getValor):Numero de fila erroneo.";
          return 0d;
        }
        if (col<0 || col>= tableView.getColumnCount()){
          msgError="Error(getValor):Numero de columna erroneo.";
          return 0d;
        }
        Object o = getValor(fil, col);
        if (o == null)
          return 0;
        String cad=o.toString().trim();
        if (cad.equals(""))
          return 0;
        cad=cad.replaceAll(",","");
        cad=cad.replaceAll(" ","");
        try {
            return Double.parseDouble(cad);
        } catch (NumberFormatException x)
        {
            return 0;
        }
    }

    /**
    * Función que me devuelve el valor de la fila seleccionada,columna
    * especificada(nombre) en forma de double.
    **/
    public double getValorDec(String col){
      return getValorDec(getSelectedRow(),buscarPosicionColumna(col));

    }

    /**
    * Funcinn que me devuelve el valor de la fila especificada,columna
    * especificada(nombre) en forma de double.
    **/
    public double getValorDec(int fil,String col){
      return getValorDec(fil,buscarPosicionColumna(col));
    }


  /**
  * Pone la variable global ValorDec con el valor numerico de lo que haya en el campo
  * Si no puede traducirlo a decimal devuelve 0.
  **/
    private boolean ponValDec(String Text){
    String Tex1="";
    for (int n=0;n< Text.length();n++)
    {
        if (Character.isDigit(Text.charAt(n))==true || Text.charAt(n)==Formatear.decimalSeparator.charAt(0) || Text.charAt(n)=='-')
                    Tex1=Tex1+Text.charAt(n);
    }
        try {
            if (Tex1==null || Tex1.length() == 0)
                ValorDec=new Double("0");
            else
                ValorDec= new Double(Tex1);
     } catch (NumberFormatException k)
     {
        msgError="ponValDec: Error al transformar Cadena a Numero";
        return false;
       }
     return true;
  }

// --- FIN


  /**
   * Funcion que hace que la columna dada no se vea
   *
   * @param col Columna a Ocultar.
   * No Usar si se esta trabajando sobre un grideditable. Hace
   * que este NO funcione
   * @see CGridEditable
   **/

    public void setVisibleColumna(String columna){
        int pos=-1;
        for(int i=0;i<tableView.getColumnCount();i++)
           if (tableView.getColumnName(i).compareTo(columna)==0) {
               pos=i;
               break;
           }
        if (pos==-1) {
            msgError="Error(removeColumna):Nombre de columna no encontrada.";
            return;
        }
        setVisibleColumna(pos);
    }

    /**
    * Funcion que hace que la columna dada no se vea
    *
    * @param col Columna a Ocultar.
    * No Usar si se esta trabajando sobre un grideditable
    * @see CGridEditable
    **/
    public void setVisibleColumna(int col){
        if (col<0 || col>=tableView.getColumnCount())
               msgError="Error(borrarColumna): No existe la columna";
        else{
           tableView.removeColumn(tableView.getColumn(tableView.getColumnName(col)));
        }
    }
    public void setLanzaCargaDatosThread(boolean sino) { swLanzaCargaDatosThread = sino; }
 /**
    * Función que me establece los nombres de los campos de cabecera.
    * @deprecated usar setCabecera(ArrayList v)
    */
    public boolean setCabecera(Vector cabs){
        return setCabecera(new ArrayList(cabs));
    }
    /**
    * Establece los valores de la cabecera
    * @param cabs ArayList con strings. Uno para cada campo. 
    * No se permite que haya dos campos con el mismo nombre.
    * @return true si todo va bien. False en caso de error (@see getMsgError())
    */
    public boolean setCabecera(ArrayList<String> cabs){
      for(int x1=0;x1<cabs.size();x1++)
      {
         for(int x2=x1+1;x2<cabs.size();x2++)
         {
             if(cabs.get(x1).toString().compareTo(cabs.get(x2))==0)
             {
                 msgError="Error(setCabecera): Identificador de cabecera duplicado";
                 return false;
             }
         }
       }
       if(cabs.size()==tableView.getColumnCount())
       {
              datosModelo.setColumnIdentifiers(cabs.toArray());
              return true;
       }
       msgError="Error(setCabecera): Número de elementos en ArrayList no se corresponde con número de columnas";
       return false;
      
    }

    /**
     * Función que me rellena el grid con los datos de un DatosTabla en el orden
     * en que se encuentran almacenados.
     * Es ejecutado en un Thread de Swing, si detecta q no esta en el.
     * 
     **/
    public boolean setDatos(DatosTabla datostabla)
    {
      return setDatos(datostabla, true);
    }

    /**
     * Función que me rellena el grid con los datos de un DatosTabla en el orden
     * en que se encuentran almacenados.
     */
    public boolean setDatos(final DatosTabla dt, final boolean borDatos)
    {
        res=false;
        finRecargar=false;
        if (SwingUtilities.isEventDispatchThread())
        {
           res=setDatosInt(dt,borDatos); 
        }
        else
        {
          try {
            SwingUtilities.invokeAndWait(new Runnable()
            {
               @Override
               public void run()
               {
                   res=setDatosInt(dt,borDatos);
               }
            });
            
          } catch (InterruptedException ex) {
            Logger.getLogger(Cgrid.class.getName()).log(Level.SEVERE, null, ex);
            return false;
          } catch (InvocationTargetException ex) {
            Logger.getLogger(Cgrid.class.getName()).log(Level.SEVERE, null, ex);
            return false;
          }
        }
        return res;
    }
    boolean setDatosInt( DatosTabla dt,  boolean borDatos)
    {
     //	Llenar el Grid con los datos de Base de Datos.
      boolean enab = isEnabled();
      setEnabled(false);
      gridActivo = true;
      esDatosTabla = true;
      datTabla = dt;
      int cols = tableView.getColumnCount();
      if (cols <= 0)
      {
        msgError = "Error(setDatos):Numero de columnas erroneo.";
        setEnabled(enab);
        return false;
      }
      if (borDatos)
        removeAllDatos();
      if (dt.getNOREG())
      {
        setEnabled(enab);
        return true;
      }

      reCargar();

      setAllRenderer();
      requestFocus(0, 0);
      ra = 0;
      ca = 0;
      setEnabled(enab);
      return true;
    }
    public void addLinea(Vector v)
    {
       addLinea(new ArrayList(v));
    }
    public void addLinea(ArrayList val) 
    {
        addLinea(val,false);
    }
    
     public void addLinea(ArrayList val,int nLin) throws IllegalArgumentException
     {
         addLinea(val,nLin,false);
     }
    /**
      * Añade nuevas lineas al grid.
      * Parecido a setdatos solo que este no borra los datos antiguos.
      * @param datos 
      */
     public void addLineas(ArrayList<ArrayList> datos)
     {
         for (int n=0;n<datos.size();n++)
         {
             addLinea(datos.get(n));
         }
     }
    /**
     * Inserta una Linea en el Grid
     * @param val Vector Vector con los datos a insertar
     * @throws IllegalArgumentException problema con los datos
     */
    public void addLinea(ArrayList val,boolean swDatTabl) throws IllegalArgumentException
    {
      if (!swDatTabl)
          esDatosTabla=false;
      int tam = tableView.getColumnCount();
      if (val.size() != tam)
      {
        msgError = "Error (addLinea): número de columnas " + val.size() + " erroneo. Deberian ser:" +
            tam;
        throw new IllegalArgumentException(msgError);
      }

      if (TABLAVACIA)
      {
        datosModelo.removeRow(0);
        TABLAVACIA = false;
      }
      Vector w = new Vector();
      for (int k = 0; k < tam; k++)
      {
        w.addElement(filtrarGrid(val.get(k), k));
      }
      datosModelo.addRow(w);
      requestFocus(tableView.getRowCount() - 1,0);
    }
    /**
    * Función para eliminar la fila especificada del grid.
    */
     public boolean removeLinea(int fila){
       if(fila==0)
       {
          if (tableView.getRowCount()==1)
            removeAllDatos();
          else{
            datosModelo.removeRow(fila);
            tableView.setRowSelectionInterval(0,0);
            ajustar_scroll(0);
          }
       }
       else{
          datosModelo.removeRow(fila);
          tableView.setRowSelectionInterval(fila-1,fila-1);
          ajustar_scroll(fila-1);
       }

       if(getRowCount()<numRegistros)
       reCargar();
       return true;
     }

    /**
    * Funcion para eliminar la fila seleccionada del grid.
     * @return 
    */
     public boolean removeLinea(){
      if(tableView.getSelectedRow()==-1){
          msgError="Error: No hay fila seleccionada";
          return false;
      }
      int fselec=tableView.getSelectedRow();
      if (!removeLinea(fselec)) return false;
      else{
        if(/*esDatosTabla &&*/ (getRowCount()<numRegistros)) reCargar();
        return true;
      }
     }
     /**
      * Ajusta el grid al tama�o del Frame que lo contiene.
      * Tambien ajusta el tama�o de las columnas al tamaño del grid para que se vean
      * todas.
      * @see setAjustarColumnas
      * @param b boolean true Ajustar. False NO ajustar
      *
      */
     public void setAjustarGrid(boolean b) {
            swAjustarGrid = b;
            if (b)
               AJUSTAR = b;
     }
     
     /**
      * Pone el panel Principal cuando se cambia el tamaño del padre que lo
      * contiene
      */
    public synchronized void ponPanel()
    {
      swPonPanel = true;
      if (swAjustarGrid) {
         if (panelG.getSize().width<=0 || panelG.getSize().height<=0) {
            swPonPanel = false;
//            Thread.currentThread().setPriority(pri);
            return;
         }
         try {
             panelG.remove(scrPanel);
         } catch (Exception j) {}
         scrPanel.setBounds(new Rectangle(0,0,panelG.getSize().width,panelG.getSize().height));
         panelG.add(scrPanel,null);
         cuadrar();
         redibujar();
      }
      else
          if (! swAdd) {
       if (panelG.getSize().width<=0 || panelG.getSize().height<=0) {
          swPonPanel = false;
          return;
       }
       swAdd=true;
       scrPanel.setBounds(new Rectangle(0,0,panelG.getSize().width,panelG.getSize().height));
       panelG.add(scrPanel,null);
       redibujar();
      }
      anchopanelOld = this.getWidth()-9;
      anchopanelOld -= scrPanel.getVerticalScrollBar().getPreferredSize().width;
      anchopanelOld -= tableView.getIntercellSpacing().width * (getColumnCount()-1);
      swPonPanel = false;
    }
    /**
    *  Funcion de refresco del grid.
    **/
    public void redibujar()
    {
      tableView.validate();
      tableView.repaint();
    }
   void redibuja1()
    {
      try {
        this.validate();
        this.repaint();
      } catch (NullPointerException k){}
    }


    public boolean setText(String val,int row,int col){
       return setValor(val,row,col); 
    }


    public boolean setValor(Object valor){
          return cambiarValor(valor);
    }
    public boolean setValor(Object valor,int columna){
          return cambiarValor(valor,columna);
    }
    public boolean setValor(Object valor,int fila,int columna){
          return cambiarValor(valor,fila,columna);
    }
    public boolean setValor(Object valor,String nomcol)
    {
      return setValor(valor,getSelectedRow(),nomcol);
    }

    public boolean setValor(Object valor,int fila,String nomcol){
          int pos=buscarPosicionColumna(nomcol);
          if(pos==-1){
            msgError="Error(setValor):Columna no encontrada.";
            return false;
          }
          return setValor(valor,fila,pos);
    }



    public int getSelectedColD(){
         return datosModelo.findColumn(tableView.getColumnName(getSelectedColumn()));
    }
    /**
     * Devuelve el Nombre de la Columna Selecionada
     */
    public String getNameColumn()
    {
        return getNameColumn(getSelectedColumn());
    }

    public void setSelectColumnDis()
    {
      setSelectColumnDis(getSelectedColumnDisab());
    }
    /**
     * Establece la Columna DISABLED al parametro mandado
     * @param col int No Col a establececer
     */
    public void setSelectColumnDis(int col)
    {
      columnFocus=col;
    }
    /**
     * Pone la Linea activa de Disabled a la actual-
     */
    public void setSelectRowDis()
  {
    setSelectRowDis(getSelectedRowDisab());
  }
  public void setSelectRowDis(int row)
  {
    rowFocus=row;
  }

    /**
     * Devuelve el Nombre de una columna dada.
     * Tengase en cuenta que si se han movido las columnas con el raton el numero sera
     * el visible.
     */
    public String getNameColumn(int numcol)
    {
        return tableView.getColumnName(numcol);
    }

    /**
    * Cambiar valor de fila y columna seleccionadas.
    * @despreciado Usar setValor
    **/
    private boolean cambiarValor(Object val)
    {
      return cambiarValor(filtrarGrid(val,getSelectedColD()),getSelectedRow(),getSelectedColD());
    }
    /**
    * Cambiar valor de columna especificada de fila seleccionada.
    * @despreciado usar setValor
    **/
    private boolean cambiarValor(Object val, int col){
      return cambiarValor(val,getSelectedRow(),col);
    }
    /**
    * Cambiar valor de fila y columna especificadas.
    * @despreciado Usar setValor
    */
    private boolean cambiarValor(Object val,int row,int col){
       if (row<0 || row>= tableView.getRowCount()){
          msgError="Error(cambiarValor):Numero de fila erroneo.";
          return false;
       }
       if (col<0 || col>= tableView.getColumnCount()){
          msgError="Error(cambiarValor):Numero de columna erroneo.";
          return false;
       }
       TABLAVACIA=false;
       if (isInTransation())
       {
           ArrayList linea=datosSinc.get(row);
           linea.set(col, filtrarGrid(val,col));
           datosSinc.set(row, linea);
           datosUpd.put(new Point(col,row), true);
       }   
       else
          datosModelo.setValueAt(filtrarGrid(val,col),row,col);
       return true;
    }
    /**
     * Pone al grid los valores de los campos editables
     * @param vector Vector el valor de los campos editables
     */
    public void setLinea(ArrayList vector){
       cambiarLinea(vector);
    }
    /**
     * Pone al grid los valores de los campos editables
     * @param arrList  ArrayList Con el valor de los campos editables
     * @param fila
     * @return Parametros incorrectos
     */
    public boolean setLinea(ArrayList arrList,int fila){
       return cambiarLinea(arrList,fila);
    }
    public void setEntornoUsuario(EntornoUsuario entUsuario){
    entUsu = entUsuario;
//    impAllItem.setEnabled((entUsu != null));
  }

    /**
    * Cambiar valores de fila seleccionada.
    * @despreciado Usar setLinea
    **/
    public boolean cambiarLinea(ArrayList val){
       return cambiarLinea(val,getSelectedRow());
    }
    /**
    * Cambiar valores de fila especificada.
    * @param val Valores a poner en la linea
    * @param rowActivo
    * @despreciado Usar setLinea
    **/
    public boolean cambiarLinea(ArrayList val, int rowActivo)
    {
       if(getSelectedRow()==-1)
        return false;
       if (rowActivo<0 || rowActivo>= tableView.getRowCount()){
          msgError="Error(cambiarValor):Número de fila erroneo.";
          return false;
       }
       if (val.size()!=tableView.getColumnCount())
       {
          msgError="Error(cambiarLinea):número de columnas erroneo";
          return false;
       }
     
       for(int j=0;j<val.size();j++)
       {
           if (isInTransation())
           {
               ArrayList linea=datosSinc.get(rowActivo);
               linea.set(j, filtrarGrid(val,j));
               datosSinc.set(rowActivo, linea);
               datosUpd.put(new Point(j,rowActivo), true);
           }   
           else
             datosModelo.setValueAt(filtrarGrid(val.get(j),j),rowActivo,j);
       }
       TABLAVACIA=false;
       return true;
     }
    public void setEnabledLater(final boolean b)
    {
      if (SwingUtilities.isEventDispatchThread())
      {
            setEnabled(b);
            return;
      }
      javax.swing.SwingUtilities.invokeLater(new Thread()
      {
            @Override
        public void run()
        {
          setEnabled(b);
        }
      });
    }
    @Override
    public void setEnabled(boolean b)
    {
      if (swOrden)
        if (isEnabled() != b)
//          if (b)
//            sorter.addMouseListenerToHeaderInTable(tableView);
//          else
//            sorter.removeMouseListenerToHeaderInTable();
      panelBusqueda.setEnabled(b);

      if (!b)
      {
        if (activo)
        {
          rowFocus = tableView.getSelectedRow();
  //          System.out.println("activo : "+ activo+"rowFocus"+rowFocus);
          if (rowFocus == -1)
            rowFocus = 0;
          columnFocus = tableView.getSelectedColumn();
          if (columnFocus == -1)
            columnFocus = 0;
          viewPosition = scrPanel.getViewport().getViewPosition();
        }
      }
      else
      {
        if (b && activoPanel)
        {
          if (!isEnabled())
          {
  //            System.out.println("activo : "+ activo+"rowFocus"+rowFocus);
            requestFocus(rowFocus, columnFocus);
            scrPanel.getViewport().setViewPosition(viewPosition);
          }
        }
      }
      activo = b;
    }

    public boolean isEnabled()
    {
      if (!activoPanel)
        return false;
      return activo;
    }

    /**
     * Pone la linea que devolvera la funcion getSelectedRow cuando el grid
     * no este activo.
     *
     * @param rwFocus
     */
    public void setRowFocus(int rwFocus)
    {
      rowFocus=rwFocus;
    }
    public void setEnabledParent(boolean enabled)
    {
      if (!enabled) {
         rowFocus = getSelectedRow();
         columnFocus = getSelectedColumn();
      } else
          tableView.setRowSelectionInterval(rowFocus, rowFocus);

      activoPanel=enabled;
//        Component[] lista = this.getComponents();
        int n = this.getComponentCount();
    }
    /**
    * Funcion para insertar una nueva linea debajo de la linea seleccionada.
    */
    public void insertaLinea(ArrayList v) throws IllegalArgumentException
    {
      addLinea(v, tableView.getSelectedRow()+1,false);
    }
    /**
     * Inserta una Linea en la posicion mandada
     * @param v Vector Datos para la linea a insertar
     * @param pos int Posicion
     * @param swDatTabla Indica si esta siendo cargado por un datostabla
     * @throws IllegalArgumentException
     */
    public void addLinea(ArrayList v,int pos,boolean swDatTabl)  throws IllegalArgumentException
    {
       if (! swDatTabl)
           esDatosTabla=false;
       if (v.size()!= tableView.getColumnCount())
       {
          msgError="Error(addLinea):Numero de columnas erroneo";
          throw new IllegalArgumentException(msgError);
       }

       if (pos<0)
       {
          msgError="Error(insertar_linea):Numero de posicion erroneo";
          throw new IllegalArgumentException(msgError);
       }
       if (pos>tableView.getRowCount())
       {
         addLinea(v,swDatTabl);
         return;
       }
       Vector aux=new Vector();
       for (int h=0;h<v.size();h++)
            aux.addElement(filtrarGrid(v.get(h),h));
       datosModelo.insertRow(pos,aux);
       requestFocus(pos, 0);
       ajustar_scroll(pos);
    }

    /**
    * Crear la estructura de datos para la tabla.
    */
    private void crear_modelo_datos(){
      datosModelo = new miDefaultTableModel(nCol,this);

      datosModelo.setDataVector(datos,cabecera);
    }

 /**
  * Establece el punto del tableVeiw que debe ser visto
  * @param pos int Posición
  */
 public void ajustar_scroll(int pos) {
       tableView.scrollRectToVisible(tableView.getCellRect(pos, getSelectedColumn(),true));
       }



    /**
    * @despreciado Usar setAnchoColumna
    **/
    public void ancho_columna(int[] anchuras)
    {
      setAnchoColumna(anchuras);
    }

    /**
    * @despreciado Usar setAnchoColumna
    **/
    public void ancho_columna(int columna,int ancho)
    {
      setAnchoColumna(columna,ancho);
    }

    /**
    * Ajusta el ancho de la columna al valor indicado.
    * @param anchuras ancho de todas las columnas del grid en pixels.
    **/
   public void setAnchoColumna(int[] anchuras)
   {
     if (anchuras.length > tableView.getColumnCount())
     {
       System.err.println("(CGRID) Numero de Columnas " +
                                      anchuras.length +
                                      " ... Erroneo");
       throw new NullPointerException("(CGRID) Numero de Columnas " +
                                      anchuras.length +
                                      " ... Erroneo. Deberia ser : "+tableView.getColumnCount());
     }
     for (int i = 0; i < anchuras.length; i++)
     {
       if (i > getColumnCount())
         break;
       if ( (anchuras[i] > 0))
       {
         TableColumn col = tableView.getColumn(tableView.getColumnName(i));
         //			              col.setWidth(anchuras[i]);
         col.setPreferredWidth(anchuras[i]);
         if (swAsignaMinWith)
           col.setMinWidth(anchuras[i]);
       }
       else
       {
         msgError = " Tamano >0  !!!!!!";
         Logger.getLogger(Cgrid.class.getName()).log(Level.SEVERE, msgError);
         break;
       }
     }
     redibujar();
   }
    public void setAsignaMinWith(boolean asigna) {
           swAsignaMinWith = asigna;
    }
    public boolean getAsignaMinWith() {
           return swAsignaMinWith;
    }
    /**
    * Ajusta el ancho de la columna al valor indicado.
    * @param columna numero de la columna a ajustar.
    * @param ancho ancho de la columna en pixels.
    **/
    public void setAnchoColumna(int columna,int ancho){
        if (columna>=0 && columna<tableView.getColumnCount() )
        {
            if (ancho>0)
            {
                   TableColumn col=tableView.getColumn(tableView.getColumnName(columna));
               col.setPreferredWidth(ancho);
               return;
            }
            else
            {
               msgError="Ancho ("+ancho+") de columna:"+columna+" no valido";
            }
        }
       else
           msgError=" Columna: "+columna+"  no existe";
       Logger.getLogger(Cgrid.class.getName()).log(Level.SEVERE, msgError);

    }

    public void setAnchoColumna(String nomcol,int ancho){
        int pos=buscarPosicionColumna(nomcol);
        if (pos==-1) {
            msgError="Error(cogerCelda):Columna no encontrada.";
            return;
        }
        setAnchoColumna(pos,ancho);
    }
    public int getAnchoColumna(int columna)
    {
        if ((columna>=0)&&(columna<tableView.getColumnCount()))
      {
        TableColumn col=tableView.getColumn(tableView.getColumnName(columna));
        return col.getWidth();
      }
      else
         msgError=" Columna no existe !!!!!!";
      return -1;
    }
    /**
    * Funcinn que me busca la posicinn de una columna
    **/
    public int buscarPosicionColumna(String nomcol){
       int pos=-1;
        /*for(int i=0;i<tableView.getColumnCount();i++)
           if (tableView.getColumnName(i).compareTo(nomcol)==0) {
               pos=i;
               break;
           }
        return pos;*/
        return datosModelo.findColumn(nomcol);
    }
    /**
     * Poner unas orejas  de tipo Raton ;) al tableview
     * @param v 
     */
    @Override
    public void addMouseListener(MouseListener v)
    {
      super.addMouseListener(v);
      tableView.addMouseListener(v);
    }
    public void addListSelectionListener(ListSelectionListener e) {
           tableView.getSelectionModel().addListSelectionListener(e);
    }


    /**
    * Funcion para asignar un tipo de datos a una columna.Le pasaremos un DefaultCellEditor
    * de una clase JComboBox,JCheckBox,JTextField o cualquier otra que se extienda de ellas.
    * @param edit tipo de editor
    * @param col columna elegida (empieza en 0)
    */
    public void activarTipoColumna(DefaultCellEditor edit,int col){
      TableColumn posic=tableView.getColumn(tableView.getColumnName(col));
        posic.setCellEditor(edit);
    }

    public void setImprimible(String cabecera,EntornoUsuario eu)
    {
      descrCabecReport=cabecera;
      entUsu=eu;
    }
    void procesaMouse(MouseEvent m) {
        boolean salir = false;
        if (CONFIGURAR) {
            configItem.setEnabled(true);
            resetItem.setEnabled(true);
        }
        bajaItem.setEnabled(isEnabled());
        editItem.setEnabled(isEnabled());
        addItem.setEnabled(isEnabled());

        if (!enabled) {
            salir = true;
        }

        if (!salir && isEnabled()) {
            if (m.getClickCount() > 1 && m.getModifiers() == m.BUTTON1_MASK) {
                if (TABLAVACIA) {
                    return;
                }
            }
            if ((m.getModifiers() != m.BUTTON1_MASK) && (activoMenu)) {
                if (TABLAVACIA) {
                    bajaItem.setEnabled(false);
                    editItem.setEnabled(false);
                } else {
                    bajaItem.setEnabled(true);
                    editItem.setEnabled(true);
                }
            }
        }// Se configura el popMenu

        if (m.getModifiers() != m.BUTTON1_MASK) {
            if (CONFIGURAR || MENU) {
                popMenu.show(tableView, m.getX(), m.getY());

                int y = m.getY();
                for (rowCopy = 0; rowCopy < getRowCount(); rowCopy++) {
                    Rectangle rec = tableView.getCellRect(rowCopy, 0, true);
                    y -= rec.getHeight();
                    if (y <= 0) {
                        break;
                    }
                }
                int x = m.getX();
                for (colCopy = 0; colCopy < getColumnCount(); colCopy++) {
                    Rectangle rec = tableView.getCellRect(rowCopy, colCopy, true);
                    x -= rec.getWidth();
                    if (x <= 0) {
                        break;
                    }
                }

            }
        }
    }

    /**
    * Funcion para cambiar la fuente de una columna
    */
    public void setFontColumna(int col,Font f){
        if (col<0 || col>=tableView.getColumnCount()){
            msgError="Error(setFontColumna):Nnmero de columna erroneo";
            return;
          }
        renders[col].setFuente(f);
        setRenderer(col,renders[col]);
    }

    /**
    * Funcion que establece un formato de Presentacinn en grid de una columna (-1 todas)
    * La columna comienza en 0.
    * El formato, en caso de utilizar un DatosTabla para cargar los datos,
    * soporta tipo fecha.
    * Para poner un formato tipo Boolean se debera Mandar en el String una 'B'
    * como primer caracter
    * y como segundo caracter el que se entendera como activo .. Ej: 'BS'
    * Si el Formato empieza por 'B' se llamara a resetRenderer(col)
    * @since 23/8/06
    * Si el formato es tipo "B-" sera tipo Boolean entendiendose como true cualquier valor diferente
    * de 0, siempre y cuando sea numerico.
    **/
   public void setFormatoColumna(int col, String formato)
   {
   
     if (col < -1 || col >= tableView.getColumnCount())
     {
       msgError = "Error(setFormatoColumna):Nnmero de columna erroneo";
       return;
     }
     if (formato==null)
         formato="";
     if (col == -1)
       for (int x = 0; x < tableView.getColumnCount(); x++)
         Formato[x] = formato;
     else
     {      
       if (formato.startsWith("B"))
         resetRenderer(col);
       Formato[col] = formato;
     }
   }
   public String getFormatoColumna(int col)
   {
     return Formato[col];
   }

     /**
    * Funcion que establece un formato de Presentacinn en grid de una columna (-1 todas)
    * La columna comienza en 0.
    **/
    public void setFormatoColumna(String nomcol,String formato){
        int pos=buscarPosicionColumna(nomcol);
        if (pos==-1) {
            msgError="Error(cogerCelda):Columna no encontrada.";
            return;
        }
        setFormatoColumna(pos,formato);
    }

    /**
    * Funcion para cambiar el color de una columna.
    */
    // Solo cambia background.
    public void setColorColumna(int col,Color cback){
        if (col<0 || col>=tableView.getColumnCount()){
            msgError="Error(setColorColumna):Nnmero de columna erroneo";
            return;
          }
        renders[col].setOldBackground(cback);
        setRenderer(col,renders[col]);
    }

    // Cambia el background y el foreground.
    public void setColorColumna(int col,Color cback,Color cfore){
        if (col<0 || col>=tableView.getColumnCount()){
            msgError="Error(setColorColumna):Nnmero de columna erroneo";
            return;
          }
        renders[col].setOldBackground(cback);
        renders[col].setOldForeground(cfore);
        setRenderer(col,renders[col]);
    }

    // Solo cambia background.
    public void setColorColumna(String nomcol,Color cback){
        int pos=buscarPosicionColumna(nomcol);
        if (pos==-1) {
            msgError="Error(setColorColumna):Columna no encontrada.";
            return;
        }
        setColorColumna(pos,cback);
    }
    /**
    * Funcion llamada si se quiere dar la opcion de salvar los datos del grid.
    * Aparecera una nueva opcion en el menu contestual que sera salvar.
    * Guardara el ancho y posicion de las columnas para el usuario que lo ejecute.
    *
    * En el momento en que se ejecuta la rutina restaura los datos que pudiera haber.
    * Es importante llamar a setConfigurar antes de setAncho para que los valores
    * salvados tengan uso.
    *
    * @param program Nombre del programa
    * @param usuario Variable de tipo EntornoUsuario
    * @param datTab datosTabla para apoyarnos en la conexi�n.
    **/
    public boolean setConfigurar(String program,EntornoUsuario entUsuario,DatosTabla datTab)throws Exception
    {

        TableColumn colu;
        for(int c=0;c<getColumnCount();c++){
           colu=tableView.getColumn(tableView.getColumnName(c));
           valAnteriores.addElement(""+colu.getPreferredWidth());
        }

        //A�adimos los componentes al popMenu
        if (MENU) popMenu.addSeparator();
        popMenu.add(configItem);
        popMenu.add(resetItem);

        conex=datTab.getConexion();
        dt=new DatosTabla();
        entUsu=entUsuario;

        nomPrg=program;
        String s;

        dt.setConexion(conex);
        if (dt.getError()){
             msgError="No se ha podido Establecer la CONEXION";
             throw new Exception(msgError + " - " + dt.getMsgError());
        }

        // Almacenamos la situacion de columnas inicial.
        for(int n=0;n <getColumnCount();n++)
        {
            identif.addElement(""+tableView.getColumn(tableView.getColumnName(n)).getIdentifier());
        }
        s="SELECT * FROM gridajuste WHERE emp_codi="+entUsuario.em_cod+
          " AND usu_nomb='"+entUsuario.usuario+"'"+
          " AND gra_nomb='"+program+"' ORDER BY grd_poscol";
        try{
           if (!dt.select(s)){
              CONFIGURAR=true;
              return true;
           }
           int posic=0,i,p;
//           TableColumn col;
           do{
             i=dt.getInt("grd_colum");
             //col=tableView.getColumn(tableView.getColumnName(i));
             //col.setWidth(dt.getDatoInt("col_ancho"));
             p=0;
             for(int l=0;l<getColumnCount();l++)
             {
                 p=l;
                 if (tableView.getColumnName(l).equals(""+identif.elementAt(i)))
                     break;
             }
             tableView.moveColumn(p,posic);
             setAnchoColumna(posic,dt.getInt("grd_anccol"));
             posic++;
           }while (dt.next());
        }
        catch(SQLException e){
           msgError="Error al recoger datos de configuracion";
           throw new SQLException(msgError + " - " + e.getMessage());
        }

        CONFIGURAR=true;
        return true;

    }// fin setConfigurar

    public boolean getConfigurar(){
      return CONFIGURAR;
    }

    // Cambia el background y el foreground.
    public void setColorColumna(String nomcol,Color cback,Color cfore){
        int pos=buscarPosicionColumna(nomcol);
        if (pos==-1) {
            msgError="Error(setColorColumna):Columna no encontrada.";
            return;
        }
        setColorColumna(pos,cback,cfore);
    }


    /**
    * Funcinn que me alinea la columnna especificada segnn:
    * 0:Alineacinn izquierda.
    * 1:Alineacinn centro.
    * 2:Alineacion derecha.
    */
    // La columna especificada.
    public void alinearColumna(int col,int alineacion){
          if (col<0 || col>tableView.getColumnCount()-1){
            msgError="Error(alinearColumna):Nnmero de columna erroneo";
            return;
          }
          if (alineacion<0 || alineacion>2){
            msgError="Error:Nnmero de alineacion erroneo";
            return;
          }
        switch(alineacion){
          case 0:renders[col].setHorizontalAlignment(JLabel.LEFT);
                  break;
          case 1:renders[col].setHorizontalAlignment(JLabel.CENTER);
                  break;
          case 2:renders[col].setHorizontalAlignment(JLabel.RIGHT);
                  break;
        }
        setRenderer(col,renders[col]);
    }
    /**
     * Devuelve la Alineacion de la columna
     * @param col int N� Columna
     * @return int -1 En caso de error
     */
    public int getColumnaAlineacion(int col)
    {
      if (col<0 || col>tableView.getColumnCount()-1){
          msgError="Error(alinearColumna):Nnmero de columna erroneo";
          return -1;
        }
        int alineacion=renders[col].getHorizontalAlignment();
        switch(alineacion){
          case JLabel.LEFT: return 0;
          case JLabel.CENTER:  return 1;
          case JLabel.RIGHT: return 2;
        }
        return -1;
    }
    // Para todas las columnas.
    public void alinearColumna(int[] alineacion){
       if (alineacion.length != tableView.getColumnCount()){
          msgError="Error(alinearColumna):Nnmero de columnas errnneo";
          return;
       }
       for(int i=0;i<alineacion.length;i++)
       {
          alinearColumna(i,alineacion[i]);
       }
    };

    public void setAlinearColumna(int[] alineacion){
       alinearColumna(alineacion);
    }

    public void setAlinearColumna(int col,int alineacion){
       alinearColumna(col, alineacion);
    }
    /**
    * Para posicionarse en un elemento en concreto,compara el contenido de las
    * celdas con busca.
    */
   public String busca = "";
   GridBagLayout gridBagLayout1 = new GridBagLayout();
   public void buscaTexto()
   {
     int ca = this.tableView.getSelectedColumn(); // Columna Activa.
     int nr = this.tableView.getRowCount(); // Numero de Lineas
     buscaTexto(ca, nr);
   }

    public void buscaTexto(int columna, int numeroLineasGrid)
    {
      int rw = 0;
      while (rw < numeroLineasGrid)
      {
        if (this.tableView.getValueAt(rw,columna).toString().trim().startsWith(busca))
        {
          requestFocus(rw, columna);
          ajustar_scroll(rw);
          break;
        }
        rw++;
      }
    }




    public void ajustar(){
      AJUSTAR=true;
    }

    /**
     * Establece si la anchura de las columnas se deben ajustar al tama�o del
     * grid. La diferencia respecto a setAjustarGrid es que esta funcion NO ajusta el
     * tamañoo del grid al Panel que lo contiene.
     * @deprecated use setAjustarColumnas
     * @param aj boolean True ajustar. False NO Ajustar Anchura columnas
     */
    public void ajustar(boolean aj){
        setAjustarColumnas(aj);
    }
    /**
     * Indica si se deben ajustar el ancho de las columnas a la anchura del GRID automaticamente.
     * La diferencia respecto a setAjustarGrid es que esta funcion NO ajusta el
     * tama�o del grid al Panel que lo contiene.
     *
     * Por defecto el valor es 'false'
     * @param aj boolean Ajustar?
     */
    public void setAjustarColumnas(boolean aj){
        AJUSTAR=aj;
    }

    public void setAjustaPanel(boolean aj)
    {
      swAjustarGrid=aj;
    }
    public void setCuadrar(boolean b){
      cuadrar=b;
    }

    /**
    * Funcion que extiende las columnas del grid segnn la escala.
    */
   public void cuadrarGrid() {
      cuadrar();
      redibujar();
   }

    protected void cuadrar(){
      cuadrar(cuadrar);
    }


    protected void cuadrar(boolean ajus)
    {
      int numColumnas = tableView.getColumnCount();
      int nuevasLong[] = new int[numColumnas];
      TableColumn col;
      for (int i = 0; i < numColumnas; i++)
      {
        col = tableView.getColumn(tableView.getColumnName(i));
        nuevasLong[i] = Math.max(col.getMinWidth(),
                                 Math.min(col.getMaxWidth(),
                                          col.getPreferredWidth()));
      }
      if (AJUSTAR)
      {
        double totalHueco = this.getWidth() - 9
            - scrPanel.getVerticalScrollBar().getPreferredSize().width
            - tableView.getIntercellSpacing().width * (numColumnas - 1);
        int minLongCols[] = new int[numColumnas];
        int maxLongCols[] = new int[numColumnas];
        double totalMinCols = 0.0;
        double totalMaxCols = 0.0;
        for (int i = 0; i < numColumnas; i++)
        {
          col = tableView.getColumn(tableView.getColumnName(i));
          minLongCols[i] = col.getMinWidth();
          totalMinCols += col.getMinWidth();
          maxLongCols[i] = col.getMaxWidth();
          totalMaxCols += col.getMaxWidth();
        }
        if (totalHueco <= totalMinCols)
          nuevasLong = minLongCols;
        else
        {
          boolean tratado[] = new boolean[numColumnas];
          double totalPrefCols = 0.0;
          if (totalHueco >= totalMaxCols)
          {
            nuevasLong = maxLongCols;
            totalPrefCols = totalMaxCols;
            for (int i = 0; i < numColumnas; i++)
              tratado[i] = false;
          }
          else
            for (int i = 0; i < numColumnas; i++)
              if (minLongCols[i] == maxLongCols[i])
              {
                nuevasLong[i] = minLongCols[i];
                totalHueco -= minLongCols[i];
                tratado[i] = true;
              }
              else
              {
                tratado[i] = false;
                totalPrefCols += nuevasLong[i];
              }
          for (int i = 0; i < numColumnas; i++)
            if (!tratado[i])
            {
              double nuevaLongitud = ( (double) nuevasLong[i]) *
                  (totalHueco / totalPrefCols);
              totalPrefCols -= nuevasLong[i];
              nuevasLong[i] = (int) (nuevaLongitud);
              totalHueco -= nuevasLong[i];
            }
        }
        if (ajus)
        {
          setAsignaMinWith(false);
          setAnchoColumna(nuevasLong);
          setAsignaMinWith(true);
        }
        else
          setAnchoColumna(nuevasLong);
        tableView.getTableHeader().resizeAndRepaint();
        scrPanel.getHorizontalScrollBar().setVisible(false);
        redibujar();
      }
    }

    /**
    * Funcinn que me coloca un boton para ajustar las columnas del grid
    **/
    public void setBotonAjustar(boolean b){
         Bajustar.setVisible(b);
    }
    /**
     * Machacar para controlar que hacer cuando se cambia de columna.
     *
     * @param col
     */
    protected void cambiaColumna(int col)
    {
      afterCambiaColumna(col,getSelectedRow());
    }
    /**
     * Funcion a Machacar si se se controlar algo cuando se cambia de columna
     * @deprecated usar afterCambiaColumna
     * @param col int
     * @param row int
     */
    protected void cambiaColumna(int col,int row)
    {
         afterCambiaColumna(col,row);
    }
    /**
     * Funcion a Machacar si se se controlar algo cuando se cambia de columna
     * @param col int
     * @param colNueva int
     * @param row int
     */
    protected void afterCambiaColumna(int col,int colNueva,int row)
    {
        
    }   
     /**
     * Funcion a Machacar si se se controlar algo cuando se cambia de columna
     * @param col int
     * @param row int
     */
    protected void afterCambiaColumna(int col,int row)
    {
    }
    /**
     * Funcion a Machacar si se se controlar algo cuando se cambia de columna
     * @deprecated usar afterCambiaColumna
     * @param col int
     * @param colNueva int
     * @param row int
     */
    protected void cambiaColumna(int col,int colNueva,int row)
    {
        afterCambiaColumna(col,colNueva,row);
    }
    /**
     * Llamado antes de procesar eventos de tecla en KeyEvent
     * @param ke KeyEvent
     * @return boolean  true si debe seguir procesando EVENTO. False en caso
     * contrario.
     */
    protected boolean procesaKeyEvent(KeyEvent ke)
    {
      return true;
    }
    /**
    * Creacion de los tipos de controles y columnas de la tabla.
    */
    private void crear_controles_tabla(){
       // Creando la estructura de ordenacinn.
       sorter = new TableSorter(datosModelo);
       // Creando la tabla
       tableView = new JTable(sorter)
       {
         public boolean editCellAt(int row, int col, EventObject e)
         {
           if (super.editCellAt(row, col, e))
           {
             getEditorComponent().requestFocus();
             return true;
           }
           return false;
         }
          protected void processKeyEvent(KeyEvent ke) {
           if (!procesaKeyEvent(ke))
           {
             super.processKeyEvent(ke);
             return;
           }
           if (ke.getID() == KeyEvent.KEY_PRESSED)
           {
             switch (ke.getKeyCode())
             {
               case KeyEvent.VK_TAB:
                 if (swGridEditable)
                   return;
                 else
                 {
                   super.processKeyEvent(ke);
                   break;
                 }
               case KeyEvent.VK_C:
                 if (ke.isControlDown())
                 { // Pulsado Control-C
                   ClipEditable.clipboard(getValCopy(getSelectedRow(), getSelectedColumn()));
                   break;
                 }
               case KeyEvent.VK_F2:
               case KeyEvent.VK_F3:
               case KeyEvent.VK_F4:
               case KeyEvent.VK_F5:
               case KeyEvent.VK_F6:
               case KeyEvent.VK_F7:
               case KeyEvent.VK_F8:
               case KeyEvent.VK_F9:
               case KeyEvent.VK_ESCAPE:
                 AbstractButton defaultButton = getButton(ke.getKeyCode());
                 if (defaultButton != null)
                 {
                   defaultButton.requestFocus();
                   defaultButton.doClick();
                 }
                 else
                   super.processKeyEvent(ke);
                 break;
               default:
                 super.processKeyEvent(ke);
                 break;
             }
           }
           else
           {
             super.processKeyEvent(ke);
           }
         }

       };
       
       tableView.addKeyListener(new KeyAdapter()
       {
           public void keyPressed(KeyEvent ke) 
           {                     
                switch (ke.getKeyCode())
                {
//                  case KeyEvent.VK_TAB:
//                    if (swGridEditable)
//                      return;
//                    else
//                    {
//                      super.processKeyEvent(ke);
//                      break;
//                    }
                  case KeyEvent.VK_C:
                    if (ke.isControlDown())
                    { // Pulsado Control-C
                      ClipEditable.clipboard(getValCopy(getSelectedRow(), getSelectedColumn()));
                      break;
                    }
                  case KeyEvent.VK_F2:
                  case KeyEvent.VK_F3:
                  case KeyEvent.VK_F4:
                  case KeyEvent.VK_F5:
                  case KeyEvent.VK_F6:
                  case KeyEvent.VK_F7:
                  case KeyEvent.VK_F8:
                  case KeyEvent.VK_F9:
                  case KeyEvent.VK_ESCAPE:
                    AbstractButton defaultButton = getButton(ke.getKeyCode());
                    if (defaultButton != null)
                    {
                      defaultButton.requestFocus();
                      defaultButton.doClick();
                    }
                }              
            } 
        });
       headerToolTip=new ToolTipHeader(tableView.getColumnModel());
       tableView.setTableHeader(headerToolTip);
       
       // Si lo hemos especificado el grid ordenaran al picar sobre la cabecera,en
       // orden ascendente y si a la vez pulsamos SHIFT en orden descendente.
       if (swOrden)
         sorter.setTableHeader(headerToolTip);
//         sorter.addMouseListenerToHeaderInTable(tableView);

       // Solo se puede seleccionar una sola fila.
       tableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       // Las columnas no se autoajustan cuando la tabla cambia de tamano(con
       // la opcinn AUTO_RESIZE_ALL_COLUMNS les dn a todas las columnas el mismo tamano).
       tableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       tableView.setSelectionBackground(new Color(216,208,200));
       tableView.setSelectionForeground(Color.black);

      //Establecer la tabla con el scroll.
        scrPanel = new CScrollPane(tableView);
        scrPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        scrPanel.setVerticalScrollBarPolicy(CScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // crea una linea negra alrededor de la tabla.
        scrPanel.setViewportBorder(BorderFactory.createLineBorder(Color.gray));
        // La unidad de incremento del grid sern el alto de celda.
        int incremento=tableView.getRowHeight();
        scrPanel.getVerticalScrollBar().setUnitIncrement(incremento);

        activarEventos();
    }

    public void setBotonBorrar()
    {
      prepaBotonInsDele();
      Bborra.setToolTipText("Borrar Linea Activa del Grid (F8)");
      Bborra.setPreferredSize(new Dimension(20, 20));
      Pboton.add(Bborra, null);
      Bborra.addActionListener(new java.awt.event.ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Bborra_actionPerformed();
        }
      });
      this.setButton(KeyEvent.VK_F8,Bborra);
    }
    public boolean getTengoFoco()
    {
        return tengoFoco;
    }
    void setTengoFoco(boolean foco)
    {
        tengoFoco=foco;
    }
    protected void Bborra_actionPerformed()
    {
      if (! isEnabled())
        return;

      if (TABLAVACIA)
        return;
      int rw = tableView.getSelectedRow();
      if (!deleteLinea(rw, tableView.getSelectedColumn()))
      {
        requestFocus(getSelectedRow(), getSelectedColumn());
        return;
      }
      int nRow = getRowCount();
      removeLinea(rw);
      if (rw == nRow - 1)
        rw = (rw > 0) ? rw - 1 : 0;
      requestFocus(rw, getSelectedColumn());
      afterDeleteLinea();
    }

    public void afterDeleteLinea()
    {

    }

    public void setBotonInsert()
    {
      prepaBotonInsDele();
      Binser.setToolTipText("Insertar encima de la Linea Activa del Grid (F7)");
      Binser.setPreferredSize(new Dimension(20, 20));
      Pboton.add(Binser, null);
      Binser.addActionListener(new java.awt.event.ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Binser_actionPerformed(true);
        }
      });
      this.setButton(KeyEvent.VK_F7,Binser);
    }
    public CPanel getPanelBotones()
    {
        return Pboton;
    }
    protected void Binser_actionPerformed(boolean b)
    {
    }

    public boolean deleteLinea(int row, int col)
    {
      return canBorrarLinea;
    }

    void prepaBotonInsDele()
    {
      if (swPrepaBotonInsDele)
      {
        swPrepaBotonInsDele=false;
        setBuscarVisible(true);
        panelBusqueda.remove(panelBuscar);
        panelBusqueda.remove(Bajustar);
        Pboton.setLayout(new GridLayout());
        panelBusqueda.add(Pboton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
      }
    }

    private void activarEventos()
    {
      panelG.addAncestorListener(new AncestorListener()
      {
        public void ancestorAdded(AncestorEvent event)
        {
          if (swPonPanel)
            return;
          ponPanel();
        }

        public void ancestorRemoved(AncestorEvent event)
        {}

        public void ancestorMoved(AncestorEvent event)
        {
          if (swPonPanel)
            return;
          ponPanel();
        }
      });
      // Panel principal.

      // ----------------    EVENTOS --------------------
      Bajustar.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          AJUSTAR = true;
          cuadrar();
          AJUSTAR = false;
          ponPanel();
        }
      });

      tableView.addKeyListener(new KeyListener()
      {
        public void keyTyped(KeyEvent e)
        {}

        public void keyReleased(KeyEvent e)
        {}

        public void keyPressed(KeyEvent e)
        {
          if (e.isActionKey())
          {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT)
            {
              busquedaE.setText("");
              busca = "";
              colSelec = getSelectedColumn();
            }
          }

          if (!buscarVisible) return;
          if (e.isAltDown())  return;

          char tecla = e.getKeyChar();
          if (tecla == KeyEvent.CHAR_UNDEFINED)
            return;
          boolean tablaEnabled = true;
          if (tablaEnabled)
          {
            // Buscar campo
            if (Character.isISOControl(tecla))
            {
              if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
              {
                if (busca.length() > 0)
                {
                  busca = (busca.substring(0, busca.length() - 1));
                  busquedaE.setText(busca);
                  buscaTexto();
                }
              }
              if (e.getKeyCode() == KeyEvent.VK_DELETE)
              {
                busca = "";
                busquedaE.setText(busca);
                buscaTexto();
              }
              return; // Es una tecla de control (Cursores,ENTER ...)
            }
            if (mayusculas) tecla = Character.toUpperCase(tecla);
            busca = busca + tecla;
            busquedaE.setText(busca);
            buscaTexto();
          } // Fin Búsqueda de elemento.
        } // final KeyPressed
      }); // final de addKeyListener.
/*
      scrPanel.getViewport().addChangeListener(new ChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          if (!gridActivo)return;
          if ( !finRecargar)return;
          if (posYview == scrPanel.getViewport().getViewPosition().y)
            return;
          posYview = scrPanel.getViewport().getViewPosition().y;
          finRecargar = false;
          int alto = tableView.getHeight();
          Point p = scrPanel.getViewport().getViewPosition();
          if (p.y + scrPanel.getViewport().getHeight() > alto - puntoDeScroll)
          {
            if (swLanzaCargaDatosThread)
              javax.swing.SwingUtilities.invokeLater(new Thread()
              {
                public void run()
                {
                  reCargar();
                }
              });
            else
              reCargar();
          }
          else
          {
            finRecargar = true;
          }
        }
      });
*/
      this.addComponentListener(new ComponentListener()
      {
        // Controla el ajuste cuando AJUSTAR=true.
        public void componentResized(ComponentEvent z)
        {
          cuadrar();
        }

        public void componentHidden(ComponentEvent z)
        {};
        public void componentMoved(ComponentEvent z)
        {}

        public void componentShown(ComponentEvent z)
        {
          cuadrar();
        }
      });

      // Poniendo Orejas.
      tableView.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent m)
        {
          if (getSelectedColumn() != colSelec)
          {
            busquedaE.setText("");
            busca = "";
            colSelec = getSelectedColumn();
          }
          procesaMouse(m);
        }
      });

      scrPanel.getViewport().addChangeListener(new ChangeListener()
      {
            @Override
        public void stateChanged(ChangeEvent e)
        {
          if (!gridActivo)return;
          if (!isEnabled() || !finRecargar)return;
          if (posYview == scrPanel.getViewport().getViewPosition().y)
                return;
          posYview = scrPanel.getViewport().getViewPosition().y;
          finRecargar = false;
          int alto = tableView.getHeight();
          Point p = scrPanel.getViewport().getViewPosition();
          if (p.y + scrPanel.getViewport().getHeight() > alto - puntoDeScroll)
          {
            if (swLanzaCargaDatosThread)
              javax.swing.SwingUtilities.invokeLater(new Thread()
              {
                public void run()
                {
                  reCargar();
                }
              });
            else
              reCargar();
          }
          else
            finRecargar = true;
        }
      });
      
      copyCellItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          ClipEditable.clipboard(getValCopy(rowCopy, colCopy));
        }
      });
      copyRowItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          String linea = "";
          for (int i = 0; i < getColumnCount(); i++)
          {
            if (!linea.equals(""))
              linea += ( (char) 9);
            linea += getValCopy(rowCopy, i);
          }
          ClipEditable.clipboard(linea);
        }
      });
      copyColItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          String linea = "";
          for (int i = 0; i < getRowCount(); i++)
          {
            if (!linea.equals(""))
              linea += "\n";
            linea += getValCopy(i, colCopy);
          }
          ClipEditable.clipboard(linea);
        }
      });
      copyAllItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          String linea = "";
          for (int i = 0; i < datosModelo.getColumnCount(); i++)
          {
            if (!linea.equals(""))
              linea += ( (char) 9);
            linea += datosModelo.getColumnName(i);
          }
          for (int i = 0; i < getRowCount(); i++)
          {
            linea += "\n";
            for (int x = 0; x < getColumnCount(); x++)
            {
              if (x != 0)
                linea += ( (char) 9);
              linea += getValCopy(i, x);
            }
          }
          ClipEditable.clipboard(linea);
        }
      });
      configItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (!saveConfigurar())
          {
            processErrorDB(msgError);
          }
          else
          {
            JOptionPane.showMessageDialog(panelG,
                                          "Configuracion del grid salvada.", "",
                                          JOptionPane.INFORMATION_MESSAGE);
          }
        }
      });
      resetItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (!resetConfigurar())
          {
            processErrorDB(msgError);
          }
          else
          {
            // Restaurar valores por defecto
            int p;
            for (int c = 0; c < getColumnCount(); c++)
            {
              p = 0;
              for (int l = 0; l < getColumnCount(); l++)
              {
                p = l;
                if (tableView.getColumnName(l).compareTo("" + identif.elementAt(c)) ==
                    0)break;
              }
              tableView.moveColumn(p, c);
              setAnchoColumna(c,
                              (new Integer("" + valAnteriores.elementAt(c))).intValue());
            }
            JOptionPane.showMessageDialog(panelG,
                                          "Configuracion del grid borrada.", "",
                                          JOptionPane.INFORMATION_MESSAGE);
          }
        }
      });
     }

     /**
      * Función que borra de la Base de datos la configuración del grid.
      **/
     public boolean resetConfigurar()
     {
       String s;
       s = "SELECT emp_codi FROM gridajuste WHERE emp_codi=" + entUsu.em_cod +
           " AND usu_nomb='" + entUsu.usuario + "' AND gra_nomb='" + nomPrg + "'";
       try
       {
         if (dt.select(s, true))
         {
           dt.delete();
         }
         dt.commit();
       }
       catch (Exception ff)
       {
         msgError = "Error en conexion - " + ff.getMessage();
         return false;
       }

       return true;
     }

     public void processErrorDB(String msg){
       try {
         dt.rollback(); //Deshacemos las insert oportunas
       } catch (Exception k)
       {
         SystemOut.print(k);
       }
        JOptionPane.showMessageDialog(panelG,"Error al salvar configuracion"+msg,"ERROR",JOptionPane.INFORMATION_MESSAGE);
     }

     private String getValCopy(int row, int col) {
             String s = getValString(row, col, true);
             s = ClipEditable.formatCopy(s);
             return s;
     }




     /**
     * Funci�n que salva en la DB la configuraci�n del grid.
     **/
     public boolean saveConfigurar(){
        String s="";
        boolean NUEVO=true;
        s="SELECT * FROM gridajuste WHERE emp_codi="+entUsu.em_cod+
              " AND usu_nomb='"+entUsu.usuario+"' AND gra_nomb='"+nomPrg+"'";
        try{
               if(dt.select(s,true))
                 NUEVO=false;
               else
                 NUEVO=true;
        }
        catch(Exception e){
                msgError="Error en conexion";
                fatalError(msgError + "\n" + e.getMessage());
                return false;
        }

        try{
           if(!NUEVO)
           {
           // Ya existen valores
              int p,colu;
              Vector se=new Vector();

              do
              {
                p=0;
                colu=dt.getInt("columna");
                while(tableView.getColumn(tableView.getColumnName(p)).getModelIndex()!=colu)
                {
                     p++;                  
                     if (p==getColumnCount()) 
                         break;
                }
//               s="col_ancho="+tableView.getColumn(tableView.getColumnName(p)).getWidth()+
//                 ",grd_poscol="+p;
                se.addElement(" UPDATE gridajuste SET "+
                   "grd_anccol="+tableView.getColumn(tableView.getColumnName(p)).getPreferredWidth()+
                  ",grd_poscol ="+p+
                  " WHERE emp_codi="+entUsu.em_cod+
                  " AND usu_nomb='"+entUsu.usuario+"' AND gra_nomb='"+nomPrg+"'");
//                dt.executeUpdate(s);
              }while (dt.next());
              for (int n=0;n<se.size();n++)
              {
                dt.executeUpdate(se.elementAt(n).toString());
              }
           }// fin cambio valores
           else
           {
           // Es un dato nuevo
              for(int numcol=0;numcol<getColumnCount();numcol++)
              {
                  TableColumn col=tableView.getColumn(tableView.getColumnName(numcol));
                  s="INSERT INTO gridajuste VALUES("+entUsu.em_cod+",'"+entUsu.usuario+
                  "','"+nomPrg+"',"+col.getModelIndex()+ "," + col.getPreferredWidth()+ "," + numcol +")";

                  if(dt.executeUpdate(s)==0){
                      msgError="Datos no salvados correctamente \n" + dt.getMsgError();
                      fatalError(msgError);
                      return false;
                  }
              }
           }// fin dato nuevo
           dt.commit();
        }
        catch(Exception e){
                msgError="Error en conexion";
                fatalError(msgError + "\n" + e.getMessage());
                return false;
        }

        return true;
     }
     void fatalError(String men){
         //dt.rollback();
         JOptionPane.showMessageDialog(this,men,"ERROR AL Grabar Configuracion del Grid\n"+men,JOptionPane.ERROR_MESSAGE);
     }

     public boolean getLlame(){
        return llame;
     }
     /**
     *  Devuelve la fila seleccionada
     * Si el Grid esta Disabled, devuelve la fila en que se puso disabled.
     **/
     public int getSelectedRow()
     {
        if (isEnabled())
          return tableView.getSelectedRow();
        else
        {
          if (rowFocus<0)
            return 0;
          if (rowFocus >=tableView.getRowCount())
            return tableView.getRowCount()-1;
          return rowFocus;
        }
     }
     public int getSelectedColumnDisab()
     {
       return tableView.getSelectedColumn();
     }
     /**
      * Devuelve la linea que esta activa
      * (aunque este disabled el grid muestra la real, al contrario que getSelectedRow)
      * @see getSelectedRow
      * @return Linea ó fila (row) activa en el grid
      */
     public int getSelectedRowDisab()
     {
       return tableView.getSelectedRow();
     }
     /**
     *  Devuelve la columna seleccionada
     **/
     public int getSelectedColumn()
     {
      if (isEnabled())
        return tableView.getSelectedColumn();
      else
        return columnFocus;
     }

     /**
     * Nnmero de filas del grid
     **/
     public int getRowCount(){
      return datosModelo.getRowCount();
     }


     /**
     * Nnmero de filas visibles del grid
     * (IMPORTANTE:del tableView no del datosmodelo)
     **/
     public int getRowViewCount(){
        return tableView.getRowCount();
     }


     /**
     * Nnmero de columnas visibles del grid
     * (IMPORTANTE:del tableView no del datosmodelo)
     **/
     public int getColumnViewCount(){
        return tableView.getColumnCount();
     }



     /**
     * Nnmero de columnas del grid
     **/
     public int getColumnCount(){
      return datosModelo.getColumnCount();
     }

     public void setRowSelectionInterval (int x,int x1){
      tableView.setRowSelectionInterval(x,x1);
      ajustar_scroll(x);
     }

     public void setRowSelectionInterval (int x){
      tableView.setRowSelectionInterval(x,x);
      ajustar_scroll(x);
     }


/* private boolean activadoParent=true;
 private boolean activado=true;

 public void setEnabled(boolean enab)
 {
    activado=enab;
    System.out.println("Vgrid: "+getNombre()+" setEnabled: "+enab);
    if (enab)
    {
      if (activadoParent)
      {
        enabled=enab;
        super.setEnabled(true);
//        setEnabledComponents(this, true);
      }
      return;
    }
    enabled=enab;
//    setEnabledComponents(this, enab);
  }


  public void setEnabledParent(boolean enab)
  {
    System.out.println("Vgrid: "+getNombre()+" setEnabledParent: "+enab);
    activadoParent=enab;
    if (! enab)
    {
      if (super.isEnabled())
      {
        setEnabled(false);
      }
    }
    else
    {
      if (! super.isEnabled() && activado)
        super.setEnabled(true);
    }
  }

/*  public boolean isEnabled(){
    return enabled;
  }
*/


     public void setActivoMenu(boolean t){
      activoMenu=t;
     }

     public boolean getActivoMenu(){
      return activoMenu;
     }

     public void setClickCons(boolean b){
      clickCons=b;
     }

     public boolean getClickCons(){
      return clickCons;
     }

     public void setCellEditable(boolean b){
          cellEditable=b;
          datosModelo.setCellEditable(b);
     }

     public boolean getCellEditable(){
         return cellEditable;
     }
     public boolean getCellEditable(int col){
         return datosModelo.isCellEditable(0,col);
     }
     public void setCellEditable(boolean b,int col){
         datosModelo.setCellEditable(b,col);
     }
    @Override
     public void setEditable(boolean isEditable)
     {
       swGridEditable=isEditable;
     }
     /**
     * Devuelve la Posicion Y en la parte de arriba de la Linea activa.
     */
    public int getPosY()
    {
      Rectangle re=tableView.getCellRect(getSelectedRow(),0,false);
      Point p=scrPanel.getViewport().getViewPosition();
      int xMas=tableView.getTableHeader().getSize().height+2-p.y;
      return re.getLocation().y+ xMas;
    }

  /**
   * Indica si el Grid sera ordenable
   *
   * @param ordenable true es ordenable. (valor por defecto)
   */
  public void setOrdenar(boolean ordenable) {
         if (ordenable)
             sorter.setTableHeader(tableView.getTableHeader());
         else
             sorter.setTableHeader(null);
         swOrden = ordenable;
  }
  public boolean isOrdenable()
  {
      return swOrden;
  }
  public JScrollPane getScrollPane()
  {
    return scrPanel;
  }
  public JPopupMenu getPopMenu()
  {
    return popMenu;
  }
 }
 

  class miDefaultTableModel extends DefaultTableModel
  {
      ArrayList editar;
      int nColum=0;
      boolean actualizar=true;
      Cgrid table;
      public miDefaultTableModel(int nCol,Cgrid jt)
      {
        nColum=nCol;
        table=jt;
        editar= new ArrayList();
        for (int n=0;n< nColum;n++)
        {
          editar.add("N");
        }
      }
    @Override
      public Class getColumnClass(int col) {
        return getValueAt(0,col).getClass();
      }

    @Override
      public boolean isCellEditable(int row, int col)
      {
        if ( col < 0 || col>nColum || col>=editar.size() )
          return false;
        if (! table.isEnabled())
          return false;
        if (editar.get(col).toString().compareTo("S")==0)
          return true;
        else
          return false;
      }

      public void setCellEditable(boolean b)
      {
        editar.clear();
        for (int n=0;n< nColum;n++)
        {
          if (b)
            editar.add("S");
          else
            editar.add("N");
        }
      }
      public void setCellEditable(boolean b,int col)
      {
        if (b)
          editar.set(col,"S");
        else
          editar.set(col,"N");
      }

  }
  class miCellEditor extends AbstractCellEditor
    implements TableCellEditor, TreeCellEditor
  {
    protected int clickCountToStart = 1;
    protected miEditorDelegate delegate;
    protected JComponent editorComponent;

    public miCellEditor(final JButton button)
    {
      editorComponent = button;
      this.clickCountToStart = 2;
      delegate = new miEditorDelegate()
      {
            @Override
        public void setValue(Object value)
        {
          button.setText( (value != null) ? value.toString() : "");
        }

            @Override
        public Object getCellEditorValue()
        {
          return button.getText();
        }
      };
      button.addActionListener(delegate);
    }

    public Component getTreeCellEditorComponent(JTree tree, Object value,
                                               boolean isSelected,
                                               boolean expanded,
                                               boolean leaf, int row) {
       String         stringValue = tree.convertValueToText(value, isSelected,
                                           expanded, leaf, row, false);

       delegate.setValue(stringValue);
       return editorComponent;
   }
   public Object getCellEditorValue() {
         return delegate.getCellEditorValue();
     }
     public Component getTableCellEditorComponent(JTable table, Object value,
                                                   boolean isSelected,
                                                   int row, int column) {
          delegate.setValue(value);
          return editorComponent;
      }


//    public miCellEditor(final JButton button)
//    {
//       editorComponent = button;
//       delegate = new EditorDelegate() {
//          public void setValue(Object value) {
//              button.setText((value != null) ? value.toString() : "");
//          }
//          public Object getCellEditorValue() {
//              return button.getText();
//          }
//      };
//      button.addActionListener(delegate);
//
//   }
   protected class miEditorDelegate implements ActionListener, ItemListener, Serializable {

         /**  The value of this cell. */
         protected Object value;

        /**
         * Returns the value of this cell.
         * @return the value of this cell
         */
         public Object getCellEditorValue() {
             return value;
         }

        /**
         * Sets the value of this cell.
         * @param value the new value of this cell
         */
             public void setValue(Object value) {
             this.value = value;
         }

        /**
         * Returns true if <code>anEvent</code> is <b>not</b> a
         * <code>MouseEvent</code>.  Otherwise, it returns true
         * if the necessary number of clicks have occurred, and
         * returns false otherwise.
         *
         * @param   anEvent         the event
         * @return  true  if cell is ready for editing, false otherwise
         * @see #setClickCountToStart
         * @see #shouldSelectCell
         */
         public boolean isCellEditable(EventObject anEvent) {
             if (anEvent instanceof MouseEvent) {
                 return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
             }
             return true;
         }

        /**
         * Returns true to indicate that the editing cell may
         * be selected.
         *
         * @param   anEvent         the event
         * @return  true
         * @see #isCellEditable
         */
         public boolean shouldSelectCell(EventObject anEvent) {
             return true;
         }

        /**
         * Returns true to indicate that editing has begun.
         *
         * @param anEvent          the event
         */
         public boolean startCellEditing(EventObject anEvent) {
             return true;
         }

        /**
         * Stops editing and
         * returns true to indicate that editing has stopped.
         * This method calls <code>fireEditingStopped</code>.
         *
         * @return  true
         */
         public boolean stopCellEditing() {
             fireEditingStopped();
             return true;
         }

        /**
         * Cancels editing.  This method calls <code>fireEditingCanceled</code>.
         */
        public void cancelCellEditing() {
            fireEditingCanceled();
        }

        /**
         * When an action is performed, editing is ended.
         * @param e the action event
         * @see #stopCellEditing
         */
         public void actionPerformed(ActionEvent e) {
             miEditorDelegate.this.stopCellEditing();
         }

        /**
         * When an item's state changes, editing is ended.
         * @param e the action event
         * @see #stopCellEditing
         */
         public void itemStateChanged(ItemEvent e) {
             miEditorDelegate.this.stopCellEditing();
         }
     }

  }

class ToolTipHeader extends JTableHeader {
    String[] toolTips;

    public ToolTipHeader(TableColumnModel model) {
      super(model);
    }

    @Override
    public String getToolTipText(MouseEvent e) {
      if (toolTips==null)
          return "";
      int col = columnAtPoint(e.getPoint());
      int modelCol = getTable().convertColumnIndexToModel(col);
      String retStr;
      try {
        retStr = toolTips[modelCol];
      } catch (NullPointerException | ArrayIndexOutOfBoundsException ex) {
        retStr = "";
      }
      if (retStr.length() < 1) {
        retStr = super.getToolTipText(e);
      }
      return retStr;
    }

    public void setToolTipStrings(String[] toolTips) {
      this.toolTips = toolTips;
    }
    
    public void setToolTipString(int col, String toolTipString) {
       if (toolTips==null)
       {
           int nCol=getTable().getColumnCount();
           toolTips=new String[nCol];
           for (int n=0;n<nCol;n++)
               toolTips[n]="";
       }
       toolTips[col]=toolTipString;
    }
    
    public String getToolTipString(int col) {
       if (toolTips==null)
           return null;
       return toolTips[col];
    }

    public String[] getToolTipStrings() {
      return this.toolTips; 
    }
  }