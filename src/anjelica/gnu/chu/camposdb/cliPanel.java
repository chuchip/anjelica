package gnu.chu.camposdb;

import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gnu.chu.controles.*;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import gnu.chu.winayu.*;

/**
 *
 * <p>Título: cliPanel</p>
 * <p>Descripción: Panel para introducir el codigo del cliente</p>
 * <p>Copyright: Copyright (c) 2005
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los t�rminos de la Licencia P�blica General de GNU seg�n es publicada por
 *  la Free Software Foundation, bien de la versi�n 2 de dicha Licencia
 *  o bien (seg�n su elecci�n) de cualquier versi�n posterior.
 *  Este programa se distribuye con la esperanza de que sea �til,
 *  pero SIN NINGUNA GARANT�A, incluso sin la garant�a MERCANTIL impl�cita
 *  o sin garantizar la CONVENIENCIA PARA UN PROP�SITO PARTICULAR.
 *  V�ase la Licencia P�blica General de GNU para m�s detalles.
 *  Deber�a haber recibido una copia de la Licencia P�blica General junto con este programa.
 *  Si no ha sido as�, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 1.0
 */
public class cliPanel extends CPanel
{
  private Integer peso=new Integer(1);
  private boolean swControl=true;
  ayucli aycli;
  String zona=null;
  boolean evQuery=true;
  CInternalFrame intfr = null;
  EntornoUsuario eu;
  DatosTabla dt;
  boolean  mascaraDB=false;
  boolean actRep=true;
  public CTextField cli_codiE= new CTextField(Types.DECIMAL,"#####9");
  public CTextField cli_nombL= new CTextField();
  public CLabel cli_nomcL=new CLabel();
  JLayeredPane vl;
  boolean eje;
  CInternalFrame infr;
  String nomComercial="";   // Variable auxiliar para dejar el nombre comercial
  boolean nComercial=false; // Variable que me indica si quiero poner el nombre comercial

  private boolean botonConsultar = true;
  boolean Error = false;
  String MsgError = "";

  public final static int DBSKIP = 1;
  public final static int COMPROBAR = 2;
  public final static int LOSTFOCUS = 3;
  public final static int CHECK_NOTEXT = 4;
  final static String NOEXISTE = "REGISTRO NO ENCONTRADO";  // para cuando no selocaliza un registro

  private String copia = "";

  private vlike lk = new vlike();
  CButton Bcons = new CButton(Iconos.getImageIcon("find"));
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  boolean aceptaNulo=false;

  public cliPanel() {
    try  {
      jbInit();
    }
    catch (Exception ex) {
      SystemOut.print(ex);
    }
  }

  public cliPanel(DatosTabla dt, int empresa) {
    this();
//    setMascaraDB(dt, empresa);
  }

  //Constructores para indicar que se quiere nombre comercial
  public cliPanel(boolean nomComer) {
    nComercial=nomComer;
    try  {
      jbInit();
    }
    catch (Exception ex) {
      SystemOut.print(ex);
    }
  }
  protected void finalize() throws Throwable { }
  {
    if (aycli!=null)
      aycli.dispose();
    aycli=null;
  }
  public void setMsgError(String msgErr)
  {
    MsgError=msgErr;
  }
  public void setError(boolean error)
  {
    Error=error;
  }
  public cliPanel(DatosTabla dt, int empresa, boolean nomComer) {
    nComercial=nomComer;
    try  {
      jbInit();
    }
    catch (Exception ex) {
      SystemOut.print(ex);
    }
//    setMascaraDB(dt, empresa);
  }

