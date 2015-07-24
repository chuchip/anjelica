package gnu.chu.utilidades;
/**
 *
 * <p>Título: navegador</p>
 * <p>Descripcion: Panel con un navegador usado en todos los programas que extienden
 * de ventanaPad usan.
 * Presenta los tipicos botones de adelante/detras/edicion, etc.
 * </p>
 * <p>Copyright: Copyright (c) 2005
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
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */
import java.awt.*;
import java.awt.event.*;
import gnu.chu.sql.*;
import gnu.chu.controles.*;
import gnu.chu.interfaces.*;

public class navegador extends CPanel
{
  private int modificadores=0;
  GridLayout gridLayout1 = new GridLayout();
  private boolean llame=false;
  public CButton btnPrimero = new CButton(Iconos.getImageIcon("start"));
  public CButton btnAnterior = new CButton(Iconos.getImageIcon("previous"));
  public CButton btnQuery = new CButton(Iconos.getImageIcon("query"));
  public CButton btnEdit = new CButton(Iconos.getImageIcon("edit"));
  public CButton btnAddNew = new CButton(Iconos.getImageIcon("nuevo"));
  public CButton btnDelete = new CButton(Iconos.getImageIcon("eraser"));
  public CButton btnSiguiente = new CButton(Iconos.getImageIcon("next"));
  public CButton btnUltimo = new CButton(Iconos.getImageIcon("finish"));
  public CButton btnChose= new CButton(Iconos.getImageIcon("choose"));//Iconos.getImageIcon("chose"));

  private boolean swEvent=false;
  private DatosTabla dt;
  private PAD pd;
  private boolean Error = false;
  private boolean Ejecutar = true;
  private String MsgError = "";
  public static final int TODOS = 0;
  public static final int PRIMERO = 1;
  public static final int ANTERIOR = 2;
  public static final int QUERY = 3;
  public static final int EDIT = 4;
  public static final int ADDNEW = 5;
  public static final int DELETE = 6;
  public static final int SIGUIENTE = 7;
  public static final int ULTIMO = 8;
  public static final int CHOSE= 9;
  public static final int NINGUNO= -1;

  boolean[] addBotones = {false, true, true, true, true, true, true, true, true, true};
   //Indica que boton esta echado sobre el panel el primer valor nunca se utiliza

  public int pulsado=NINGUNO; // Indica cual ha sido el Ultimo Boton Pulsado.
  public boolean incChose; // Mostrar Choose
  public int modo; // Indica el Modo de navegador de los siguientes 4

  /**
   * El modo se pone en binario. Los botones van en el siguiente orden
   * Primero  128
   * Anterior  64
   * Query     32
   * Editar    16
   * Añadir     8
   * Borrar     4
   * Siguiente  2
   * Ultimo     1
   *
   * Asi para poner el solo los cursores se pondra:
   * 128+64+2+1 = 195
   * Cursores y Boton Consultar
   * 195+32=227
   * Grid
   * 16+8+4 =28
   * Consultar
   * 32+16+8+4=
   */
  // El modo se pone en binario. Los botones van en el siguiente orden
// Primero Siguiente
// A

  public static final int BTN_PRIMERO=128;
  public static final int BTN_ANTERIOR=64;
  public static final int BTN_CONSULTAR=32;
  public static final int BTN_EDITAR=16;
  public static final int BTN_ANADIR=8;
  public static final int BTN_BORRAR = 4;
  public static final int BTN_SIGUIENTE = 2;
  public static final int BTN_ULTIMO = 1;

  public static final int NORMAL = 0xFF; // Normal
  public static final int GRID =28; // Anula los cursores y el boton Consultar.
  public static final int CONSU=60; // Anula los cursores, pero permite el boton Consultar.
  public static final int SOLCUR=195; // Solo Muestra los Cursores.
  public static final int CURYCON = 227;  //  Muestra los Cursores y el boton consultar.
  public static final int NOBOTON = 0;  //  No muestra ningun boton

  public navegador()
  {
    this(null, null, false, NORMAL);
  }

  public navegador(PAD pd)
  {
    this(pd, null, false, NORMAL);
  }

  public navegador(PAD pd, boolean addChose)
  {
    this(pd, null, addChose, NORMAL);
  }

  public navegador(PAD pd, boolean addChose, int modo)
  {
    this(pd, null, addChose, modo);
  }

