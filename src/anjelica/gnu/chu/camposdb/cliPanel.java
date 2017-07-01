package gnu.chu.camposdb;

import gnu.chu.Menu.Principal;
import gnu.chu.anjelica.pad.pdclien;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gnu.chu.controles.*;
import gnu.chu.interfaces.ejecutable;
import gnu.chu.sql.*;
import gnu.chu.utilidades.*;
import gnu.chu.winayu.*;

/**
 *
 * <p>Título: cliPanel</p>
 * <p>Descripción: Panel para introducir el codigo del cliente</p>
 * <p>Copyright: Copyright (c) 2005-2016
 *  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
 *  los términos de la Licencia Pública General de GNU según es publicada por
 *  la Free Software Foundation, bien de la versión 2 de dicha Licencia
 *  o bien (según su elección) de cualquier versión posterior.
 *  Este programa se distribuye con la esperanza de que sea útil,
 *  pero SIN NINGUNA GARANTÍA, incluso sin la garant�a MERCANTIL impl�cita
 *  o sin garantizar la CONVENIENCIA PARA UN PROPOSITO PARTICULAR.
 *  Vease la Licencia P�blica General de GNU para más detalles.
 *  Debería haber recibido una copia de la Licencia Pública General junto con este programa.
 *  Si no ha sido así, escriba a la Free Software Foundation, Inc.,
 *  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
 * </p>
 * <p>Empresa: MISL</p>
 * @author chuchiP
 * @version 2.0
 */
public class cliPanel extends CPanel
{
  public final static int SERVIR_NO=0;
  public final  static int SERVIR_SI=1;
  public final  static int SERVIR_NO_FORZADO=2;
  public final  static int SERVIR_INCFRA=3;
  Principal jf;
  private Integer peso=1;
  private boolean swControl=true;
  ayuClientes ayucli;
  String zona=null;
  boolean evQuery=true;
  CInternalFrame intfr = null;
  EntornoUsuario eu;
  DatosTabla dt;
  boolean  mascaraDB=false;
  boolean actRep=true;
  public CTextField cli_codiE= new CTextField(Types.DECIMAL,"#####9");
  private CTextField cli_nombE= new CTextField();
  private CLabel cli_codrepL= new CLabel();
  public CLabel cli_nomcL=new CLabel();
  JLayeredPane vl;
  boolean eje;
  CInternalFrame infr;
  String nomComercial="";   // Variable auxiliar para dejar el nombre comercial
  boolean nComercial=false; // Variable que me indica si quiero poner el nombre comercial

  private boolean botonConsultar = true;
  private boolean  swMostrarCodigoReparto=false;
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
  