  void jbInit() throws Exception {

    this.setLayout(gridBagLayout1);
    cli_nombL.setBackground(Color.orange);
    cli_nombL.setForeground(Color.black);
    cli_nombL.setMaximumSize(new Dimension(313, 18));
    cli_nombL.setMinimumSize(new Dimension(313, 18));
    cli_nombL.setOpaque(true);
    cli_nombL.setEnabled(false);
    cli_nombL.setPreferredSize(new Dimension(313, 18));
    cli_nomcL.setBackground(new java.awt.Color(255, 255, 227));
    cli_nomcL.setOpaque(true);
    cli_nomcL.setPreferredSize(new Dimension(30, 17));
    Bcons.setFocusable(false);

    Bcons.setMaximumSize(new Dimension(22, 18));
    Bcons.setMinimumSize(new Dimension(22, 18));
    Bcons.setPreferredSize(new Dimension(22, 18));
    Bcons.setMargin(new Insets(0, 0, 0, 0));
    cli_codiE.setMaximumSize(new Dimension(50, 18));
    cli_codiE.setMinimumSize(new Dimension(50, 18));
    cli_codiE.setPreferredSize(new Dimension(50, 18));
    cli_codiE.setQuery(false);
    this.add(cli_codiE,      new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 2), 0, 0));
    this.add(cli_nombL,         new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
    this.add(Bcons,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    if (nComercial){
        this.add(cli_nomcL, new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(1, 0, 0, 0), 0, 0));
    }
  }

  public void setAceptaNulo(boolean acepNulo)
  {
    aceptaNulo=acepNulo;
  }
  public boolean getAceptaNulo()
  {
    return aceptaNulo;
  }
  /**
   * Hace el boton de consultar visible
   * @param activo true = visible
   */
  public void setBotonConsultar(boolean activo) {
    Bcons.setVisible(activo);
    botonConsultar=activo;
  }
  public boolean getBotonConsultar() {
    return botonConsultar;
  }
  public void requestFocus()
  {
    super.requestFocus();
    cli_codiE.requestFocus();
  }
  /**
   * Usado para cuando se añade la ventana (internal frame) de consulta de clientes
   * especifica el peso.
   * @param peso
   */
  public void setPeso(Integer peso)
  {
      this.peso=peso;
  }
  private void activarEventos()
  {
    Bcons.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        consCli();
      }
    });
    if (swControl)
    {

      cli_codiE.addFocusListener(new FocusListener()
      {
        public void focusGained(FocusEvent e)
        {}

        public void focusLost(FocusEvent e)
        {
          if (getQuery())
          {
            return;
          }
          try
          {
            if (cli_codiE.hasCambio())
            {
              cli_codiE.resetCambio();
              afterFocusLost(!controlar(LOSTFOCUS));
            }
          }
          catch (Exception k)
          {}
        }
      });
    }
    cli_codiE.addKeyListener(new KeyAdapter()
    {
            @Override
      public void keyPressed(KeyEvent e)
      {
        if (botonConsultar && (e.getKeyCode() == KeyEvent.VK_F3 ||
            (e.getKeyChar()=='3' && e.getModifiers()==KeyEvent.CTRL_MASK)) )
        {
          consCli();
        }

      }
    });
  }
  /**
   * Devuelve la tarifa de un cliente
   * @return int
   * @throws SQLException Error en la base de datos
   */
  public int getTarifa() throws SQLException
  {
    return lk.getInt("tar_codi");
  }
  /**
   * Machacar para controlar que hacer despues de un focus lost.
   * Esta funcion es llamada despues de un focusLost, si ha habido cambio en el
   * codigo de cliente y despues de haber llamado a controlar
   * @see controlar()
   * @param noError boolean Recibe el resultado de la funcion controlar
   *              true significa que NO ha habido error
   *              false que hay error
   *
   */
  protected void afterFocusLost(boolean noError)
  {

  }
  public boolean controlar() throws SQLException
  {
    return controlar(COMPROBAR);
  }
  public boolean hasError()
  {
    return Error;
  }
  public boolean controlar(int caso) throws SQLException
  {
    return controlar(caso,null);
  }
  public boolean controlar(int caso,String cliNomb) throws SQLException
  {
    String s;
    MsgError = "";
    Error=! checkCond();
    return controlar1(caso,cliNomb);
  }

  boolean controlar1(int caso, String cliNomb) throws  SQLException
  {
    if (!Error && cli_codiE.getValorInt()!=0)
      Error = getNombCliente(dt, cli_codiE.getValorInt(), zona) == null;

    if (Error)
    {
      switch (caso)
      {
        case COMPROBAR:
        case CHECK_NOTEXT:
          cli_codiE.requestFocus();
        case LOSTFOCUS:
        case DBSKIP:
          if (cli_nombL !=null)
             cli_nombL.setText(NOEXISTE);
          setNomComercial("" + NOEXISTE);
          break;
      }
      Error = true;
      MsgError = "Cliente no Existe";
      return false;
    }
    else
    {
      if (caso!=CHECK_NOTEXT)
      {
        if (cli_codiE.getValorInt()==0)
        {
          if (cli_nombL !=null)
            cli_nombL.setText("");
          setNomComercial("");
        }
        else
        {
          if (cli_nombL != null)
            cli_nombL.setText(cliNomb == null || dt.getInt("cli_gener") == 0 ?
                              dt.getString("cli_nomb") :
                              cliNomb);
          setNomComercial(dt.getString("cli_nomco"));
        }
      }
    }
    return true;
  }

  /**
   * Comprueba condiciones Iniciales.
   * @return boolean false ERROR. true = Correcto
   */
  boolean checkCond()
  {
    if (dt == null)
    {
      MsgError = "Base de Datos no Inicializada";
      return false;
    }

    if (cli_codiE.getValorInt() == 0)
    {
      if (!aceptaNulo)
      {
        MsgError = "Introduzca codigo de cliente";
        return false;
      }
    }
    return true;
  }

  public String getNombCliente(DatosTabla dt,int cliCodi) throws SQLException
  {
    return getNombCliente(dt,cliCodi,zona);
  }
  public String getNombCliente(DatosTabla dt,int cliCodi, String zona) throws SQLException
  {
    String s = "SELECT * from clientes WHERE cli_codi = '" + cliCodi +"'"+
          (zona==null?"":" AND cli_zonrep LIKE '"+zona+"'");
    if (! dt.selectInto(s,lk))
      return null;
    return dt.getString("cli_nomb");
  }