  public navegador(PAD pd, DatosTabla tabla, boolean addChose)
  {
    this(pd, tabla, addChose, NORMAL);
  }

  public navegador(PAD pd, DatosTabla tabla, boolean addChose, int modo)
  {
    incChose = addChose;
    setPAD(pd);
    setDatosTabla(tabla);
    this.modo =  modo;

    try
    {
      jbInit();
    }
    catch (Exception e)
    {
      SystemOut.print(e);
    }
  }

  public void jbInit() throws Exception
  {
    this.setPreferredSize(new Dimension(10, 20));
    this.addKeyListener(new navegador_this_keyAdapter(this));
    this.setLayout(gridLayout1);
    int a=modo & 128 ;
    if (a!=0)
    {
      btnPrimero.setActionCommand("");
      btnPrimero.setToolTipText("Primero");
      btnPrimero.addKeyListener(new navegador_btnPrimero_keyAdapter(this));
      btnPrimero.addActionListener(new navegador_btnPrimero_actionAdapter(this));
      btnPrimero.setMargin(new Insets(0, 0, 0, 0));
      btnPrimero.setMnemonic('P');
      this.add(btnPrimero, null);
    }
    a=modo & 64;
    if (a!=0)
    {
      btnAnterior.setActionCommand("");
      btnAnterior.setToolTipText("Anterior");
      btnAnterior.addKeyListener(new navegador_btnAnterior_keyAdapter(this));
      btnAnterior.addActionListener(new navegador_btnAnterior_actionAdapter(this));
      btnAnterior.setMargin(new Insets(0, 0, 0, 0));
      btnAnterior.setMnemonic('N');
      this.add(btnAnterior, null);
    }
    a=modo&32;
    if (a!=0)
    {
      btnQuery.setActionCommand("");
      btnQuery.setToolTipText("Consultar");
      btnQuery.setMnemonic('C');
      btnQuery.addKeyListener(new navegador_btnQuery_keyAdapter(this));
      btnQuery.addActionListener(new navegador_btnQuery_actionAdapter(this));
      btnQuery.setMargin(new Insets(0, 0, 0, 0));
      this.add(btnQuery, null);
    }
    a=modo&16;
    if (a != 0)
    {
      insBedit();
      this.add(btnEdit, null);
    }
    a=modo&8;
    if (a != 0)
    {
      insBadd(btnAddNew);
      this.add(btnAddNew, null);
    }
    a=modo&4;
    if (a != 0)
    {
      btnDelete.setActionCommand("");
      btnDelete.setToolTipText("Borrar");
      btnDelete.setMnemonic('B');
      btnDelete.addKeyListener(new navegador_btnDelete_keyAdapter(this));
      btnDelete.addActionListener(new navegador_btnDelete_actionAdapter(this));
      btnDelete.setMargin(new Insets(0, 0, 0, 0));
      this.add(btnDelete, null);
    }
    a=modo&2;
    if (a!=0)
    {
      btnSiguiente.setActionCommand("");
      btnSiguiente.setToolTipText("Siguiente");
      btnSiguiente.addKeyListener(new navegador_btnSiguiente_keyAdapter(this));
      btnSiguiente.addActionListener(new navegador_btnSiguiente_actionAdapter(this));
      btnSiguiente.setMargin(new Insets(0, 0, 0, 0));
      btnSiguiente.setMnemonic('G');
      this.add(btnSiguiente, null);
    }
    a=modo&1;
    if (a!=0)
    {
      btnUltimo.setActionCommand("");
      btnUltimo.setToolTipText("Ultimo");
      btnUltimo.addKeyListener(new navegador_btnUltimo_keyAdapter(this));
      btnUltimo.addActionListener(new navegador_btnUltimo_actionAdapter(this));
      btnUltimo.setMargin(new Insets(0, 0, 0, 0));
      btnUltimo.setMnemonic('U');
      this.add(btnUltimo, null);
   }


   if (incChose)
   {
     insBchose();
     this.add(btnChose, null);
     btnChose.setEnabled(true);
   }
   activarEventos();
  }
  /**
   * Establece modificadores del actionPerformed o KeyPerformed (ActionEvent.getModifiers)
   * @param modificadores 
   */
  void setModifiers(int modificadores)
  {
      this.modificadores=modificadores;
  }
  /**
   * Devuelve los modificadoes establecidos por el ultimo ActionPerformed o KeyPerformed
   * @return  modificadores 
   * @see ActionEvent.getModifiers()
   */
  public int getModifiers()
  {
      return modificadores;
  }
  void activarEventos()
  {

  }
  public void insBchose()
  {
    btnChose.setActionCommand("");
    btnChose.setToolTipText("Elegir Este Registro");
    btnChose.setMnemonic('E');
    btnChose.addKeyListener(new navegador_btnChose_keyAdapter(this));
    btnChose.addActionListener(new navegador_btnChose_actionAdapter(this));
    btnChose.setEnabled(false);
    btnChose.setMargin(new Insets(0, 0, 0, 0));
  }