  @Override
  protected void finalize() throws Throwable 
  {
    if (ayucli!=null)
      ayucli.dispose();
    ayucli=null;
    super.finalize();
  }
  
  
  public void iniciar(Principal principal)
  {
      if (principal==null)
          return;
      jf=principal;
      cli_codiE.addMouseListener(new MouseAdapter()
      {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (!cli_codiE.isNull() && e.getClickCount()>1)
                llamaMantClientes();
        }
      }); 
  }
  void llamaMantClientes()
  {
        ejecutable prog;
        if ((prog=jf.gestor.getProceso(pdclien.getNombreClase()))==null)
               return;
       pdclien cm=(pdclien) prog;
       if (cm.inTransation())
       {
          mensajes.mensajeAviso("Mantenimiento Clientes ocupado. No se puede realizar la busqueda");
          return;
       }
       cm.PADQuery();

       cm.setCliente(cli_codiE.getValorInt());
       cm.ej_query();
       jf.gestor.ir(cm);
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
    cli_codrepL.setBackground(Color.orange);
    cli_codrepL.setForeground(Color.black);
    cli_codrepL.setMaximumSize(new Dimension(35, 18));
    cli_codrepL.setMinimumSize(new Dimension(35, 18));
    cli_codrepL.setPreferredSize(new Dimension(35, 18));
    cli_codrepL.setOpaque(true);
    cli_codrepL.setVisible(false);
    cli_nombE.setBackground(Color.orange);
    cli_nombE.setForeground(Color.black);
    cli_nombE.setMaximumSize(new Dimension(313, 18));
    cli_nombE.setMinimumSize(new Dimension(313, 18));
    cli_nombE.setOpaque(true);
    cli_nombE.setEnabled(false);
    cli_nombE.setPreferredSize(new Dimension(313, 18));
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
    this.add(cli_nombE,         new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 2), 0, 0));
    this.add(Bcons,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    this.add(cli_codrepL,     new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
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
  public boolean isActivo() throws SQLException
  {
      return getLikeCliente().getString("cli_activ").toUpperCase().startsWith("S")
          && !isNoServirForzado();
  }
  /**
   * Devuelve si a un cliente se le puede servir o no.
   * @return true si se le puede servir 
   * @throws SQLException 
   */
  public boolean isServir() throws SQLException
  {
      return getLikeCliente().getInt("cli_servir")==SERVIR_NO;
  }
  public int getEstadoServir() throws SQLException
  {
       return getLikeCliente().getInt("cli_servir");
  }
  /**
   * 
   * @return true si esta marcado como incluir factura
   * @throws SQLException 
   */
   public boolean isIncluirFra() throws SQLException
  {
      return getLikeCliente().getInt("cli_servir")==SERVIR_INCFRA;
  }
  /**
   * Devuelve si a un cliente se le puede servir o no.
   * @return true si no se le debe ni cargar pedidos.
   * @throws SQLException 
   */
  public boolean isNoServirForzado() throws SQLException
  {
      return getLikeCliente().getInt("cli_servir")==SERVIR_NO_FORZADO;
  }
  /**
   * Hace el boton de consultar visible
   * @param activo true = visible
   */
  public void setBotonConsultar(boolean activo) {
    Bcons.setVisible(activo);
    botonConsultar=activo;
  }
   /**
   * Hace el Campo Codigo de Reparto visible
   * Por Defecto es invisible
   * @param activo true = visible
   */
  public void setCampoReparto(boolean activo)
  {
    cli_codrepL.setVisible(activo);
    swMostrarCodigoReparto=activo;
  }
  
  public boolean getCampoReparto()
  {
       return swMostrarCodigoReparto;
  }
  public boolean getBotonConsultar() {
    return botonConsultar;
  }
  @Override
  public void requestFocus()
  {
//    super.requestFocus();
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
      @Override
      public void actionPerformed(ActionEvent e)
      {
        consCli();
      }
    });
    if (swControl)
    {

      cli_codiE.addFocusListener(new FocusAdapter()
      {

        @Override
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
  /**
   * Funcion a llmar para controlar si el estado del campo es correcto
   * @return False si hay algun tipo de error. Si hay error realiza un requestfocus al campo
   * @see    getMsgError() {
   * @throws SQLException 
   */
  public boolean controlar() throws SQLException
  {
    return controlar(COMPROBAR);
  }
  public boolean hasError()
  {
    return Error;
  }
  /**
   * 
   * Funcion a llmar para controlar si el estado del campo es correcto   
   * @see    getMsgError() {  
   * @param caso Que hacer en caso de error. 
   * Si es COMPROBAR realizara un requestfocus. Si es LOSTFOCUS no.
   * @return False si hay algun tipo de error. 
   * @throws SQLException 
   */
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
  
  public CTextField getCampoCiente()
  {
      return cli_codiE;
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
          cli_codrepL.setText("");
          if (cli_nombE !=null)
             cli_nombE.setText(NOEXISTE);
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
          cli_codrepL.setText("");
          if (cli_nombE !=null)
            cli_nombE.setText("");
          setNomComercial("");
        }
        else
        {
          if (cli_nombE != null)
          {
            cli_nombE.setText(cliNomb == null || dt.getInt("cli_gener") == 0 ?
                              dt.getString("cli_nomb"):
                              cliNomb);
            cli_codrepL.setText(dt.getString("cli_codrut",true));
          }
          
          setNomComercial(dt.getString("cli_nomco"));
        }
      }
    }
    return true;
  }
  
  public CTextField getFieldCliNomb()
  {
    return cli_nombE;
  }
  public String getCodigoReparto()
  {
    return cli_codrepL.getText();
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
  
  public CTextField getCampoNombreCliente()
  {
      return cli_nombE;
  }
  public void setNombreCliente(String nombre)
  {
      cli_nombE.setText(nombre);
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
  @Override
  public String getColumnaAlias()
  {
    return cli_codiE.getColumnaAlias();
  }
  public String getMsgError() {
    return MsgError;
  }
  /**
   * Establece el codigo de Reparto
   * @param codRep 
   */
  public void setTextReparto(String codRep)
  {
      cli_codrepL.setText(codRep);
  }
  @Override
  public void setText(String texto) {

    cli_codiE.setText(texto);
    if (! swControl)
        return; // Pasa de buscar y poner el nombre del 
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

  @Override
  public String getText() {
    return cli_codiE.getText();
  }
  /**
   * Devuelve el nombre del Cliente
   * @return String con el nombre del Cliente
   */
  public String getTextNomb() {
    return cli_nombE.getText();
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

  @Override
  public void setEnabled(boolean activo) {   
    cli_codiE.setEnabled(activo);
    Bcons.setEnabled(activo);
  }
  public boolean getEnabled() {
    return cli_codiE.isEnabled();
  }
  
  @Override
  public void setQuery(boolean modoQuery) {
    cli_codiE.setQuery(modoQuery);
    Bcons.setEnabled(!modoQuery);
  }
  @Override
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
        if (ayucli==null)
        {
          ayucli = new ayuClientes(eu, vl, dt)
          {
            @Override
            public void matar()
            {
               ej_consCli(ayucli);
            }
          };
          ayucli.setLocation(25, 25);
          if (intfr!=null)
            intfr.getLayeredPane().add(ayucli,1);
          else
            vl.add(ayucli,peso);
        }
        ayucli.consulta=false;
        ayucli.setVisible(true);
        if (intfr!=null)
        {
          intfr.setEnabled(false);
          intfr.setFoco(ayucli);
        }
        ayucli.setZona(zona);
        ayucli.iniciarVentana();
        if (nombCli!=null)
        {
          ayucli.setCliNomb(nombCli);
          ayucli.Bbuscar_actionPerformed();
        }
      }
      catch (Exception j)
      {        
        if (intfr != null)
          intfr.setEnabled(true);
      }

  }

  void ej_consCli(ayuClientes aycli)
  {
    evQuery=false;
    if (aycli.consulta)
    {
      cli_codiE.setText(aycli.getCliCodi());
      if (swControl)
         cli_nombE.setText(aycli.getCliNomb());
      setNomComercial(aycli.getNombCom());
      cli_codrepL.setText(aycli.getCodigoReparto());
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
    * @param cli_nombE CTextField poner a NULL si no se quiere actualizar el nombre
    *                  del producto.
    */
   public void setCliNomb(CTextField cli_nombE)
   {
     swControl = cli_nombE != null;

     this.remove(this.cli_nombE);
     
     if (!swControl)
       return;
     this.remove(Bcons);
     this.remove(cli_codiE);
     this.add(cli_codiE, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));
     this.cli_nombE=cli_nombE;
   }

  public String getNomComercial(){
    return cli_nomcL.getText();
  }

  public void setNomComercial(String nom){
    cli_nomcL.setText(nom);
  }
  
  
      
  public void resetTexto(){
    super.resetTexto();
    cli_nombE.setText("");
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
  /**
   * Añade un listener al foco.
   * El foco se añade al campo cli_codiE  (CTextField)  
   * @see  afterFocusLost(boolean noError)
   * @param f 
   */
  @Override
  public void addFocusListener(FocusListener f)
  {
      if (cli_codiE!=null)
          cli_codiE.addFocusListener(f);      
  }
  public CTextField getFieldCliCodi()
  {
      return cli_codiE;
  }
  
//  CTextField getFieldCliNomb()
//  {
//      return cli_nombE;
//  }
  /**
   * Pone enabled o disbled el campo nombre del cliente
   * @param enable   
   */
  public void  setEnabledNombre(boolean enable)
  {
      cli_nombE.setEnabled(enable);
  }
  /**
   * Devuelve si el campo nombre de cliente esta enabled
   * @return boolean Devuelve si el campo nombre de cliente esta enabled
   */
  public boolean  isEnabledNombre()
  {
      return cli_nombE.isEnabled();
  }
  /**
   * Quita un listener al foco. El foco se habria añadido al campo cli_codiE (CTextField)
   * @param f 
   */
  @Override
  public void removeFocusListener(FocusListener f)
  {
      if (cli_codiE!=null)
          cli_codiE.removeFocusListener(f);     
  }
  
}  // Final de Class