//  public boolean ejec_sele(String s)
//  {
//    try
//    {
//      dt.select(s, false);
//      lk.setDatosTabla(dt);
//      if (dt.getNOREG())
//        return false;
//      return true;
//    }
//    catch (Exception k)
//    {
//      Error = true;
//      MsgError = "SELECT: " + dt.getMsgError();
//      return false;
//    }
//  }

  public vlike getLikeCliente() { return lk; }


/*
  public void setConectaDB(DatosTabla dt, String campoDB)
  {
    cli_codiE.setConectaDB(dt, campoDB);

		dt.addDBListener(new DBListener() {
    	public void DBCambio(DBEvent event) {}
      public void DBSkip(DBEvent Event) {
	  		controlar(DBSKIP);
      }
      public void DBOtro(DBEvent Event){}
    });
  }
*/
/**
 * Solo dara como Validos los clientes de esta zona.
 * Si es NULL dara como validos TODOS (Por defecto)
 * @param zonCli String
 */
public void setZona(String zonCli)
  {
    zona=zonCli;
  }
  public void iniciar(DatosTabla datTabla, CInternalFrame intFrame,
                    JLayeredPane layPan, EntornoUsuario entUsu) throws SQLException
   {
     intfr = intFrame;
     dt = datTabla;
     vl = layPan;
     eu = entUsu;
//    pro_codiE.setMascaraDB(dt,eu.em_cod);
     activarEventos();
     setText("");

     if (swControl)
     {
       setText("");
       controlar();
     }
  }

  public void setColumnaAlias(String colAlias)
  {
    cli_codiE.setColumnaAlias(colAlias);
  }
  public String getColumnaAlias()
  {
    return cli_codiE.getColumnaAlias();
  }
  public String getMsgError() {
    return MsgError;
  }
  public void setText(String texto) {

    cli_codiE.setText(texto);
    if (! swControl)
        return; // Pasa de buscar y poner el nombre del producto.
    try {
      controlar(DBSKIP);
    } catch (Exception k)
    {

    }
    cli_codiE.resetCambio();
  }

  public void setControla(boolean swControla)
  {
    swControl = swControla;
  }

  public String getText() {
    return cli_codiE.getText();
  }
  public String getTextNomb() {
    return cli_nombL.getText();
  }
 /**
  * Devuelve True si el Texto introducido es Nulo
  * @return isNull boolean
  */
  public boolean isNull()
  {
     return cli_codiE.isNull();
  }
  public void setCeroIsNull(boolean ceroIsNull)
  {
    cli_codiE.setCeroIsNull(ceroIsNull);
  }

  public void setEnabled(boolean activo) {
    cli_codiE.setEnabled(activo);
    Bcons.setEnabled(activo);
  }
  public boolean getEnabled() {
    return cli_codiE.isEnabled();
  }

  public void setQuery(boolean modoQuery) {
    cli_codiE.setQuery(modoQuery);
    Bcons.setEnabled(!modoQuery);
  }
  public boolean getQuery() {
    return cli_codiE.isQuery();
  }

  public String getStrQuery() {
    return cli_codiE.getStrQuery();
  }

  public void consCli()
  {
    consCli(null);
  }
  public void consCli(String nombCli)
  {
      try
      {
        if (aycli==null)
        {
          aycli = new ayucli(eu, vl, dt)
          {
            @Override
            public void matar()
            {
               ej_consCli(aycli);
            }
          };
          aycli.setLocation(25, 25);
          if (intfr!=null)
            intfr.getLayeredPane().add(aycli,1);
          else
            vl.add(aycli,peso);
        }
        aycli.setVisible(true);
        if (intfr!=null)
        {
          intfr.setEnabled(false);
          intfr.setFoco(aycli);
        }
        aycli.setZona(zona);
        aycli.iniciarVentana();
        if (nombCli!=null)
        {
          aycli.cli_nombE.setText(nombCli);
          aycli.Bbuscar_actionPerformed();
        }
      }
      catch (Exception j)
      {
        SystemOut.print(j);
        if (intfr != null)
          intfr.setEnabled(true);
      }

  }

  void ej_consCli(ayucli aycli)
  {
    evQuery=false;
    if (aycli.consulta)
    {
      cli_codiE.setText(aycli.cli_codiE);
       if (swControl)
         cli_nombL.setText(aycli.cli_nombE.getText());
      setNomComercial(aycli.cli_nombcE.getText());
    }
    cli_codiE.requestFocus();
    aycli.setVisible(false);

    if (intfr != null)
    {
      intfr.setEnabled(true);
      intfr.toFront();
      try
      {
        intfr.setSelected(true);
      }
      catch (Exception k)
      {}
      intfr.setFoco(null);
      this.requestFocus();
      evQuery=true;
    }
  }
  /**
    *
    * @param pro_nombE CTextField poner a NULL si no se quiere actualizar el nombre
    *                  del producto.
    */
   public void setCliNomb(CTextField cli_nombE)
   {
     if (cli_nombE==null)
       swControl = false;
     else
       swControl = true;

     this.remove(cli_nombL);
     this.remove(Bcons);
     if (!swControl)
       return;
     this.remove(cli_codiE);
     this.add(cli_codiE, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));

     cli_nombL=cli_nombE;
   }

  public String getNomComercial(){
    return cli_nomcL.getText();
  }

  public void setNomComercial(String nom){
    cli_nomcL.setText(nom);
  }
  public void resetTexto(){
    super.resetTexto();
    cli_nombL.setText("");
    cli_codiE.resetCambio();
  }
  public boolean hasCambio() {
         if (copia.equals(getText()))
            return false;
         else
             return true;
  }
  public void resetCambio() {
         copia = getText();
  }
  public String getValorOld() {
    return copia;
  }
  public int getValorInt()
  {
    return cli_codiE.getValorInt();
  }

  public void setValorInt(int valor)
  {
    cli_codiE.setValorDec(valor);
    try
    {
      controlar(DBSKIP);
    }
    catch (Exception k)
    {

    }
    cli_codiE.resetCambio();

  }

  public String getValorAct() {
    return getText();
  }

}  // Final de Class