  public void insBadd(CButton btnAdd)
  {
    btnAdd.setActionCommand("");
    btnAdd.setToolTipText("Añadir");
    btnAdd.setMnemonic('A');
    btnAdd.addKeyListener(new navegador_btnAddNew_keyAdapter(this));
    btnAdd.addActionListener(new navegador_btnAddNew_actionAdapter(this));
    btnAdd.setMargin(new Insets(0, 0, 0, 0));
  }

  public void insBedit()
  {
    btnEdit.setActionCommand("");
    btnEdit.setToolTipText("Modificar");
    btnEdit.setMnemonic('M');
    btnEdit.addKeyListener(new navegador_btnEdit_keyAdapter(this));
    btnEdit.addActionListener(new navegador_btnEdit_actionAdapter(this));
    btnEdit.setMargin(new Insets(0, 0, 0, 0));
  }

  //Devuelve true si estamos en el primer elemento del datosTabla
  public boolean isPrimero(){
    return dt.isFirst();
  }
  //Devuelve true si estamos en el ultimo elemento del datosTabla
  public boolean isUltimo(){
    return dt.isLast();
  }

  public void ponEnabled(boolean b)
  {
    super.setEnabled(b);
    btnAddNew.setEnabled(b);
    btnAnterior.setEnabled(b);
    btnChose.setEnabled(b);
    btnDelete.setEnabled(b);
    btnEdit.setEnabled(b);
    btnPrimero.setEnabled(b);
    btnAnterior.setEnabled(b);
    btnQuery.setEnabled(b);
    btnSiguiente.setEnabled(b);
    btnUltimo.setEnabled(b);
  }

  public void btnPrimero_actionPerformed()
  {
    if (swEvent)
      return;
    swEvent = true;
    if (pd != null)
      pd.mensaje("", false);
    Error = false;
    pulsado = PRIMERO;

    try
    {
      if (dt != null && Ejecutar)
      {
        this.setEnabled(false);
        if (pd != null)
          pd.mensaje("Buscando Primer Registro ... Espere, por Favor", false);
        dt.first();
        this.setEnabled(true);
        if (pd != null)
          pd.mensaje("", false);
        btnPrimero.setEnabled(false);
        btnAnterior.setEnabled(false);
        btnSiguiente.setEnabled(true);
        btnUltimo.setEnabled(true);
        if (dt.getError())
        {
          Error = true;
          MsgError = dt.getMsgError();
          if (pd != null)
            pd.mensaje(MsgError, true);
        }
      }
    }
    catch (Exception j)
    {
      Error = true;
      MsgError = "ERROR: " + j.getMessage();
      mensajes.mensajeUrgente(MsgError);
      SystemOut.print(j);
    }

    if (pd != null)
    {
      llame = true;
      pd.PADPrimero();
      llame = false;
    }
    else
      procesaPrimero();
    btnSiguiente.requestFocus();

    swEvent = false;

  }

  public void btnAnterior_actionPerformed()
  {
    if (swEvent)
      return;
    swEvent = true;
    if (pd != null)
      pd.mensaje("", false);
    pulsado = ANTERIOR;
    Error = false;

    if (dt != null && Ejecutar)
    {
      this.setEnabled(false);
      if (pd != null)
        pd.mensaje("Buscando Anterior Registro ... Espere, por Favor", false);
      try {
        dt.previous();
      } catch (java.sql.SQLException k)
      {
       SystemOut.print(k); // No se que hacer, la verdad.
      }
      this.setEnabled(true);
      if (pd != null)
        pd.mensaje("", false);
      if (dt.isFirst())
      {
        btnPrimero.setEnabled(false);
        btnAnterior.setEnabled(false);
        if (pd != null)
          pd.mensaje("Ya esta en el primer Registro", true);
      }
      btnUltimo.setEnabled(true);
      btnSiguiente.setEnabled(true);

      if (dt.getError())
      {
        Error = true;
        MsgError = dt.getMsgError();
        if (pd != null)
          pd.mensaje(MsgError, true);
      }
    }

    if (pd != null)
    {
      llame = true;
      pd.PADAnterior();

      llame = false;
    }
    else
      procesaAnt();
    if (dt!=null)
    {
      if (dt.isFirst())
        btnSiguiente.requestFocus();
      else
        btnAnterior.requestFocus();
    }
    swEvent = false;
  }

  void btnQuery_actionPerformed()
  {
    if (swEvent)
      return;

    ponEnabled(false);
    pulsado = QUERY;

    Error = false;
    if (pd != null)
    {
      pd.salirEnabled(false);
      llame = true;
      pd.PADQuery();
      llame = false;
    }

    if (dt != null && Ejecutar)
    {
//	    System.out.println("Realizar Query");
    }
//  	ponEnabled(true);
  }

  void btnEdit_actionPerformed()
  {
    if (swEvent)
      return;

    ponEnabled(false);
    pulsado = EDIT;

    Error = false;
    if (pd != null)
    {
      pd.salirEnabled(false);
      llame = true;
      pd.PADEdit();
      llame = false;
    }

  }

  void btnAddNew_actionPerformed()
  {
    if (swEvent)
      return;
    ponEnabled(false);
    pulsado = ADDNEW;

    Error = false;
    if (pd != null)
    {
      pd.salirEnabled(false);
      llame = true;
      pd.PADAddNew();
      llame = false;
    }

  }

  void btnDelete_actionPerformed()
  {
    if (swEvent)
      return;

    ponEnabled(false);
    pulsado = DELETE;

    Error = false;
    if (pd != null)
    {
      pd.salirEnabled(false);
      llame = true;
      pd.PADDelete();
      llame = false;
    }
  }

  void btnChose_actionPerformed()
  {
    if (swEvent)
      return;

//    ponEnabled(false);
    pulsado = CHOSE;

    Error = false;
    if (pd != null)
    {
//      pd.salirEnabled(false);
      llame = true;
      pd.PADChose();
      llame = false;
    }
  }


  public void btnSiguiente_actionPerformed()
  {
    if (swEvent)
      return;
    swEvent=true;
    if (pd!=null)
      pd.mensaje("", false);
    pulsado=SIGUIENTE;
    Error = false;
    if (dt != null && Ejecutar)
    {
      try
      {
        this.setEnabled(false);
        if (pd!=null)
           pd.mensaje("Buscando Siguiente Registro ... Espere, por Favor", false);
        dt.next();
        this.setEnabled(true);
        pd.mensaje("", false);
        btnPrimero.setEnabled(true);
        btnAnterior.setEnabled(true);

        if (dt.isLast())
        {
          Error = true;
          MsgError = dt.getMsgError();
          btnUltimo.setEnabled(false);
          btnSiguiente.setEnabled(false);
          pd.mensaje(MsgError, true);
        }
      }
      catch (Exception j)
      {
        Error = true;
        MsgError = "ERROR: " + j.getMessage();
        mensajes.mensajeUrgente(MsgError);
        SystemOut.print(j);
      }
    }

    if (pd != null)
    {
      llame = true;
      pd.PADSiguiente();
      llame = false;
    }
    else
      procesaSigu();
    if (dt != null)
    {
      if (dt.isLast())
        btnAnterior.requestFocus();
      else
        btnSiguiente.requestFocus();
    }
    swEvent=false;
  }

  public void btnUltimo_actionPerformed()
  {
    if (swEvent)
      return;
    swEvent = true;
    if (pd != null)
      pd.mensaje("", false);
    pulsado = ULTIMO;
    Error = false;

    try
    {
      if (dt != null && Ejecutar)
      {
        this.setEnabled(false);
        if (pd != null)
          pd.mensaje("Buscando Ultimo Registro ... Espere, por Favor", false);
        dt.last();
        this.setEnabled(true);
        if (pd != null)
          pd.mensaje("", false);
        if (!dt.getNOREG())
        {
          btnPrimero.setEnabled(true);
          btnAnterior.setEnabled(true);
          btnSiguiente.setEnabled(false);
          btnUltimo.setEnabled(false);
        }
        if (dt.getError())
        {
          Error = true;
          MsgError = dt.getMsgError();
          if (pd != null)
            pd.mensaje(MsgError, true);
        }
      }
    }
    catch (Exception j)
    {
      Error = true;
      MsgError = "ERROR: " + j.getMessage();
      mensajes.mensajeUrgente(MsgError);
      SystemOut.print(j);
    }
    if (pd != null)
    {
      llame = true;
      pd.PADUltimo();
      llame = false;
    }
    else
      procesaUltimo();
    btnAnterior.requestFocus();
    swEvent = false;
  }

  protected void procesaAnt()  {}

  protected void procesaSigu()  {}

  protected void procesaPrimero()  {}

  protected void procesaUltimo()  {}

  void this_keyPressed(KeyEvent e)
  {
    setModifiers(e.getModifiers());
    switch (e.getKeyCode())
    {
      case KeyEvent.VK_UP: // Primero
        if (!addBotones[PRIMERO])
          break;
        if (modo != NORMAL && modo != SOLCUR && modo != CURYCON)
          break;
        if (btnPrimero.isEnabled())
          btnPrimero.doClick();
        break;
      case KeyEvent.VK_LEFT: // Anterior
        if (!addBotones[ANTERIOR])
          break;
        if (modo != NORMAL && modo != SOLCUR && modo != CURYCON)
          break;
        if (btnAnterior.isEnabled())
          btnAnterior.doClick();
        break;
      case KeyEvent.VK_Q: // Query
        if (!addBotones[QUERY])
          break;
        if (modo != CONSU && modo != NORMAL && modo != CURYCON)
          break;
        if (btnQuery.isEnabled())
          btnQuery.doClick();
        break;
      case KeyEvent.VK_S: // Seleccionar
        if (!addBotones[CHOSE])
          break;
        if (!incChose)
          break;
        if (btnChose.isEnabled())
          btnChose.doClick();
        break;
      case KeyEvent.VK_E: // Editar
        if (!addBotones[EDIT])
          break;
        if (btnEdit.isEnabled())
          btnEdit.doClick();
        break;
      case KeyEvent.VK_A: // Añadir
        if (!addBotones[ADDNEW])
          break;
        if (btnAddNew.isEnabled())
          btnAddNew.doClick();
        break;
      case KeyEvent.VK_D: // Eliminar
        if (!addBotones[DELETE])
          break;
        if (btnDelete.isEnabled())
          btnDelete.doClick();
        break;
      case KeyEvent.VK_RIGHT: // Siguiente
        if (!addBotones[SIGUIENTE])
          break;
        if (modo != NORMAL && modo != SOLCUR && modo != CURYCON)
          break;
        if (btnSiguiente.isEnabled())
          btnSiguiente.doClick();
        break;
      case KeyEvent.VK_DOWN: // Ultimo
        if (!addBotones[ULTIMO])
          break;
        if (modo != NORMAL && modo != SOLCUR && modo != CURYCON)
          break;
        if (btnUltimo.isEnabled())
          btnUltimo.doClick();
        break;
    }
  }

  void btnPrimero_keyPressed(KeyEvent e) {
		this_keyPressed(e);
  }

  void btnAnterior_keyPressed(KeyEvent e) {
		this_keyPressed(e);
  }

  void btnQuery_keyPressed(KeyEvent e) {
		this_keyPressed(e);
  }

  void btnEdit_keyPressed(KeyEvent e) {
		this_keyPressed(e);
  }

  void btnAddNew_keyPressed(KeyEvent e) {
		this_keyPressed(e);
  }

  void btnDelete_keyPressed(KeyEvent e) {
		this_keyPressed(e);
  }

  void btnSiguiente_keyPressed(KeyEvent e) {
		this_keyPressed(e);
  }

  void btnUltimo_keyPressed(KeyEvent e) {
		this_keyPressed(e);
  }

  void btnChose_keyPressed(KeyEvent e)
  {
    this_keyPressed(e);
  }

  public void setDatosTabla(DatosTabla tabla) {
  	dt = tabla;
  }

  public void setPAD(PAD p) {
  	pd = p;
  }

  public boolean getError() {
  	return Error;
  }

  public String getMsgError() {
  	return MsgError;
  }

    @Override
  public void requestFocus() {
    if (btnPrimero.isEnabled())
      btnPrimero.requestFocus();
    else if (btnAnterior.isEnabled())
      btnAnterior.requestFocus();
    else if (btnQuery.isEnabled())
      btnQuery.requestFocus();
    else if (btnEdit.isEnabled())
      btnEdit.requestFocus();
    else if (btnAddNew.isEnabled())
      btnAddNew.requestFocus();
    else if (btnDelete.isEnabled())
      btnDelete.requestFocus();
    else if (btnSiguiente.isEnabled())
      btnSiguiente.requestFocus();
    else if (btnUltimo.isEnabled())
      btnUltimo.requestFocus();
    else
      super.requestFocus();
  }
  public void setEnabled(int p1, boolean p2){
    super.setEnabled(true);
  	switch (p1) {
    	case PRIMERO:
      	btnPrimero.setEnabled(p2);
      	break;
      case ANTERIOR:
      	btnAnterior.setEnabled(p2);
      	break;
      case QUERY:
      	btnQuery.setEnabled(p2);
      	break;
      case EDIT:
      	btnEdit.setEnabled(p2);
      	break;
      case ADDNEW:
      	btnAddNew.setEnabled(p2);
      	break;
      case DELETE:
      	btnDelete.setEnabled(p2);
      	break;
      case SIGUIENTE:
      	btnSiguiente.setEnabled(p2);
      	break;
      case ULTIMO:
      	btnUltimo.setEnabled(p2);
      	break;
      case CHOSE:
      	btnChose.setEnabled(p2);
      	break;
      case TODOS:
        ponEnabled(p2);
        default:
      	break;
    }
  }
  public void removeBoton(int boton) {
  	switch (boton) {
    	case PRIMERO:
        addBotones[PRIMERO] = false;
      	remove(btnPrimero);
      	break;
      case ANTERIOR:
        addBotones[ANTERIOR] = false;
      	remove(btnAnterior);
      	break;
      case QUERY:
        addBotones[QUERY] = false;
      	remove(btnQuery);
      	break;
      case EDIT:
        addBotones[EDIT] = false;
      	remove(btnEdit);
      	break;
      case ADDNEW:
        addBotones[ADDNEW] = false;
      	remove(btnAddNew);
      	break;
      case DELETE:
        addBotones[DELETE] = false;
      	remove(btnDelete);
      	break;
      case SIGUIENTE:
        addBotones[SIGUIENTE] = false;
      	remove(btnSiguiente);
      	break;
      case ULTIMO:
        addBotones[ULTIMO] = false;
      	remove(btnUltimo);
      	break;
      case CHOSE:
        addBotones[CHOSE] = false;
      	remove(btnChose);
      	break;
			default:
      	break;
    }
  }
  public int getPulsado() {
  	return pulsado;
  }

  public void setPulsado(int i) {
  	pulsado= i;
  }


  public boolean getIncChose()
  {
    return incChose;
  }

  public int getModo()
  {
    return modo;
  }
  /**
  *  Devuelve true si ha ejecutado una llamda a una funcion de PAD
  */
  public boolean getLlame()
  {
    return llame;
  }

  public void setLlame(boolean llamo)
  {
    llamo=llame;
  }
  /**
   * Devuelve si esta en ediccion
   * (EDIT, ADDNEW, DELETE o QUERY)
   * @return 
   */
  public boolean isEdicion()
  {
    return  pulsado == EDIT || pulsado == ADDNEW ||
            pulsado==DELETE  || pulsado==QUERY;
  }
} // Fin de Clase.

class navegador_btnPrimero_actionAdapter implements java.awt.event.ActionListener
{
  navegador adaptee;

  navegador_btnPrimero_actionAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.setModifiers(e.getModifiers()); 
    adaptee.btnPrimero_actionPerformed();
  }
}

class navegador_btnAnterior_actionAdapter implements java.awt.event.ActionListener
{
  navegador adaptee;

  navegador_btnAnterior_actionAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.setModifiers(e.getModifiers()); 
    adaptee.btnAnterior_actionPerformed();  }

}

class navegador_btnQuery_actionAdapter implements java.awt.event.ActionListener{
  navegador adaptee;

  navegador_btnQuery_actionAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.setModifiers(e.getModifiers()); 
    adaptee.btnQuery_actionPerformed();
  }
}

class navegador_btnEdit_actionAdapter implements java.awt.event.ActionListener{
  navegador adaptee;

  navegador_btnEdit_actionAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.setModifiers(e.getModifiers()); 
    adaptee.btnEdit_actionPerformed();
  }
}

class navegador_btnAddNew_actionAdapter implements java.awt.event.ActionListener{
  navegador adaptee;

  navegador_btnAddNew_actionAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.setModifiers(e.getModifiers()); 
    adaptee.btnAddNew_actionPerformed();
  }
}

class navegador_btnDelete_actionAdapter implements java.awt.event.ActionListener{
  navegador adaptee;

  navegador_btnDelete_actionAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {      
    adaptee.setModifiers(e.getModifiers()); 
    adaptee.btnDelete_actionPerformed();
  }
}

class navegador_btnSiguiente_actionAdapter implements java.awt.event.ActionListener     {
  navegador adaptee;

  navegador_btnSiguiente_actionAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
      adaptee.setModifiers(e.getModifiers()); 
      adaptee.btnSiguiente_actionPerformed();
  }
}

class navegador_btnUltimo_actionAdapter implements java.awt.event.ActionListener
{
  navegador adaptee;

  navegador_btnUltimo_actionAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.setModifiers(e.getModifiers()); 
    adaptee.btnUltimo_actionPerformed();
  }
}

class navegador_btnChose_actionAdapter implements java.awt.event.ActionListener{
  navegador adaptee;

  navegador_btnChose_actionAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.setModifiers(e.getModifiers()); 
    adaptee.btnChose_actionPerformed();
  }
}


class navegador_this_keyAdapter extends java.awt.event.KeyAdapter {
  navegador adaptee;
  KeyPress keypress;

  navegador_this_keyAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e)
  {
  	keypress = new KeyPress(adaptee, e);
  }
}

class navegador_btnPrimero_keyAdapter extends java.awt.event.KeyAdapter {
  navegador adaptee;
  KeyPress keypress;

  navegador_btnPrimero_keyAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e) {
  	keypress = new KeyPress(adaptee, e);
  }
}

class navegador_btnAnterior_keyAdapter extends java.awt.event.KeyAdapter {
  navegador adaptee;
  KeyPress keypress;

  navegador_btnAnterior_keyAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e) {
  	keypress = new KeyPress(adaptee, e);
  }
}

class navegador_btnQuery_keyAdapter extends java.awt.event.KeyAdapter {
  navegador adaptee;
  KeyPress keypress;

  navegador_btnQuery_keyAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e) {
  	keypress = new KeyPress(adaptee, e);
  }
}

class navegador_btnEdit_keyAdapter extends java.awt.event.KeyAdapter {
  navegador adaptee;
  KeyPress keypress;

  navegador_btnEdit_keyAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e) {
  	keypress = new KeyPress(adaptee, e);
  }
}

class navegador_btnAddNew_keyAdapter extends java.awt.event.KeyAdapter {
  navegador adaptee;
  KeyPress keypress;

  navegador_btnAddNew_keyAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e) {
  	keypress = new KeyPress(adaptee, e);
  }
}

class navegador_btnDelete_keyAdapter extends java.awt.event.KeyAdapter {
  navegador adaptee;
  KeyPress keypress;

  navegador_btnDelete_keyAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e) {
  	keypress = new KeyPress(adaptee, e);
  }
}

class navegador_btnSiguiente_keyAdapter extends java.awt.event.KeyAdapter {
  navegador adaptee;
  KeyPress keypress;

  navegador_btnSiguiente_keyAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e) {
  	keypress = new KeyPress(adaptee, e);
  }
}

class navegador_btnUltimo_keyAdapter extends java.awt.event.KeyAdapter {
  navegador adaptee;
  KeyPress keypress;

  navegador_btnUltimo_keyAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e) {
  	keypress = new KeyPress(adaptee, e);
  }
}

class navegador_btnChose_keyAdapter extends java.awt.event.KeyAdapter {
  navegador adaptee;
  KeyPress keypress;

  navegador_btnChose_keyAdapter(navegador adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e) {
  	keypress = new KeyPress(adaptee, e);
  }
}

class KeyPress   extends java.awt.event.KeyAdapter
{
  navegador adaptee;
  KeyEvent key;

  KeyPress(navegador adaptee, KeyEvent e)
  {
    this.adaptee = adaptee;
    key = e;
    adaptee.this_keyPressed(key);
  }
}

